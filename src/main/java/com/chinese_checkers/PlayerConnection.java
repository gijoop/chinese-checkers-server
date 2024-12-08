package com.chinese_checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import com.chinese_checkers.Message.AcknowledgeMessage;
import com.chinese_checkers.Message.ConnectMessage;
import com.chinese_checkers.Message.Message;

class PlayerConnection implements Runnable {

    private Player player;
    private ServerSocket listener;
    private Socket clientSocket;
    private BufferedReader reciever;
    private PrintWriter sender;
    private CommandParser commandParser = CommandParser.getInstance();
    private ReentrantLock socketLock;


    public PlayerConnection(ServerSocket listener, ReentrantLock socketLock) {
        this.listener = listener;
        this.socketLock = socketLock;
    }

    public void send(Message message) {
        sender.println(message.toJson());
    }

    private void establishConnection() {
        socketLock.lock();
        try {
            clientSocket = listener.accept();

            reciever = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sender = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socketLock.unlock();
        }

        ConnectMessage connectMessage = waitForConnectMessage();
        if (connectMessage == null) {
            System.out.println("Invalid connection from player " + player.getName());
            terminate();
        }

        System.out.println("Player " + connectMessage.getName() + " connected");
        player = new Player(connectMessage.getName(), Server.getID(), connectMessage.getColor());
        
    }

    public ConnectMessage waitForConnectMessage() {

        try {
            String line = reciever.readLine();
            if (line == null) return null;
            Message msg = Message.fromJson(line);

            if (msg != null && msg.getType().equals("connect")) {
                sender.println(new AcknowledgeMessage("Successfully connected").toJson());
                return (ConnectMessage) msg;
            } else {
                sender.println("ERROR: Invalid connection message");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void run() {
        try {
            establishConnection();
            parseCommands();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            terminate();
        }
    }
    
    public void terminate() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCommands() {
        while (true) {
            try {
                String line = reciever.readLine();
                if (line == null) {
                    System.out.println("Player " + player.getName() + " disconnected");
                    break;
                }
                Message msg = Message.fromJson(line);
                System.out.println("Received message: " + msg.toJson());
                commandParser.parseCommand(msg);
            } catch (IOException e) {
                System.out.println("Player " + player.getName() + " disconnected");
                break;
            }
        }
    }

}
