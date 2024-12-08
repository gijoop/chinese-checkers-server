package com.chinese_checkers;

public class CLI {
    public CLI() {
    }

    public void start() {
        System.out.println("Chinese Checkers Server CLI started");
        System.out.println("Usage: <command> <args>\n Available commands: \n" +
        "- set_port <1024 - 65535> \n " +
        "- set_player_count <2 - 10> \n " +
        "- start\n " +
        "- exit\n");
        System.out.println("Default port: 58901\nDefault player count: 2");

        int playerCount = 2;
        int port = 58901;

        while (true) {
            System.out.print("> ");
            String input = System.console().readLine();
            String[] tokens = input.split(" ");

            if (tokens[0].equals("set_port")) {
                if (tokens.length != 2) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }

                port = Integer.parseInt(tokens[1]);
                if (port < 1024 || port > 65535) {
                    System.out.println("Port must be between 1024 and 65535");
                    continue;
                }

                System.out.println("Port set to " + port);
            } else if (tokens[0].equals("set_player_count")) {
                if (tokens.length != 2) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }

                playerCount = Integer.parseInt(tokens[1]);
                if (playerCount < 2 || playerCount > 10) {
                    System.out.println("Player count must be between 2 and 10");
                    continue;
                }

                System.out.println("Player count set to " + playerCount);
            } else if (tokens[0].equals("start")) {
                Server server = new Server(playerCount, port);
                server.start();
            }
            else if (tokens[0].equals("exit")) {
                break;
            } else if (tokens[0].trim().equals("")) {
                continue;  
            }else {
                System.out.println("Invalid command");
            }
        }
    }
}
