package com.chinese_checkers.server.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.chinese_checkers.comms.Message.FromClient.MoveRequestMessage;
import com.chinese_checkers.comms.Message.FromServer.MovePlayerMessage;
import com.chinese_checkers.comms.Message.FromServer.ResponseMessage;
import com.chinese_checkers.server.Game.Player;
import com.chinese_checkers.comms.Message.JoinMessage;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.CommandParser;

public class Server {
    private int playerID = 1000;
    
    /**
     * Get the next avaiable player ID
     * @return ID of the next player
     */
    public int getplayerID() {
        return playerID++;
    }
    
    private final int playerCount;
    private final int port;

    private HashMap<Integer, PlayerConnection> playerConnections;
    private ServerSocket listener;
    private ExecutorService connectionPool;
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
        playerConnections = new HashMap<>(playerCount);

        try {
            listener = new ServerSocket(port);
            connectionPool = Executors.newFixedThreadPool(playerCount);

            for (int i = 0; i < playerCount; i++) {
                int playerID = getplayerID();
                PlayerConnection playerConn = new PlayerConnection(listener, socketLock, this, playerID);
                connectionPool.execute(playerConn);
                playerConnections.put(playerID, playerConn);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for players to connect...");

        while (true) {
            // count connected players
            int connectedPlayers = 0;
            for (PlayerConnection connection : playerConnections.values()) {
                if (connection.isConnected()) {
                    connectedPlayers++;
                }
            }

            if (connectedPlayers == playerCount) {
                break;
            }

            try {
                // the CPU is eepy
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All players connected, starting game");

    }

    public void stop() {
        for (PlayerConnection connection : playerConnections.values()) {
            connection.terminate();
        }
        connectionPool.shutdown();

        try {
            listener.close();
        } catch (IOException e) {
        }
    }

    private void sendToAll(Message msg) {
        for (PlayerConnection connections : playerConnections.values()) {
            connections.send(msg);
        }
    }

    private void sendToPlayer(int playerID, Message msg) {
        PlayerConnection playerConn = playerConnections.get(playerID);
        if (playerConn != null) {
            playerConn.send(msg);
        }
    }

    public void moveCallback(MoveRequestMessage msg, Player player) {
        // validate s, q, r
        // validate pawnID

        System.out.println("Player " + player.getName() + " moved pawn " + msg.pawnID + " to (" + msg.s + ", " + msg.q + ", " + msg.r + ")");

        // inform the player that the move was successful
        Message confirmMove = new ResponseMessage("move_request", "success");
        sendToPlayer(player.getId(), confirmMove);

        // Send the move to all players
        Message validatedMoveMsg = new MovePlayerMessage(player.getId(), msg.pawnID, msg.s, msg.q, msg.r);

        sendToAll(validatedMoveMsg);
    }

    private void joinCallback(JoinMessage msg) {
        System.out.println("Unexpected join command received");
    }

}

