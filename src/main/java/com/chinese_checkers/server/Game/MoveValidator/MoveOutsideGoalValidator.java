package com.chinese_checkers.server.Game.MoveValidator;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Move;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class MoveOutsideGoalValidator extends MoveValidatorDecorator {
    private Board board;
    private Ruleset ruleset;

    public MoveOutsideGoalValidator(MoveValidator nextValidator, Board board, Ruleset ruleset) {
        super(nextValidator);
        this.board = board;
        this.ruleset = ruleset;
    }
    
    @Override
    public void validateMove(Move move) {
        validateOutsideGoal(move);
        super.validateMove(move);
    }

    private void validateOutsideGoal(Move move) {
        Pawn pawn = move.getPawn();
        Position position = move.getGoal();
        ArrayList<Position> goalPositions = ruleset.getStartingPositions(pawn.getOwner().getCorner().getOpposite());
        if(goalPositions.contains(board.getPositionOf(pawn))){
            if(!goalPositions.contains(position)){
                move.setResult(MoveResult.OUT_OF_GOAL);
            }
        }
    }
}