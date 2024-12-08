package com.chinese_checkers;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Missing arguments, required args: <port> <players>");
            System.exit(1);
        }


        final int port = Integer.parseInt(args[0]);
        final int players = Integer.parseInt(args[1]);
        Server server = new Server(port, players);
        server.start();
    }
}