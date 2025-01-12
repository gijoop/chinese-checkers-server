package com.chinese_checkers.server.Game;

import java.util.HashMap;

public class BaseBoard implements Board {
    private HashMap<Pawn, Position> pawnToPos;
    private HashMap<Position, Pawn> posToPawn;

    public BaseBoard() {
        pawnToPos = new HashMap<>();
        posToPawn = new HashMap<>();
    }

    public void addPawn(Pawn pawn, Position position) {
        pawnToPos.put(pawn, position);
        posToPawn.put(position, pawn);
    }

    public void movePawn(Pawn pawn, Position goal) {
        Position start = pawnToPos.get(pawn);
        pawnToPos.put(pawn, goal);
        posToPawn.remove(start);
        posToPawn.put(goal, pawn);
    }

    public Pawn getPawnAt(Position position) {
        return posToPawn.get(position);
    }

    public Position getPositionOf(Pawn pawn) {
        return pawnToPos.get(pawn);
    }

    public boolean isOccupied(Position position) {
        return posToPawn.containsKey(position);
    }

    public void printBoard() {
        for (Pawn p : pawnToPos.keySet()) {
            System.out.println(p + " " + pawnToPos.get(p));
        }
    }
}
