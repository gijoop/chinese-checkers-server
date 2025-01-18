package com.chinese_checkers.server.Game.Ruleset;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;

import com.chinese_checkers.server.Game.Board;

/**
 * The StandardRuleset class implements the Ruleset interface and provides the standard rules for a game of Chinese Checkers.
 * It includes methods for validating moves, checking if a position is in bounds, and retrieving starting positions and reachable moves or jumps.
 */
public class FastPacedRuleset extends StandardRuleset {
    /**
     * Constructs a FastPacedRuleset object with the specified board and corner helper.
     *
     * @param board the game board
     * @param cornerHelper the corner helper for managing corner-related configurations
     */
    public FastPacedRuleset(Board board, CornerHelper cornerHelper) {
        super(board, cornerHelper);
    }

    @Override
    public ArrayList<Position> getReachableJumps(Position position) {
        ArrayList<Position> jumps = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();
        int maxDistance = board.getSize() + 1;
        for (Position dir : DIRECTIONS) {
            Position firstPawn = null;
            int pawnDist = 1;
            for(int dist = 1; dist <= maxDistance; dist++){
                Position potentialPawn = new Position(x + dist * dir.getX(), y + dist * dir.getY());
                if(board.isOccupied(potentialPawn)){
                    firstPawn = potentialPawn;
                    pawnDist = dist;
                    break;
                }
            }
            if(firstPawn == null){
                continue;
            }
            boolean isJumpable = true;
            for(int behind = 1; behind <= pawnDist; behind++){
                Position potentialPawn = new Position(firstPawn.getX() + behind * dir.getX(), firstPawn.getY() + behind * dir.getY());
                if(board.isOccupied(potentialPawn)){
                    isJumpable = false;
                    break;
                }
            }
            if(isJumpable){
                jumps.add(new Position(x + 2 * pawnDist * dir.getX(), y + 2 * pawnDist * dir.getY()));
            }
        }
        return jumps;
    }

    @Override
    public String getName() {
        return "Fast Paced";
    }
}
