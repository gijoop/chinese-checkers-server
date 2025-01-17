package com.chinese_checkers.server.Game.Ruleset;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;

/**
 * The Ruleset interface defines the rules and behaviors for a game of Chinese Checkers.
 * It includes methods for validating moves, checking if a position is in bounds,
 * and retrieving starting positions and reachable moves or jumps.
 */
public interface Ruleset {
    /**
     * The MoveResult enum represents the possible outcomes of a move.
     */
    public enum MoveResult {
        SUCCESS,
        SUCCESS_JUMP,
        SUCCESS_WIN,
        INVALID_MOVE,
        OCCUPIED,
        OUT_OF_BOUNDS,
        INVALID_PAWN,
        NOT_YOUR_TURN,
        UNREACHABLE,
        OUT_OF_GOAL
    }

    /**
     * Validates a move for a given pawn to a specified position.
     *
     * @param pawn the pawn to move
     * @param position the position to move the pawn to
     * @return the result of the move validation
     */
    public MoveResult validateMove(Pawn pawn, Position position);

    /**
     * Checks if a given position is within the bounds of the game board.
     *
     * @param position the position to check
     * @return true if the position is in bounds, false otherwise
     */
    public boolean isInBounds(Position position);

    /**
     * Gets the starting positions for a given corner.
     *
     * @param corner the corner to get the starting positions for
     * @return a list of starting positions
     */
    public ArrayList<Position> getStartingPositions(Corner corner);

    /**
     * Gets the reachable moves from a given position.
     *
     * @param position the position to get the reachable moves from
     * @return a list of reachable positions
     */
    public ArrayList<Position> getReachableMoves(Position position);

    /**
     * Gets the reachable jumps from a given position.
     *
     * @param position the position to get the reachable jumps from
     * @return a list of reachable jump positions
     */
    public ArrayList<Position> getReachableJumps(Position position);
}