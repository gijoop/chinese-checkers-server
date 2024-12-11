package com.chinese_checkers.server.Game;

public interface MoveValidator {
    public boolean isValidMove(Board board, Pawn pawn, int s, int q, int r);
}
