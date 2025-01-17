package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Game.Move;

public abstract class MoveValidatorDecorator implements MoveValidator {
    protected MoveValidator nextValidator;

    public MoveValidatorDecorator(MoveValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    /**
     * Validates the given move. If there is a next validator in the chain,
     * it delegates the validation to the next validator.
     *
     * @param move the move to be validated
     */
    public void validateMove(Move move) {
        if (nextValidator == null) {
            return;
        }
        nextValidator.validateMove(move);
    }
}