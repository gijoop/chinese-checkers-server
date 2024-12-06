package com.chinese_checkers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
