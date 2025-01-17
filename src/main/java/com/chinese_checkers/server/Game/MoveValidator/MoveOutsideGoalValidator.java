package com.chinese_checkers.server.Game.MoveValidator;

import java.util.ArrayList;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class MoveOutsideGoalValidator extends MoveValidatorDecorator {
    private Corner corner;
    private Ruleset ruleset;

    public MoveOutsideGoalValidator(MoveValidator nextValidator, Corner corner, Ruleset ruleset) {
        super(nextValidator);
        this.corner = corner;
        this.ruleset = ruleset;
    }
    
    @Override
    public void validateMove(Move move) {
        validateOutsideGoal(move, corner);
        super.validateMove(move);
    }

    /**
     * Validates if a move is outside the goal area.
     * 
     * This method checks if the starting position of the move is within the goal positions
     * of the opposite corner. If the starting position is within the goal positions and the 
     * goal position is outside the goal positions, the move result is set to OUT_OF_GOAL.
     * 
     * @param move   The move to be validated.
     * @param corner The starting corner of pawn that is making the move.
     */
    private void validateOutsideGoal(Move move, Corner corner) {
        Position start = move.getStart();
        Position goal = move.getGoal();
        ArrayList<Position> goalPositions = ruleset.getStartingPositions(corner.getOpposite());
        if(goalPositions.contains(start)){
            if(!goalPositions.contains(goal)){
                move.setResult(MoveResult.OUT_OF_GOAL);
            }
        }
    }
}