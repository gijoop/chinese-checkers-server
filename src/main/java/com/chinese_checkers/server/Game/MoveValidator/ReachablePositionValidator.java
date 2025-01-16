package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Move;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class ReachablePositionValidator extends MoveValidatorDecorator {
    private Board board;
    private Ruleset ruleset;

    public ReachablePositionValidator(MoveValidator nextValidator, Board board, Ruleset ruleset) {
        super(nextValidator);
        this.board = board;
        this.ruleset = ruleset;
    }
    
    @Override
    public void validateMove(Move move) {
        validateReachable(move);
        super.validateMove(move);
    }

    private void validateReachable(Move move) {
        Pawn pawn = move.getPawn();
        Position goal = move.getGoal();
        if(!ruleset.getReachableMoves(board.getPositionOf(pawn)).contains(goal)){
            if(!ruleset.getReachableJumps(board.getPositionOf(pawn)).contains(goal)){
                move.setResult(MoveResult.UNREACHABLE);
            }else{
                move.setResult(MoveResult.SUCCESS_JUMP);
            }
        }
    }
}