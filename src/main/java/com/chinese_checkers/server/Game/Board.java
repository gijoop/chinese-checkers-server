package com.chinese_checkers.server.Game;

public interface Board {
    public void movePiece(Pawn pawn, int s, int q, int r);
    public Pawn getPawnAt(int s, int q, int r);
    public Pawn getPawnById(int id);
}
