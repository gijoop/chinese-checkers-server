package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;

public interface Board {
    public void addPawn(Pawn pawn, Position position);
    public void movePawn(Pawn pawn, Position position);
    public Pawn getPawnAt(Position position);
    public Pawn getPawnById(int id);
    public Position getPositionOf(Pawn pawn);
    public Integer getSize();
    public ArrayList<Pawn> getPlayerPawns(Corner corner);
    public ArrayList<Position> getStartingPositions(Corner corner);
    public ArrayList<Position> getValidMoves(Position position);
    public ArrayList<Position> getValidJumps(Position position);
    public boolean isValidPosition(Position position);
    public boolean isOccupied(Position position);
    public void printBoard();
}
