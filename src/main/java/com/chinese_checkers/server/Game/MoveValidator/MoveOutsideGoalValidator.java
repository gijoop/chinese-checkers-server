package com.chinese_checkers.server.Game.MoveValidator;

import java.util.ArrayList;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class MoveOutsideGoalValidator extends MoveValidatorDecorator {
    private Corner corner;
    private Ruleset ruleset;

    public MoveOutsideGoalValidator(MoveValidator nextValidator, Corner corner, Ruleset ruleset) {
        super(nextValidator);
        this.corner = corner;
        this.ruleset = ruleset;
    }
    
    @Override
    public void validateMove(Move move) {
        validateOutsideGoal(move, corner);
        super.validateMove(move);
    }

    private void validateOutsideGoal(Move move, Corner corner) {
        Position start = move.getStart();
        Position goal = move.getGoal();
        ArrayList<Position> goalPositions = ruleset.getStartingPositions(corner.getOpposite());
        if(goalPositions.contains(start)){
            if(!goalPositions.contains(goal)){
                move.setResult(MoveResult.OUT_OF_GOAL);
            }
        }
    }
}