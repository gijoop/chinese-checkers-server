package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Game.Move;

/**
 * The MoveValidator interface defines the method for validating a move in the game of Chinese Checkers.
 */
public interface MoveValidator {
    /**
     * Validates the specified move.
     *
     * @param move the move to validate
     */
    void validateMove(Move move);
}