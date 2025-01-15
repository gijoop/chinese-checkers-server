package com.chinese_checkers.server.Connection;

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
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.CommandParser;

public class PlayerConnection implements Runnable {

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
    private boolean waitingToJoin = false;


    public PlayerConnection(ServerSocket listener, ReentrantLock socketLock, Server server, int playerID) {
        this.listener = listener;
        this.socketLock = socketLock;
        this.playerID = playerID;
        
        commandParser = new CommandParser();
        commandParser.addCommand("move_request", msg -> server.moveCallback((MoveRequestMessage) msg, player));
        commandParser.addCommand("request_join", msg -> joinCallback((RequestJoinMessage) msg));
        commandParser.addCommand("end_turn", msg -> server.endTurnCallback(player));
    }

    public void send(Message message) {
        sender.println(message.toJson());
    }

    @Override
    public void run() {
        try {
            establishConnection();
            startListener();

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

        waitingToJoin = true;
    }

    private void startListener() throws IOException {
        while (!terminated) {
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

    public Player getPlayer() {
        return player;
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

    private void joinCallback(RequestJoinMessage msg) {
        if (waitingToJoin) {
            // Validate message

            connected = true;
            player = new Player(msg.getName(), playerID);
            System.out.println("Player " + msg.getName() + " connected");

            // send ACK to player
            send(new SelfDataMessage(playerID));
        }
        else {
            send(new ResponseMessage("request_join", "join request denied"));
        }
    }
}
