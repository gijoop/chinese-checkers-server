package com.chinese_checkers.server.Game;

public interface MoveValidator {
    enum MoveResult {
        SUCCESS,
        INVALID_MOVE,
        OCCUPIED,
        OUT_OF_BOUNDS,
        NONEXISTENT_PAWN
    }
    public MoveResult isValidMove(Board board, Pawn pawn, Position position);
}
