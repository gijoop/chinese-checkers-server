package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class Move {
    private Position start;
    private Position goal;
    private MoveResult result;

    public Move(Position start, Position goal) {
        this.start = start;
        this.goal = goal;
        result = MoveResult.SUCCESS;
    }

    public void setResult(MoveResult result) {
        if ((result == MoveResult.SUCCESS || result == MoveResult.SUCCESS_JUMP) 
            && this.result != MoveResult.SUCCESS) {
            return; // Can't change from a failed result to a successful one
        }
        this.result = result;
    }

    public Position getStart() {
        return start;
    }

    public Position getGoal() {
        return goal;
    }

    public MoveResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "start=" + start +
                ", goal=" + goal +
                '}';
    }
}
