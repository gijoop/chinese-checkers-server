package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;

public interface Ruleset {
    enum MoveResult {
        SUCCESS,
        SUCCESS_JUMP,
        INVALID_MOVE,
        OCCUPIED,
        OUT_OF_BOUNDS,
        NONEXISTENT_PAWN,
        NOT_YOUR_TURN,
        GAME_OVER
    }
    public MoveResult isValidMove(Board board, Pawn pawn, Position position);
}
