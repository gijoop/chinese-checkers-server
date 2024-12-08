package com.chinese_checkers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class Server {
    private static int ID = 1000;
    public static int getID() {
        return ID++;
    }
    
    private final int playerCount;
    private final int port;

    private PlayerConnection[] playerConns;
    private CommandParser commandParser;
    private ServerSocket listener;

    public Server(final int playerCount, final int port) throws IllegalArgumentException {
        if (playerCount < 2 || playerCount > 6) {
            throw new IllegalArgumentException("Player count must be between 2 and 6");
        }
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1024 and 65535");
        }

        this.playerCount = playerCount;
        this.port = port;
        this.commandParser = CommandParser.getInstance();

        commandParser.addCommand("move", msg -> System.out.println("Move command received"));
        commandParser.addCommand("connect", msg -> System.out.println("Connect command received"));
        commandParser.addCommand("disconnect", msg -> System.out.println("Disconnect command received"));
    }

    public void start() {
        System.out.println("Chinese Checkers Server started");
        this.playerConns = new PlayerConnection[playerCount];

        try {
            listener = new ServerSocket(port);
            ExecutorService threadPool = Executors.newFixedThreadPool(playerCount);
            ReentrantLock socketLock = new ReentrantLock();

            for (int i = 0; i < playerCount; i++) {
                playerConns[i] = new PlayerConnection(listener, socketLock);
                threadPool.execute(playerConns[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        for (PlayerConnection playerConn : playerConns) {
            if (playerConn != null) {
                playerConn.terminate();
            }
        }
    }
}
