package com.chinese_checkers.server.Game.Ruleset;

import com.chinese_checkers.comms.Position;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player.Corner;

public interface Ruleset {
    public enum MoveResult {
        SUCCESS,
        SUCCESS_JUMP,
        INVALID_MOVE,
        OCCUPIED,
        OUT_OF_BOUNDS,
        INVALID_PAWN,
        NOT_YOUR_TURN,
        GAME_OVER,
        UNREACHABLE,
        OUT_OF_GOAL
    }
    public MoveResult validateMove(Pawn pawn, Position position);
    public boolean isInBounds(Position position);
    public ArrayList<Position> getStartingPositions(Corner corner);
    public ArrayList<Position> getReachableMoves(Position position);
    public ArrayList<Position> getReachableJumps(Position position);
}
