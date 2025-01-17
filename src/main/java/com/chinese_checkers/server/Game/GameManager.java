package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Ruleset.CornerHelper;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;

/**
 * The GameManager class manages the state and logic of a game of Chinese Checkers.
 * It handles initializing the game, validating and executing moves, and determining the winner.
 */
public class GameManager {
    private Board board;
    private Ruleset ruleset;
    private int pawnsPerPlayer;
    private Optional<Corner> currentTurn;
    private ArrayList<Corner> takenCorners;
    private boolean hasJumped;
    private CornerHelper cornerHelper;

    /**
     * Constructs a GameManager object with the specified board, ruleset, and number of pawns per player.
     *
     * @param board the game board
     * @param ruleset the ruleset to use for the game
     * @param pawnsPerPlayer the number of pawns each player has
     */
    public GameManager(Board board, Ruleset ruleset, int pawnsPerPlayer) {
        this.board = board;
        this.ruleset = ruleset;
        this.pawnsPerPlayer = pawnsPerPlayer;
        this.currentTurn = Optional.empty();
        this.takenCorners = new ArrayList<>();
        this.hasJumped = false;
    }

    /**
     * Initializes the game with the specified players.
     *
     * @param players the players participating in the game
     * @return a GameStartMessage containing the initial state of the game
     */
    public GameStartMessage initializeGame(Collection<Player> players) {
        cornerHelper = new CornerHelper(players.size(), board);
        ArrayList<Corner> startingCorners = cornerHelper.getStartingCorners();
        GameStartMessage gameStartMessage = new GameStartMessage(board.getSize());
        
        for(Player player : players) {
            Corner corner = startingCorners.remove(0);
            player.setCorner(corner);
            takenCorners.add(corner);

            ArrayList<Position> startingPositions = ruleset.getStartingPositions(player.getCorner());
            for(int i = 0; i < pawnsPerPlayer; i++) {
                Pawn pawn = new Pawn(player);
                board.addPawn(pawn, startingPositions.get(i));
                gameStartMessage.addPawn(startingPositions.get(i), pawn);
            }
        }

        currentTurn = Optional.of(players.stream().skip((int)(players.size() * Math.random())).findFirst().get().getCorner());
        return gameStartMessage;
    }

    /**
     * Checks and executes a move for the specified pawn to the given position.
     *
     * @param pawnId the ID of the pawn to move
     * @param p the position to move the pawn to
     * @param player the player making the move
     * @return the result of the move
     */
    public MoveResult checkAndMove(Integer pawnId, Position p, Player player) {
        if(!isPlayerTurn(player)) {
            System.out.println("Player " + player.getName() + " tried to move when it's not their turn");
            return MoveResult.NOT_YOUR_TURN;
        }
        
        Pawn pawn = board.getPawnById(pawnId);

        if(!isValidPawn(pawn, player)) {
            System.out.println("Player " + player.getName() + " tried to move a pawn that doesn't belong to them or doesn't exist");
            return MoveResult.INVALID_PAWN;
        }

        MoveResult result = ruleset.validateMove(pawn, p);

        if(result == MoveResult.SUCCESS && !hasJumped) {
            System.out.println("Player " + player.getName() + " moved pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ")");
            board.movePawn(pawn, p);
        }
        else if(result == MoveResult.SUCCESS_JUMP) {
            System.out.println("Player " + player.getName() + " jumped pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ")");
            board.movePawn(pawn, p);
            hasJumped = true;
            return result;
        }
        else {
            System.out.println("Player " + player.getName() + " tried to move pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ") but it was invalid: " + result);
            return result;
        }

        if(checkWin(player)) {
            System.out.println("Player " + player.getName() + " won the game!");
            return MoveResult.GAME_OVER;
        }
        
        endTurn(player);
        return result;
    }

    /**
     * Checks if it is the specified player's turn.
     *
     * @param player the player to check
     * @return true if it is the player's turn, false otherwise
     */
    private boolean isPlayerTurn(Player player) {
        return player.getCorner().equals(currentTurn.orElse(null));
    }
    
    /**
     * Checks if the specified pawn belongs to the given player.
     *
     * @param pawn the pawn to check
     * @param player the player to check
     * @return true if the pawn belongs to the player, false otherwise
     */
    private boolean isValidPawn(Pawn pawn, Player player) {
        return pawn != null && pawn.getOwner().equals(player);
    }

    /**
     * Checks if the specified player has won the game.
     *
     * @param player the player to check
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(Player player) {
        Corner goalCorner = player.getCorner().getOpposite();
        Set<Position> winningPositions = new HashSet<>(ruleset.getStartingPositions(goalCorner));
        return board.getPlayerPawns(player.getCorner()).stream()
                    .map(board::getPositionOf)
                    .allMatch(winningPositions::contains);
    }

    /**
     * Gets the current player's turn.
     *
     * @return the current player's turn
     */
    public Corner getCurrentTurn() {
        return currentTurn.orElse(null);
    }

    /**
     * Ends the turn for the specified player and sets the next player's turn.
     *
     * @param player the player ending their turn
     */
    public void endTurn(Player player) {
        if(player.getCorner() != currentTurn.get()) {
            System.out.println("Player " + player.getName() + " tried to end turn when it's not their turn");
            return;
        }

        System.out.print("Changing turn from " + currentTurn + " to ");
        setNextTurn();
        System.out.println(currentTurn);
    }

    /**
     * Sets the next player's turn.
     */
    private void setNextTurn() {
        currentTurn = currentTurn.map(corner -> 
            takenCorners.get((takenCorners.indexOf(corner) + 1) % takenCorners.size())
        );
        hasJumped = false;
    } 
}