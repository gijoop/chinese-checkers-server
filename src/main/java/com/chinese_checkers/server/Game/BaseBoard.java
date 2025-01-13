package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;

public class BaseBoard implements Board {
    private HashMap<Pawn, Position> pawnToPos;
    private HashMap<Position, Pawn> posToPawn;
    private Integer size;

    public BaseBoard(Integer size) {
        pawnToPos = new HashMap<>();
        posToPawn = new HashMap<>();

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

    public ArrayList<Position> getStartingPositions(Corner corner) {
        ArrayList<Position> baseTriangle = new ArrayList<>();
        ArrayList<Position> upsideDownTriangle = new ArrayList<>();
        ArrayList<Position> startingPositions = new ArrayList<>();
        int triSize = size - 1; // f.e. if we have board with radius 5, each corner triangle has side length 4
        for (int i = 0; i < triSize; i++) {
            for (int j = 0; j <= i; j++) {
                baseTriangle.add(new Position(i, j));
                upsideDownTriangle.add(new Position(triSize - 1 - i, -j));
                startingPositions.add(new Position());
            }
        }

        Position offsetPosition = new Position();

        switch (corner) {
            case UPPER:
                // Lower left corner of the triangle
                offsetPosition = new Position(1, size);
                break;
            case UPPER_RIGHT:
                // Upper right corner of the triangle
                offsetPosition = new Position(size, size-1);
                break;
            case LOWER_RIGHT:
                offsetPosition = new Position(1, -size+1);
                break;
            case LOWER:
                offsetPosition = new Position(-size+1, -size);
                break;
            case LOWER_LEFT:
                offsetPosition = new Position(-size-triSize+1, -size+1);
                break;
            case UPPER_LEFT:
                offsetPosition = new Position(-size+1, triSize);
                break;
        }

        for (int i = 0; i < baseTriangle.size(); i++) {
            startingPositions.set(i, baseTriangle.get(i).add(offsetPosition));
        }

        return startingPositions;
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
