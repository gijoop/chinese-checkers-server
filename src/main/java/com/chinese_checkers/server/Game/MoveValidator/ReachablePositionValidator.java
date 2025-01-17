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

    /**
     * Validates if the goal position of a move is reachable from the start position.
     * If the goal position is not reachable by a direct move or a jump, the move result is set to UNREACHABLE.
     * If the goal position is reachable by a jump, the move result is set to SUCCESS_JUMP.
     *
     * @param move the move to be validated, containing the start and goal positions
     */
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