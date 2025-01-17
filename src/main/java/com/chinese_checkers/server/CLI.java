package com.chinese_checkers.server;

import com.chinese_checkers.server.Connection.Server;

/**
 * The CLI class provides a command-line interface for the Chinese Checkers Server.
 * It allows users to configure the server's port and player count, start and stop the server, and exit the CLI.
 * 
 * Commands:
 * - set_port [1024 - 65535]: Sets the server port to the specified value within the range.
 * - set_player_count [2 - 10]: Sets the number of players for the server within the specified range.
 * - start: Starts the server with the configured port and player count.
 * - stop: Stops the server if it is running.
 * - exit: Exits the CLI and stops the server if it is running.
 * 
 * Default values:
 * - Default port: 12345
 * - Default player count: 2
 * 
 * Usage:
 * [command] [args]
 * 
 * Example:
 *  set_port 8080
 *  set_player_count 4
 *  start
 *  stop
 *  exit
 */
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
        
        int playerCount = 2;
        int port = 12345;
        Server server = null;
        System.out.println("Default port: " + port + "\nDefault player count: " + playerCount);

        while (true) {
            System.out.print("> ");
            String input = System.console().readLine();
            String[] tokens = input.split(" ");

            if (tokens[0].equals("set_port")) {
                if (tokens.length != 2) {
                    System.out.println("Invalid number of arguments");
                    continue;
                }
                
                try {
                    port = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Port must be an integer");
                    continue;
                }

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
                
                try {
                    playerCount = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Player count must be an integer");
                    continue;
                }

                if (playerCount < 2 || playerCount > 10) {
                    System.out.println("Player count must be between 2 and 10");
                    continue;
                }


                System.out.println("Player count set to " + playerCount);
            } else if (tokens[0].equals("start")) {
                if (server != null) {
                    System.out.println("Server already started");
                    continue;
                }
                server = new Server(playerCount, port);
                server.start();
            } else if (tokens[0].equals("stop")) {
                if (server != null) {
                    server.stop();
                }
                server = null;
                System.out.println("Server stopped");
            }
            else if (tokens[0].equals("exit")) {
                if (server != null) {
                    server.stop();
                }
                System.out.println("Exiting...");
                break;
            } else if (tokens[0].trim().equals("")) {
                continue;  
            }else {
                System.out.println("Invalid command");
            }
        }
    }
}
