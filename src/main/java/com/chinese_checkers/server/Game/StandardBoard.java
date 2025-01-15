package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.HashMap;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;

public class StandardBoard implements Board {
    private HashMap<Pawn, Position> pawnToPos;
    private HashMap<Position, Pawn> posToPawn;
    private ArrayList<Position> validPositions;
    private Integer size;

    public StandardBoard(Integer size) {
        this.pawnToPos = new HashMap<>();
        this.posToPawn = new HashMap<>();

        this.size = size;
        this.validPositions = getValidPositions();
    }

    private ArrayList<Position> getValidPositions() {
        ArrayList<Position> validPositions = new ArrayList<>();
        Position offset = new Position(-2*(size-1), -(size-1));
        
        // Add big triangle
        for (int x = 0; x <= 2*(size+1); x++) {
            for (int y = 0; y <= x; y++) {
                validPositions.add(new Position(x, y).add(offset));
            }
        }

        // Add missing corners
        validPositions.addAll(getStartingPositions(Corner.UPPER_LEFT));
        validPositions.addAll(getStartingPositions(Corner.UPPER_RIGHT));
        validPositions.addAll(getStartingPositions(Corner.LOWER));

        return validPositions;
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
                baseTriangle = upsideDownTriangle;
                break;
            case LOWER_RIGHT:
                offsetPosition = new Position(1, -size+1);
                break;
            case LOWER:
                offsetPosition = new Position(-size+1, -size);
                baseTriangle = upsideDownTriangle;
                break;
            case LOWER_LEFT:
                offsetPosition = new Position(-size-triSize+1, -size+1);
                break;
            case UPPER_LEFT:
                offsetPosition = new Position(-size+1, triSize);
                baseTriangle = upsideDownTriangle;
                break;
            case NONE:
                return null;
        }

        for (int i = 0; i < baseTriangle.size(); i++) {
            startingPositions.set(i, baseTriangle.get(i).add(offsetPosition));
        }

        return startingPositions;
    }

    public boolean isOccupied(Position position) {
        return posToPawn.containsKey(position);
    }

    public boolean isValidPosition(Position position) {
        return validPositions.contains(position);
    }

    public ArrayList<Position> getValidMoves(Position position) {
        ArrayList<Position> validMoves = new ArrayList<>();
        Position[] directions = {new Position(1, 0), new Position(0, 1), new Position(-1, 0), new Position(0, -1), new Position(-1, -1), new Position(1, 1)};
        int x = position.getX();
        int y = position.getY();

        for (Position dir : directions) {
            Position move = new Position(x + dir.getX(), y + dir.getY());
            if (isValidPosition(move) && !isOccupied(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    public ArrayList<Position> getValidJumps(Position position) {
        ArrayList<Position> jumps = new ArrayList<>();
        return findJumps(position, jumps);
    }

    private ArrayList<Position> findJumps(Position position, ArrayList<Position> jumps) {
        int x = position.getX();
        int y = position.getY();
        Position[] directions = {new Position(1, 0), new Position(0, 1), new Position(-1, 0), new Position(0, -1), new Position(-1, -1), new Position(1, 1)};
        for (Position dir : directions) {
            if(isOccupied(position.add(dir))){
                Position jump = new Position(x + 2*dir.getX(), y + 2*dir.getY());
                if (isValidPosition(jump) && !isOccupied(jump) && !jumps.contains(jump)) {
                    jumps.add(jump);
                    //findJumps(jump, jumps);
                }
            }
        }
        return jumps;
    }
        
    public void printBoard() {
        for (Pawn p : pawnToPos.keySet()) {
            System.out.println(p + " " + pawnToPos.get(p));
        }
    }
}
