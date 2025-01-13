package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;

public interface Board {
    enum Corner {
        UPPER,
        UPPER_LEFT,
        LOWER_LEFT,
        LOWER,
        LOWER_RIGHT,
        UPPER_RIGHT
    }
    public void addPawn(Pawn pawn, Position position);
    public void movePawn(Pawn pawn, Position position);
    public Pawn getPawnAt(Position position);
    public Pawn getPawnById(int id);
    public Position getPositionOf(Pawn pawn);
    public Integer getSize();
    public ArrayList<Position> getStartingPositions(Corner corner);
    public boolean isOccupied(Position position);
    public void printBoard();
}
