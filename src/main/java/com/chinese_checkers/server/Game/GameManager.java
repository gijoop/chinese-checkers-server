package com.chinese_checkers.server.Game;

public class GameManager {
    private Board board;
    private MoveValidator moveValidator;

    public GameManager(Board board, MoveValidator moveValidator) {
        this.board = board;
        this.moveValidator = moveValidator;
    }

    public boolean checkAndMove(Pawn pawn, int s, int q, int r) {
        if (moveValidator.isValidMove(board, pawn, s, q, r)) {
            board.movePiece(pawn, s, q, r);
            return true;
        }
        return false;
    }
}
