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

    @Override
    public int distance(Position a, Position b)
    {
        // offset everything by a
        // a is now (0, 0)
        Position newB = new Position(b.getX() - a.getX(), b.getY() - a.getY());

        int sgnX = Integer.compare(newB.getX(), 0);
        int sgnY = Integer.compare(newB.getY(), 0);

        // treat 0 as don't care

        if (sgnX + sgnY == 0) {
            // different signs
            return Math.abs(newB.getX()) + Math.abs(newB.getY());
        } else {
            // same sign
            return Math.max(Math.abs(newB.getX()), Math.abs(newB.getY()));
        }
    }

    @Override
    public int distance(Position a, Corner corner)
    {
        int p = size - 1; // used to calculate distance to corners

		return switch (corner)
		{
			case Corner.UPPER ->        distance(a, new Position(p,   2 * p));
			case Corner.UPPER_RIGHT ->  distance(a, new Position(2 * p,   p));
			case Corner.LOWER_RIGHT ->  distance(a, new Position(p,         -p));
			case Corner.LOWER ->        distance(a, new Position(-p, -2 * p));
			case Corner.LOWER_LEFT ->   distance(a, new Position(-2 * p, -p));
			case Corner.UPPER_LEFT ->   distance(a, new Position(-p,         p));
			default -> -1;
		};
    }
}