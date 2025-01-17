package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

/**
 * The Move class represents a move in the game of Chinese Checkers.
 * It includes the start and goal positions of the move, as well as the result of the move.
 */
public class Move {
    private Position start;
    private Position goal;
    private MoveResult result;

    /**
     * Constructs a Move object with the specified start and goal positions.
     *
     * @param start the starting position of the move
     * @param goal the goal position of the move
     */
    public Move(Position start, Position goal) {
        this.start = start;
        this.goal = goal;
        result = MoveResult.SUCCESS;
    }

    /**
     * Sets the result of the move.
     *
     * @param result the result of the move
     */
    public void setResult(MoveResult result) {
        if ((result == MoveResult.SUCCESS || result == MoveResult.SUCCESS_JUMP) 
            && this.result != MoveResult.SUCCESS) {
            return; // Can't change from a failed result to a successful one
        }
        this.result = result;
    }

    /**
     * Gets the starting position of the move.
     *
     * @return the starting position
     */
    public Position getStart() {
        return start;
    }

    /**
     * Gets the goal position of the move.
     *
     * @return the goal position
     */
    public Position getGoal() {
        return goal;
    }

    /**
     * Gets the result of the move.
     *
     * @return the result of the move
     */
    public MoveResult getResult() {
        return result;
    }

    /**
     * Returns a string representation of the move.
     *
     * @return a string representation of the move
     */
    @Override
    public String toString() {
        return "Move{" +
                "start=" + start +
                ", goal=" + goal +
                '}';
    }
}