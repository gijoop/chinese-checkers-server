package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Move;

public abstract class MoveValidatorDecorator implements MoveValidator {
    protected MoveValidator nextValidator;

    public MoveValidatorDecorator(MoveValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public void validateMove(Move move) {
        if(nextValidator == null) {
            return;
        }
        nextValidator.validateMove(move);
    }
}