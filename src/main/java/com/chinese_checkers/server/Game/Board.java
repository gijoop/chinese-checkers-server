package com.chinese_checkers.server.Game;

public interface Board {
    public void setPawns(Pawn[] pawns);
    public Pawn[] getPawns();
    public Pawn[] getPawns(Player.PawnColor color);
    public void movePiece(Pawn pawn, int s, int q, int r);
    public Pawn getPawnAt(int s, int q, int r);
    public Pawn getPawnById(int id);
}
