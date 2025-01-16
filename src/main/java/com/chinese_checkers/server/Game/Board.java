package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Move;
import com.chinese_checkers.comms.Player.Corner;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;

public interface Board {
    public void addPawn(Pawn pawn, Position position);
    public void movePawn(Move move);
    public Pawn getPawnAt(Position position);
    public Pawn getPawnById(int id);
    public Position getPositionOf(Pawn pawn);
    public Integer getSize();
    public ArrayList<Pawn> getPlayerPawns(Corner corner);
    public boolean isOccupied(Position position);
    public void printBoard();
}
