package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;

public class StandardBoard implements Board {
    private HashMap<Pawn, Position> pawnToPos;
    private HashMap<Position, Pawn> posToPawn;
    private Integer size;

    public StandardBoard(Integer size) {
        this.pawnToPos = new HashMap<>();
        this.posToPawn = new HashMap<>();
        this.size = size;
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

    public ArrayList<Pawn> getPlayerPawns(Corner corner) {
        ArrayList<Pawn> pawns = new ArrayList<>();
        for (Pawn p : pawnToPos.keySet()) {
            if (p.getOwner().getCorner() == corner) {
                pawns.add(p);
            }
        }
        return pawns;
    }

    public Pawn getPawnById(int id) {
        for (Pawn p : pawnToPos.keySet()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Position getPositionOf(Pawn pawn) {
        return pawnToPos.get(pawn);
    }

    public Integer getSize() {
        return size;
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
