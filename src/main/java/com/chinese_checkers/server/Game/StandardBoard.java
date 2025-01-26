package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;

/**
 * The StandardBoard class implements the Board interface and provides the standard implementation
 * for interacting with the game board in Chinese Checkers.
 */
public class StandardBoard implements Board {
    private HashMap<Pawn, Position> pawnToPos;
    private HashMap<Position, Pawn> posToPawn;
    private Integer size;

    /**
     * Constructs a StandardBoard object with the specified size.
     *
     * @param size the size of the board
     */
    public StandardBoard(Integer size) {
        this.pawnToPos = new HashMap<>();
        this.posToPawn = new HashMap<>();
        this.size = size;
    }

    public void addPawn(Pawn pawn, Position position) {
        pawnToPos.put(pawn, position);
        posToPawn.put(position, pawn);
    }

    public void movePawn(Pawn pawn, Position pos) {
        Position start = pawnToPos.get(pawn);
        pawnToPos.put(pawn, pos);
        posToPawn.remove(start);
        posToPawn.put(pos, pawn);
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

    public ArrayList<Pawn> getPawns() {
        return new ArrayList<>(pawnToPos.keySet());
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