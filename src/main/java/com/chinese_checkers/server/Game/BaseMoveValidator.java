package com.chinese_checkers.server.Game;

public class BaseMoveValidator implements MoveValidator {

    public MoveResult isValidMove(Board board, Pawn pawn, Position position) {
        return MoveResult.SUCCESS;
    }
    
}
