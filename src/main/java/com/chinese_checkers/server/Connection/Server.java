package com.chinese_checkers.server.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.chinese_checkers.comms.Message.FromClient.MoveRequestMessage;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;
import com.chinese_checkers.comms.Message.FromServer.MovePlayerMessage;
import com.chinese_checkers.comms.Message.FromServer.NextRoundMessage;
import com.chinese_checkers.comms.Message.FromServer.ResponseMessage;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.StandardBoard;
import com.chinese_checkers.server.Game.Ruleset.PlayerConfig;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.StandardRuleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.GameManager;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Message.Message;

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
    private GameManager gameManager;
    private ServerSocket listener;
    private ExecutorService connectionPool;
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
        int pawnsPerPlayer = 10;
        if(playerCount == 2) {
            pawnsPerPlayer = 15;
        }
        Board board = new StandardBoard(5);
        PlayerConfig playerConfig = new PlayerConfig(playerCount, board);
        Ruleset ruleset = new StandardRuleset(board, playerConfig);
        this.gameManager = new GameManager(board, ruleset, pawnsPerPlayer);
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

        GameStartMessage msg = gameManager.initializeGame(playerConnections.values()
                                    .stream()
                                    .map(PlayerConnection::getPlayer)
                                    .collect(Collectors.toList()));
        
        sendToAll(msg);

        NextRoundMessage nextRoundMsg = new NextRoundMessage(getPlayerOfTurn(gameManager.getCurrentTurn()));

        sendToAll(nextRoundMsg);
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
        Position pos = new Position(msg.x, msg.y);

        Corner startTurn = gameManager.getCurrentTurn();
        MoveResult result = gameManager.checkAndMove(msg.pawnID, pos, player);

        sendToPlayer(player.getId(), new ResponseMessage("move_request", result.toString()));
        if(result == MoveResult.GAME_OVER) {
            sendToAll(new ResponseMessage("game_over", "Game over! Player " + player.getName() + " has won!"));
            stop();
            return;
        }
        if (result != MoveResult.SUCCESS) {
            return;
        }

        Message validatedMoveMsg = new MovePlayerMessage(player.getId(), msg.pawnID, msg.x, msg.y);
        sendToAll(validatedMoveMsg);

        if (gameManager.getCurrentTurn() != startTurn) {
            sendToAll(new NextRoundMessage(getPlayerOfTurn(gameManager.getCurrentTurn())));
        }
    }

    public void endTurnCallback(Player player) {
        gameManager.endTurn(player);
        sendToAll(new NextRoundMessage(getPlayerOfTurn(gameManager.getCurrentTurn())));
    }

    private int getPlayerOfTurn(Corner turn) {
        return playerConnections.values().stream()
                .filter(connection -> connection.getPlayer().getCorner() == turn)
                .map(connection -> connection.getPlayer().getId())
                .findFirst()
                .orElse(-1);
    }
}

