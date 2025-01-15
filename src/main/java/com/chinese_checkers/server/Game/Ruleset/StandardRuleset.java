package com.chinese_checkers.server.Game.Ruleset;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Board;


public class StandardRuleset implements Ruleset {
    private static final Position[] DIRECTIONS = {
        new Position(1, 0), new Position(0, 1), new Position(-1, 0), 
        new Position(0, -1), new Position(-1, -1), new Position(1, 1)
    };

    private PlayerConfig playerConfig;
    private Board board;
    private Set<Position> validPositions;
    private int playerCount;

    public StandardRuleset(int playerCount, Board board, PlayerConfig playerConfig) {
        this.playerCount = playerCount;
        this.board = board;
        this.playerConfig = playerConfig;
        this.validPositions = getValidPositions();
    }

    public MoveResult validateMove(Pawn pawn, Position position) {
        if(!isInBounds(position)){
            return MoveResult.OUT_OF_BOUNDS;
        }

        if(board.isOccupied(position)) {
            return MoveResult.OCCUPIED;
        }

        ArrayList<Position> goalPositions = getStartingPositions(pawn.getOwner().getCorner().getOpposite());
        if(goalPositions.contains(board.getPositionOf(pawn))){
            if(!goalPositions.contains(position)){
                // Player is trying to move out of their goal
                return MoveResult.INVALID_MOVE;
            }
        }
        if(!getValidMoves(board.getPositionOf(pawn)).contains(position)){
            if(!getValidJumps(board.getPositionOf(pawn)).contains(position)){
                return MoveResult.INVALID_MOVE;
            }
            return MoveResult.SUCCESS_JUMP;
        }

        return MoveResult.SUCCESS;
    }

    private Set<Position> getValidPositions() {
        Set<Position> validPositions = new HashSet<>();
        int size = board.getSize();
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

    public ArrayList<Position> getStartingPositions(Corner corner) {
        ArrayList<Position> baseTriangle = new ArrayList<>();
        ArrayList<Position> startingPositions = new ArrayList<>();

        int size = board.getSize();
        int triSize = size - 1;
        if(playerCount == 2 && (corner == Corner.UPPER || corner == Corner.LOWER)){
            triSize = size;
        }
        for (int i = 0; i < triSize; i++) {
            for (int j = 0; j <= i; j++) {
                baseTriangle.add(new Position(i, j));
            }
        }

        Position offsetPosition = playerConfig.getOffset(corner);
        if(playerConfig.isStartingCornerReverse(corner)){
            baseTriangle = invertTriangle(baseTriangle, triSize);
        }

        for (Position p : baseTriangle) {
            startingPositions.add(p.add(offsetPosition));
        }

        return startingPositions;
    }

    private ArrayList<Position> invertTriangle(ArrayList<Position> triangle, int triSize) {
        return triangle.stream()
            .map(p -> new Position(triSize - 1 - p.getX(), -p.getY()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean isInBounds(Position position) {
        return validPositions.contains(position);
    }

    public ArrayList<Position> getValidMoves(Position position) {
        ArrayList<Position> validMoves = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();

        for (Position dir : DIRECTIONS) {
            Position move = new Position(x + dir.getX(), y + dir.getY());
            if (isInBounds(move) && !board.isOccupied(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    public ArrayList<Position> getValidJumps(Position position) {
        ArrayList<Position> jumps = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();
        for (Position dir : DIRECTIONS) {
            if(board.isOccupied(position.add(dir))){
                Position jump = new Position(x + 2*dir.getX(), y + 2*dir.getY());
                if (isInBounds(jump) && !board.isOccupied(jump)) {
                    jumps.add(jump);
                }
            }
        }
        return jumps;
    }
    
}
