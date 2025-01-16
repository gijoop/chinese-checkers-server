package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Game.Move;

public interface MoveValidator {
    void validateMove(Move move);
}