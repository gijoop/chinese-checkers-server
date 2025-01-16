package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Move;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class OccupiedValidator extends MoveValidatorDecorator {
    private Board board;

    public OccupiedValidator(MoveValidator nextValidator, Board board) {
        super(nextValidator);
        this.board = board;
    }
    
    @Override
    public void validateMove(Move move) {
        validateOccupied(move);
        super.validateMove(move);
    }

    private void validateOccupied(Move move) {
        if(board.isOccupied(move.getGoal())){
            move.setResult(MoveResult.OCCUPIED);
        }
    }
}
