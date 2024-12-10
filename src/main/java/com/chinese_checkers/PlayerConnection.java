package com.chinese_checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;
import com.chinese_checkers.comms.Message.FromClient.*;
import com.chinese_checkers.comms.Message.FromServer.*;
import com.chinese_checkers.comms.CommandParser;

import com.chinese_checkers.comms.Message.AcknowledgeMessage;
import com.chinese_checkers.comms.Message.JoinMessage;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.Message.MoveMessage;

class PlayerConnection implements Runnable {

    private Server server;
    private Player player;
    private int playerID;
    private ServerSocket listener;
    private Socket clientSocket;
    private BufferedReader reciever;
    private PrintWriter sender;
    private CommandParser commandParser;
    private ReentrantLock socketLock;
    private boolean terminated = false;
    private boolean connected = false;


    public PlayerConnection(ServerSocket listener, ReentrantLock socketLock, Server server, int playerID) {
        this.listener = listener;
        this.socketLock = socketLock;
        this.server = server;
        this.commandParser = new CommandParser();

        this.playerID = playerID;

        //commandParser.addCommand("move", msg -> server.moveCallback((MoveMessage) msg, player));
        commandParser.addCommand("move_request", msg -> server.moveCallback((MoveRequestMessage) msg, player));
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

        RequestJoinMessage connectMessage = waitForJoinMessage();
        if (connectMessage == null) {
            throw new IOException("Invalid connection from player " + player.getName());
        }

        connected = true;
        player = new Player(connectMessage.getName(), playerID);
        System.out.println("Player " + connectMessage.getName() + " connected");

        // send data to player
        Message msg = new SelfDataMessage(playerID);
        send(msg)   ;
    }

    public RequestJoinMessage waitForJoinMessage() {

        try {
            String line = reciever.readLine();
            System.out.println("Received: " + line);
            if (line == null) return null;
                Message msg = Message.fromJson(line);

            if (msg != null && msg.getType().equals("request_join")) {
                sender.println(new ResponseMessage("request_join", "join request accepted").toJson());
                return (RequestJoinMessage) msg;
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
