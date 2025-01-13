package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;

public class BaseMoveValidator implements MoveValidator {

    public MoveResult isValidMove(Board board, Pawn pawn, Position position) {
        return MoveResult.SUCCESS;
    }
    
}
