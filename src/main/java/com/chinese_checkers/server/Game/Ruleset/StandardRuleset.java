package com.chinese_checkers.server.Game.Ruleset;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.MoveValidator.BoundsValidator;
import com.chinese_checkers.server.Game.MoveValidator.MoveOutsideGoalValidator;
import com.chinese_checkers.server.Game.MoveValidator.MoveValidator;
import com.chinese_checkers.server.Game.MoveValidator.OccupiedValidator;
import com.chinese_checkers.server.Game.MoveValidator.ReachablePositionValidator;

/**
 * The StandardRuleset class implements the Ruleset interface and provides the standard rules for a game of Chinese Checkers.
 * It includes methods for validating moves, checking if a position is in bounds, and retrieving starting positions and reachable moves or jumps.
 */
public class StandardRuleset implements Ruleset {
    protected static final Position[] DIRECTIONS = {
        new Position(1, 0), new Position(0, 1), new Position(-1, 0), 
        new Position(0, -1), new Position(-1, -1), new Position(1, 1)
    };

    protected CornerHelper cornerHelper;
    protected Board board;
    protected Set<Position> validPositions;
    protected int playerCount;

    /**
     * Constructs a StandardRuleset object with the specified board and corner helper.
     *
     * @param board the game board
     * @param cornerHelper the corner helper for managing corner-related configurations
     */
    public StandardRuleset(Board board, CornerHelper cornerHelper) {
        this.board = board;
        this.cornerHelper = cornerHelper;
        this.playerCount = cornerHelper.getPlayerCount();
        this.validPositions = getInBoundPositions();
    }

    public MoveResult validateMove(Pawn pawn, Position position) {
        Move move = new Move(board.getPositionOf(pawn), position);
        Corner playerCorner = pawn.getOwner().getCorner();
        MoveValidator validator = 
            new BoundsValidator(
            new OccupiedValidator(
            new MoveOutsideGoalValidator(
            new ReachablePositionValidator(null, this), playerCorner, this), board), this);

        validator.validateMove(move);

        return move.getResult();
    }

    /**
     * Retrieves the set of positions that are within the bounds of the game board.
     *
     * @return a set of valid positions
     */
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

        Position offsetPosition = cornerHelper.getOffset(corner);
        if(CornerHelper.isStartingCornerReverse(corner)){
            baseTriangle = invertTriangle(baseTriangle, triSize);
        }

        for (Position p : baseTriangle) {
            startingPositions.add(p.add(offsetPosition));
        }

        return startingPositions;
    }

    /**
     * Inverts a triangle of positions.
     *
     * @param triangle the triangle to invert
     * @param triSize the size of the triangle
     * @return the inverted triangle
     */
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

    public String getName() {
        return "Standard";
    }
}
