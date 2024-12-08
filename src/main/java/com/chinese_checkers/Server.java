package com.chinese_checkers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.chinese_checkers.Message.JoinMessage;
import com.chinese_checkers.Message.Message;
import com.chinese_checkers.Message.ServerMoveMessage;

class Server {
    private static int playerID = 1000;
    
    /**
     * Get the next avaiable player ID
     * @return ID of the next player
     */
    public static int getplayerID() {
        return playerID++;
    }
    
    private final int playerCount;
    private final int port;

    private HashMap<Player, PlayerConnection> playerConns = new HashMap<Player, PlayerConnection>();
    private CommandParser commandParser = CommandParser.getInstance();
    private ServerSocket listener;
    private ReentrantLock socketLock = new ReentrantLock();

    public Server(final int playerCount, final int port) throws IllegalArgumentException {
        if (playerCount < 2 || playerCount > 6) {
            throw new IllegalArgumentException("Player count must be between 2 and 6");
        }
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1024 and 65535");
        }

        this.playerCount = playerCount;
        this.port = port;

        commandParser.addCommand("serverMove", msg -> serverMoveCallback((ServerMoveMessage)msg));
        commandParser.addCommand("join", msg -> joinCallback((JoinMessage)msg));
        commandParser.addCommand("disconnect", msg -> disconnectCallback(msg));
    }

    public void start() {
        System.out.println("Server started on port " + port);

        try {
            listener = new ServerSocket(port);
            ExecutorService threadPool = Executors.newFixedThreadPool(playerCount);

            for (int i = 0; i < playerCount; i++) {
                Player player = new Player(getplayerID());
                PlayerConnection playerConn = new PlayerConnection(listener, socketLock, player);
                playerConns.put(player, playerConn);
                threadPool.execute(playerConn);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for players to connect...");

        while (true) {
            boolean allConnected = true;
            for (PlayerConnection playerConn : playerConns.values()) {
                if (playerConn == null || !playerConn.isConnected()) {
                    allConnected = false;
                    break;
                }
            }
            if (allConnected) {
                break;
            }
        }

        System.out.println("All players connected, starting game");

    }

    public void stop() {
        for (PlayerConnection playerConn : playerConns.values()) {
            if (playerConn != null) {
                playerConn.terminate();
            }
        }
        try {
            listener.close();
        } catch (IOException e) {
        }
    }

    private void sendToAll(Message msg) {
        for (PlayerConnection playerConn : playerConns.values()) {
            if (playerConn != null) {
                playerConn.send(msg);
            }
        }
    }

    private void serverMoveCallback(ServerMoveMessage msg) {
        sendToAll(msg);
    }

    private void joinCallback(JoinMessage msg) {
        System.out.println("Unexpected join command received");
    }

    private void disconnectCallback(Message msg) {
        System.out.println("Disconnect command received");
    }

}

