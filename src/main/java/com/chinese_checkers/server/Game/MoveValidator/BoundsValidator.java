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

    private void validateBounds(Move move) {
        if(!ruleset.isInBounds(move.getGoal())){
            move.setResult(MoveResult.OUT_OF_BOUNDS);
        }
    }
}
