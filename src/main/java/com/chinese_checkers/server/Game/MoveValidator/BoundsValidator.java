package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class BoundsValidator extends MoveValidatorDecorator {
    private Ruleset ruleset;

    public BoundsValidator(MoveValidator nextValidator, Ruleset ruleset) {
        super(nextValidator);
        this.ruleset = ruleset;
    }

    @Override
    public void validateMove(Move move) {
        validateBounds(move);
        super.validateMove(move);
    }

    /**
     * Validates if the goal position of the given move is within the allowed bounds.
     * If the goal position is out of bounds, sets the move result to OUT_OF_BOUNDS.
     *
     * @param move the move to be validated
     */
    private void validateBounds(Move move) {
        if(!ruleset.isInBounds(move.getGoal())){
            move.setResult(MoveResult.OUT_OF_BOUNDS);
        }
    }
}
