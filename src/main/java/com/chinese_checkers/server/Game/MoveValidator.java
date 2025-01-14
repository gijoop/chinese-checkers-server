package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;

public interface MoveValidator {
    enum MoveResult {
        SUCCESS,
        INVALID_MOVE,
        OCCUPIED,
        OUT_OF_BOUNDS,
        NONEXISTENT_PAWN,
        NOT_YOUR_TURN
    }
    public MoveResult isValidMove(Board board, Pawn pawn, Position position);
}
