package com.chinese_checkers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.chinese_checkers.comms.Message.JoinMessage;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.Message.MoveMessage;

class Server {
    private static int playerID = 1000;
    
    /**
     * Get the next avaiable player ID
     * @return ID of the next player
     */
    public int getplayerID() {
        return playerID++;
    }
    
    private final int playerCount;
    private final int port;

    private PlayerConnection playerConns[];
    private ServerSocket listener;
    private CommandParser commandParser = new CommandParser();
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

        commandParser.addCommand("join", msg -> joinCallback((JoinMessage)msg));
    }

    public void start() {
        System.out.println("Server started on port " + port);
        playerConns = new PlayerConnection[playerCount];

        try {
            listener = new ServerSocket(port);
            ExecutorService threadPool = Executors.newFixedThreadPool(playerCount);

            for (int i = 0; i < playerCount; i++) {
                PlayerConnection playerConn = new PlayerConnection(listener, socketLock, this);
                threadPool.execute(playerConn);
                playerConns[i] = playerConn;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for players to connect...");

        while (true) {
            boolean allConnected = true;
            for (PlayerConnection playerConn : playerConns) {
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
        for (PlayerConnection playerConn : playerConns) {
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
        for (PlayerConnection playerConn : playerConns) {
            if (playerConn != null) {
                playerConn.send(msg);
            }
        }
    }

    public void moveCallback(MoveMessage msg, Player player) {
        System.out.println("Player " + player.getName() + " moved from " + msg.getFrom() + " to " + msg.getTo());

        // Send the move to all players

        sendToAll(msg);
    }

    private void joinCallback(JoinMessage msg) {
        System.out.println("Unexpected join command received");
    }

}

