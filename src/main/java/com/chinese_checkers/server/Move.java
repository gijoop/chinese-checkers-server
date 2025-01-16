package com.chinese_checkers.server;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class Move {
    private Pawn pawn;
    private Position goal;
    private MoveResult result;

    public Move(Pawn pawn, Position goal) {
        this.pawn = pawn;
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

    public Pawn getPawn() {
        return pawn;
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
                "pawn=" + pawn +
                ", goal=" + goal +
                '}';
    }
}
