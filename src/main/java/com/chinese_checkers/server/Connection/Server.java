package com.chinese_checkers.server.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.chinese_checkers.comms.Message.FromClient.EndTurnMessage;
import com.chinese_checkers.comms.Message.FromClient.MoveRequestMessage;
import com.chinese_checkers.comms.Message.FromServer.AnnounceWinnerMessage;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;
import com.chinese_checkers.comms.Message.FromServer.MovePlayerMessage;
import com.chinese_checkers.comms.Message.FromServer.NextRoundMessage;
import com.chinese_checkers.comms.Message.FromServer.ResponseMessage;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Ruleset.CornerHelper;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;
import com.chinese_checkers.server.DBConnection.Game;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.GameManager;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Message.Message;

/**
 * The Server class manages the server-side logic for a game of Chinese Checkers.
 * It handles player connections, sending and receiving messages, and executing actions based on them.
 */
public class Server {
    private int playerID = 1000;
    
    /**
     * Get the next available player ID.
     *
     * @return ID of the next player
     */
    public int getPlayerID() {
        return playerID++;
    }
    
    private final int playerCount;
    private final int humanCount;
    private final int botCount;
    private final int port;
    private int latestPlace = 1;

    private HashMap<Integer, PlayerConnection> playerConnections;
    private final GameManager gameManager;
    private ServerSocket listener;
    private ExecutorService connectionPool;
    private ReentrantLock socketLock = new ReentrantLock();

    /**
     * Constructs a Server object with the specified player count and port.
     *
     * @param playerCount the number of players in the game
     * @param port the port to run the server on
     * @throws IllegalArgumentException if the player count or port is out of valid range
     */
    public Server(
        final int playerCount, 
        final int botCount, 
        final int port, 
        Ruleset ruleset, 
        Board board, 
        CornerHelper cornerHelper) 
        throws IllegalArgumentException {

        if (playerCount < 2 || playerCount > 6) {
            throw new IllegalArgumentException("Player count must be between 2 and 6");
        }
        if (botCount < 0 || botCount >= playerCount) {
            throw new IllegalArgumentException("Bot count must be between 0 and player count - 1");
        }
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1024 and 65535");
        }

        this.playerCount = playerCount;
        this.botCount = botCount;
        this.humanCount = playerCount - botCount;

        this.port = port;
        int pawnsPerPlayer = 10;
        if(playerCount == 2) {
            pawnsPerPlayer = 15;
        }
        this.gameManager = new GameManager(board, ruleset, pawnsPerPlayer);
    }

    /**
     * Starts the server and waits for players to connect.
     */
    public void start(Game loadGame) {
        System.out.println("Server started on port " + port);
        playerConnections = new HashMap<>(playerCount);

        try {
            listener = new ServerSocket(port);
            connectionPool = Executors.newFixedThreadPool(humanCount);

            for (int i = 0; i < humanCount; i++) {
                int playerID = getPlayerID();
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

            if (connectedPlayers == humanCount) {
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // add bots
        for (int i = 0; i < botCount; i++) {
            int playerID = getPlayerID();
            BotConnection botConn = new BotConnection(listener, socketLock, this, playerID);
            connectionPool.execute(botConn);
            playerConnections.put(playerID, botConn);
        }

        System.out.println("All players connected, starting game");

        GameStartMessage msg = gameManager.initializeGame(playerConnections.values()
                                    .stream()
                                    .map(PlayerConnection::getPlayer)
                                    .collect(Collectors.toList()), loadGame);
        
        sendToAll(msg);

        NextRoundMessage nextRoundMsg = new NextRoundMessage(getPlayerOfTurn());

        sendToAll(nextRoundMsg);
    }

    /**
     * Stops the server and terminates all player connections.
     */
    public void stop() {
        for (PlayerConnection connection : playerConnections.values()) {
            connection.terminate();
        }
        connectionPool.shutdown();

        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to all connected players.
     *
     * @param msg the message to send
     */
    private void sendToAll(Message msg) {
        for (PlayerConnection connections : playerConnections.values()) {
            connections.send(msg);
        }
    }

    /**
     * Sends a message to a specific player.
     *
     * @param playerID the ID of the player to send the message to
     * @param msg the message to send
     */
    private void sendToPlayer(int playerID, Message msg) {
        PlayerConnection playerConn = playerConnections.get(playerID);
        if (playerConn != null) {
            playerConn.send(msg);
        }
    }

    /**
     * Handles a move request from a player.
     *
     * @param msg the move request message
     * @param player the player making the move
     */
    public void moveCallback(MoveRequestMessage msg, Player player) {
        Position pos = new Position(msg.x, msg.y);

        Corner startTurn = gameManager.getCurrentTurn();
        MoveResult result = gameManager.checkAndMove(msg.pawnID, pos, player);

        ResponseMessage responseMsg = new ResponseMessage(
            "move_request",
            ResponseMessage.Status.SUCCESS,
            result.toString());

        if(result == MoveResult.SUCCESS_WIN) {
            sendToAll(new AnnounceWinnerMessage(player.getId(), latestPlace++));
            
        }else if(result != MoveResult.SUCCESS && result != MoveResult.SUCCESS_JUMP){
            responseMsg.setStatus(ResponseMessage.Status.FAILURE);
            sendToPlayer(player.getId(), responseMsg);
            return;
        }

        sendToPlayer(player.getId(), responseMsg);

        Message validatedMoveMsg = new MovePlayerMessage(player.getId(), msg.pawnID, msg.x, msg.y);
        sendToAll(validatedMoveMsg);

        if (gameManager.getCurrentTurn() != startTurn) {
            sendToAll(new NextRoundMessage(getPlayerOfTurn()));
        }
    }

    /**
     * Handles the end turn request from a player.
     *
     * @param player the player ending their turn
     */
    public void endTurnCallback(EndTurnMessage msg, Player player) {
        gameManager.endTurn(player);
        sendToAll(new NextRoundMessage(getPlayerOfTurn()));
    }

    /**
     * Gets the ID of the player whose turn it is.
     *
     * @return the ID of the player whose turn it is
     */
    private int getPlayerOfTurn() {
        return playerConnections.values().stream()
                .filter(connection -> connection.getPlayer().getCorner() == gameManager.getCurrentTurn())
                .map(connection -> connection.getPlayer().getId())
                .findFirst()
                .orElse(-1);
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}