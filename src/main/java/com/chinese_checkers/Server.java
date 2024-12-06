package com.chinese_checkers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {

    private int latestID = 1000;


    // Board cells numbered 0-8, top to bottom, left to right; null if empty
    private PlayerConnection[] playerConns;

    PlayerConnection currentPlayer;
    ServerSocket listener;

    public Server(final int playerCount) {
        System.out.println("Chinese Checkers Server is Running...");
        System.out.println("Waiting for all players to connect...");
        playerConns = new PlayerConnection[playerCount];

        try {
            listener = new ServerSocket(58901);
            ExecutorService threadPool = Executors.newFixedThreadPool(playerCount);

            for (int i = 0; i < playerCount; i++) {
                playerConns[i] = new PlayerConnection(listener.accept(), new Player("Player " + (i + 1), latestID++, Player.CheckerColor.values()[i]));
                threadPool.execute(playerConns[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("All players connected, starting game...");
    }

    class PlayerConnection implements Runnable {
        Player player;
    
        Socket socket;
        Scanner reciever;
        PrintWriter sender;
    
        public PlayerConnection(Socket socket, Player player) {
            this.socket = socket;
            this.player = player;
        }
    
        @Override
        public void run() {
            try {
                setup();
                processCommands();
    
            } catch (Exception e) {
                e.printStackTrace();
    
            } finally {
                try {
                    socket.close();
    
                } catch (IOException e) {
                }
            }
        }
    
        private void setup() throws IOException {
            System.out.println("Player " + player.getName() + " connected (ID: " + player.getId() + ")");
            reciever = new Scanner(socket.getInputStream());
            sender = new PrintWriter(socket.getOutputStream(), true);
            sender.println("Welcome Player " + player.getName() + " (ID: " + player.getId() + ")");
        }
    
        private void processCommands() {
            while (reciever.hasNextLine()) {
                String command = reciever.nextLine();
                System.out.println("Recieved command: " + command + " from player " + player.getName() + " (ID: " + player.getId() + ")");
    
                // for(PlayerConnection player : playerConns) {
                //     if(player != this && player != null) {
                //         player.sender.println("PLAYER " + UID + " : " + command);
                //     }
                // }
            }
        }
    }
}