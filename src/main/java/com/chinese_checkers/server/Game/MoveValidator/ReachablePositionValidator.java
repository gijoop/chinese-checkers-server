package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class ReachablePositionValidator extends MoveValidatorDecorator {
    private Ruleset ruleset;

    public ReachablePositionValidator(MoveValidator nextValidator, Ruleset ruleset) {
        super(nextValidator);
        this.ruleset = ruleset;
    }
    
    @Override
    public void validateMove(Move move) {
        validateReachable(move);
        super.validateMove(move);
    }

    private void validateReachable(Move move) {
        Position start = move.getStart();
        Position goal = move.getGoal();
        if(!ruleset.getReachableMoves(start).contains(goal)){
            if(!ruleset.getReachableJumps(start).contains(goal)){
                move.setResult(MoveResult.UNREACHABLE);
            }else{
                move.setResult(MoveResult.SUCCESS_JUMP);
            }
        }
    }
}