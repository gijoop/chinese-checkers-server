package com.chinese_checkers.server.Game;

public class BaseMoveValidator implements MoveValidator {

    public boolean isValidMove(Board board, Pawn pawn, int s, int q, int r) {
        return true;
    }
    
}
