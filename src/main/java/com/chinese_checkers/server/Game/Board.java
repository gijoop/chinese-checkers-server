package com.chinese_checkers.server.Game;

public interface Board {
    public void addPawn(Pawn pawn, Position position);
    public void movePawn(Pawn pawn, Position position);
    public Pawn getPawnAt(Position position);
    public Position getPositionOf(Pawn pawn);
    public boolean isOccupied(Position position);
    public void printBoard();
}
