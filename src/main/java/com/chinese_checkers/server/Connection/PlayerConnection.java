package com.chinese_checkers.server.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;
import com.chinese_checkers.comms.Message.FromClient.*;
import com.chinese_checkers.comms.Message.FromServer.*;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.CommandParser;

/**
 * The PlayerConnection class manages the connection between the server and a single player.
 * It handles receiving and sending messages, establishing the connection, and processing commands.
 */
public class PlayerConnection implements Runnable {

    private Player player;
    private int playerID;
    private ServerSocket listener;
    private Socket clientSocket;
    private BufferedReader reciever;
    private PrintWriter sender;
    private CommandParser commandParser;
    private ReentrantLock socketLock;
    private boolean terminated = false;
    private boolean connected = false;
    private boolean waitingToJoin = false;
    
    /**
     * Constructs a PlayerConnection object with the specified listener, socket lock, server, and player ID.
     *
     * @param listener the server socket to listen for connections
     * @param socketLock the lock to synchronize socket access
     * @param server the server managing the game
     * @param playerID the ID of the player
     */
    public PlayerConnection(ServerSocket listener, ReentrantLock socketLock, Server server, int playerID) {
        this.listener = listener;
        this.socketLock = socketLock;
        this.playerID = playerID;
        
        commandParser = new CommandParser();
        commandParser.addCommand("move_request", msg -> server.moveCallback((MoveRequestMessage) msg, player));
        commandParser.addCommand("request_join", msg -> joinCallback((RequestJoinMessage) msg));
        commandParser.addCommand("end_turn", msg -> server.endTurnCallback((EndTurnMessage) msg, player));
    }

    /**
     * Sends a message to the player.
     *
     * @param message the message to send
     */
    public void send(Message message) {
        sender.println(message.toJson());
    }

    /**
     * Runs the player connection, establishing the connection and starting the listener.
     */
    @Override
    public void run() {
        try {
            establishConnection();
            startListener();

        } catch (IOException e) {
            if(!terminated){
                System.out.print("\n Connection error: " + e.getMessage());
            }
        } finally {
            terminate();
        }
    }

    /**
     * Establishes the connection with the player.
     *
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    private void establishConnection() throws IOException {
        socketLock.lock();
        try {
            clientSocket = listener.accept();
            reciever = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sender = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new SocketException("Connection failed");
        } finally {
            socketLock.unlock();
        }

        waitingToJoin = true;
    }

    /**
     * Starts the listener to receive messages from the player.
     *
     * @throws IOException if an I/O error occurs when reading from the input stream
     */
    private void startListener() throws IOException {
        while (!terminated) {
            try {
                String line = reciever.readLine(); // blocking if no line
                if (line == null) {
                    throw new IOException("Connection to player " + player.getName() + " lost. Player disconnected");
                }
                commandParser.parseCommand(Message.fromJson(line));

            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * Checks if the player is connected.
     *
     * @return true if the player is connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Gets the player associated with this connection.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Terminates the connection with the player.
     */
    public void terminate() {
        terminated = true;
        if (clientSocket != null){
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore exception
            }
        }
    }

    /**
     * Handles the join request from the player.
     *
     * @param msg the join request message
     */
    private void joinCallback(RequestJoinMessage msg) {
        if (waitingToJoin) {
            connected = true;
            player = new Player(msg.getName(), playerID);
            System.out.println("Player " + msg.getName() + " connected");

            send(new SelfDataMessage(playerID));
            
        }
        else {
            send(new ResponseMessage("request_join", ResponseMessage.Status.FAILURE, "join request denied"));
        }
    }
}