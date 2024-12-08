package com.chinese_checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;

import com.chinese_checkers.comms.Message.AcknowledgeMessage;
import com.chinese_checkers.comms.Message.JoinMessage;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.Message.MoveMessage;

class PlayerConnection implements Runnable {

    private Server server;
    private Player player;
    private ServerSocket listener;
    private Socket clientSocket;
    private BufferedReader reciever;
    private PrintWriter sender;
    private CommandParser commandParser;
    private ReentrantLock socketLock;
    private boolean terminated = false;
    private boolean connected = false;


    public PlayerConnection(ServerSocket listener, ReentrantLock socketLock, Server server) {
        this.listener = listener;
        this.socketLock = socketLock;
        this.server = server;
        this.commandParser = new CommandParser();

        commandParser.addCommand("move", msg -> server.moveCallback((MoveMessage) msg, player));
    }

    public void send(Message message) {
        sender.println(message.toJson());
    }

    @Override
    public void run() {
        try {
            establishConnection();
            parseCommands();

        } catch (IOException e) {
            if(!terminated){
                System.out.print("\n Connection error: " + e.getMessage());
            }
        } finally {
            terminate();
        }
    }

    private void establishConnection() throws IOException {
        socketLock.lock();
        try {
            clientSocket = listener.accept();
            reciever = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sender = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new SocketException("Connection failed");
        } finally {
            socketLock.unlock();
        }

        JoinMessage connectMessage = waitForJoinMessage();
        if (connectMessage == null) {
            throw new IOException("Invalid connection from player " + player.getName());
        }

        connected = true;
        player = new Player(connectMessage.getName(), server.getplayerID());
        System.out.println("Player " + connectMessage.getName() + " connected");
        
    }

    public JoinMessage waitForJoinMessage() {

        try {
            String line = reciever.readLine();
            if (line == null) return null;
            Message msg = Message.fromJson(line);

            if (msg != null && msg.getType().equals("join")) {
                sender.println(new AcknowledgeMessage("Successfully connected").toJson());
                return (JoinMessage) msg;
            } else {
                sender.println("ERROR: Invalid join message");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void parseCommands() throws IOException {
        while (true) {
            try {
                String line = reciever.readLine(); // blocking if no line
                if (line == null) {
                    throw new IOException("Connection to player " + player.getName() + " lost. Player disconnected");
                }
                commandParser.parseCommand(Message.fromJson(line));

            } catch (IOException e) {
                throw e;
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public int getPlayerID() {
        return player.getId();
    }

    public void terminate() {
        terminated = true;
        if (clientSocket != null){
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
