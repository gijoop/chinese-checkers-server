package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Move;

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

    public void movePawn(Move move) {
        Position start = pawnToPos.get(move.getPawn());
        pawnToPos.put(move.getPawn(), move.getGoal());
        posToPawn.remove(start);
        posToPawn.put(move.getGoal(), move.getPawn());
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
    System.out.println("Pawn Positions:");
        for (Map.Entry<Pawn, Position> entry : pawnToPos.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
    
}
