package com.chinese_checkers.server.Game.Ruleset;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Move;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.MoveValidator.BoundsValidator;
import com.chinese_checkers.server.Game.MoveValidator.MoveOutsideGoalValidator;
import com.chinese_checkers.server.Game.MoveValidator.MoveValidator;
import com.chinese_checkers.server.Game.MoveValidator.OccupiedValidator;
import com.chinese_checkers.server.Game.MoveValidator.ReachablePositionValidator;


public class StandardRuleset implements Ruleset {
    private static final Position[] DIRECTIONS = {
        new Position(1, 0), new Position(0, 1), new Position(-1, 0), 
        new Position(0, -1), new Position(-1, -1), new Position(1, 1)
    };

    private PlayerConfig playerConfig;
    private Board board;
    private Set<Position> validPositions;
    private int playerCount;

    public StandardRuleset(Board board, PlayerConfig playerConfig) {
        this.board = board;
        this.playerConfig = playerConfig;
        this.playerCount = playerConfig.getPlayerCount();
        this.validPositions = getInBoundPositions();
    }

    public MoveResult validateMove(Pawn pawn, Position position) {
        Move move = new Move(pawn, position);
        MoveValidator validator = 
            new BoundsValidator(
            new OccupiedValidator(
            new MoveOutsideGoalValidator(
            new ReachablePositionValidator(null, board, this), board, this), board), this);

        validator.validateMove(move);

        return move.getResult();
    }

    private Set<Position> getInBoundPositions() {
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
        if(PlayerConfig.isStartingCornerReverse(corner)){
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

    public ArrayList<Position> getReachableMoves(Position position) {
        ArrayList<Position> validMoves = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();

        for (Position dir : DIRECTIONS) {
            Position move = new Position(x + dir.getX(), y + dir.getY());
            validMoves.add(move);
        }
        return validMoves;
    }

    public ArrayList<Position> getReachableJumps(Position position) {
        ArrayList<Position> jumps = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();
        for (Position dir : DIRECTIONS) {
            if(board.isOccupied(position.add(dir))){
                Position jump = new Position(x + 2*dir.getX(), y + 2*dir.getY());
                jumps.add(jump);
            }
        }
        return jumps;
    }
    
}
