package com.chinese_checkers;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.chinese_checkers.comms.Message.JoinMessage;
import com.chinese_checkers.comms.Message.MoveMessage;


public class Client_CLI {

    private String serverAddress = "localhost";

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;
    
    public Client_CLI(){
    }

    public void start() {
        System.out.println("Chinese Checkers Client CLI started");
        System.out.println("Usage: <command> <args>\n Available commands: \n" +
        "- set_address <server_address> \n " +
        "- join <player_name> <checker_color> \n " +
        "- exit\n");

        while (true) {
            System.out.print("> ");
            String input = System.console().readLine();
            String[] tokens = input.split(" ");

            if (tokens[0].equals("set_address")) {
                if (tokens.length != 2) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }
                
                serverAddress = tokens[1];
                System.out.println("Server address: " + serverAddress);

            } else if (tokens[0].equals("connect")) {
                if (tokens.length != 1) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }

                try {
                    socket = new Socket(serverAddress, 58901);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connected = true;
   
            } else if (tokens[0].equals("join")) {
                if (tokens.length != 3) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }

                if (!connected) {
                    System.out.println("Not connected to server");
                    continue;
                }

                String playerName = tokens[1];
                JoinMessage joinMessage = new JoinMessage(playerName);

                out.println(joinMessage.toJson());
                System.out.println("Join message sent");
                
            }else if (tokens[0].equals("move")) {
                if (tokens.length != 3) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }

                if (!connected) {
                    System.out.println("Not connected to server");
                    continue;
                }

                String from = tokens[1];
                String to = tokens[2];

                out.println(new MoveMessage(from, to).toJson());
                System.out.println("Move message sent");
                try {
                    String response = in.readLine();
                    System.out.println("Server response: " + response);
                    String response2 = in.readLine();
                    System.out.println("Server response: " + response2);
                    String response3 = in.readLine();
                    System.out.println("Server response: " + response3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            else if (tokens[0].equals("exit")) {
                break;
            } else {
                System.out.println("Invalid command");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Client_CLI client = new Client_CLI();
        client.start();
    }
}
