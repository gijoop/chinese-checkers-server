package com.chinese_checkers.server.Game.Ruleset;

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
        INVALID_PAWN,
        NOT_YOUR_TURN,
        GAME_OVER
    }
    public MoveResult validateMove(Pawn pawn, Position position);
    public boolean isInBounds(Position position);
    public ArrayList<Position> getStartingPositions(Corner corner);
    public ArrayList<Position> getValidMoves(Position position);
    public ArrayList<Position> getValidJumps(Position position);
}
