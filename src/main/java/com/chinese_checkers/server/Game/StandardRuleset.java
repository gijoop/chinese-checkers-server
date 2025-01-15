package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;

public class StandardRuleset implements Ruleset {

    public MoveResult isValidMove(Board board, Pawn pawn, Position position) {
        if(!board.isValidPosition(position)){
            return MoveResult.OUT_OF_BOUNDS;
        }
        ArrayList<Position> goalPositions = board.getStartingPositions(pawn.getOwner().getCorner().getOpposite());
        if(goalPositions.contains(board.getPositionOf(pawn))){
            if(!goalPositions.contains(position)){
                // Player is trying to move out of their goal
                return MoveResult.INVALID_MOVE;
            }
        }
        if(!board.getValidMoves(board.getPositionOf(pawn)).contains(position)){
            if(!board.getValidJumps(board.getPositionOf(pawn)).contains(position)){
                return MoveResult.INVALID_MOVE;
            }
            return MoveResult.SUCCESS_JUMP;
        }

        return MoveResult.SUCCESS;
    }
    
}
