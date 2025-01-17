package com.chinese_checkers.server.Game.MoveValidator;

import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Move;
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

    /**
     * Validates if the goal position of the given move is occupied.
     * If the goal position is occupied, sets the move result to OCCUPIED.
     *
     * @param move the move to be validated
     */
    private void validateOccupied(Move move) {
        if(board.isOccupied(move.getGoal())){
            move.setResult(MoveResult.OCCUPIED);
        }
    }
}
