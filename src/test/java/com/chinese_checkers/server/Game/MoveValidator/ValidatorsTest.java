package com.chinese_checkers.server.Game.MoveValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.StandardBoard;
import com.chinese_checkers.server.Game.Ruleset.PlayerConfig;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.StandardRuleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class ValidatorsTest {
    private MoveValidator validator;
    private StandardBoard board;
    private Ruleset ruleset;
    private PlayerConfig playerConfig;
    private Pawn pawnA;
    private Pawn pawnB;

    @BeforeEach
    public void setUp() {
        board = new StandardBoard(5);
        playerConfig = new PlayerConfig(2, board);
        ruleset = new StandardRuleset(board, playerConfig);

        pawnA = new Pawn(null);
        pawnB = new Pawn(null);
        board.addPawn(pawnA, new Position(1, 1));
        board.addPawn(pawnB, new Position(1, 3));
    }

    @Test
    public void testOccupiedValidator() {
        validator = new OccupiedValidator(null, board);

        Move move = new Move(new Position(0, 0), new Position(1, 1));
        validator.validateMove(move);
        assertEquals(MoveResult.OCCUPIED, move.getResult());

        move = new Move(new Position(0, 0), new Position(1, 3));
        validator.validateMove(move);
        assertEquals(MoveResult.OCCUPIED, move.getResult());

        move = new Move(new Position(0, 0), new Position(2, 2));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(10, 10));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());
    }

    @Test
    public void testReachablePositionValidator() {
        validator = new ReachablePositionValidator(null, ruleset);

        Move move = new Move(new Position(0, 0), new Position(1, 1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(-1, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(0, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(2, 2));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS_JUMP, move.getResult());

        move = new Move(new Position(0, 1), new Position(2, 1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS_JUMP, move.getResult());

        move = new Move(new Position(1, 2), new Position(1, 4));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS_JUMP, move.getResult());

        move = new Move(new Position(1, 2), new Position(1, 0));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS_JUMP, move.getResult());


        move = new Move(new Position(0, 0), new Position(1, 3));
        validator.validateMove(move);
        assertEquals(MoveResult.UNREACHABLE, move.getResult());

        move = new Move(new Position(0, 0), new Position(10, 10));
        validator.validateMove(move);
        assertEquals(MoveResult.UNREACHABLE, move.getResult());

        move = new Move(new Position(0, 0), new Position(5, 5));
        validator.validateMove(move);
        assertEquals(MoveResult.UNREACHABLE, move.getResult());

        move = new Move(new Position(0, 0), new Position(-5, -5));
        validator.validateMove(move);
        assertEquals(MoveResult.UNREACHABLE, move.getResult());
    }

    @Test
    public void testMoveOutsideGoalValidator() {
        validator = new MoveOutsideGoalValidator(validator, Corner.LOWER, ruleset);

        Move move = new Move(new Position(0, 0), new Position(1, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 3), new Position(1, 4));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(5, 4), new Position(4, 4));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(2, 4), new Position(3, 4));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(3, -3), new Position(0, 0));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(-3, -6), new Position(0, 0));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(2, 4), new Position(2, 3));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_GOAL, move.getResult());

        move = new Move(new Position(0, 4), new Position(-1, 4));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_GOAL, move.getResult());

        move = new Move(new Position(3, 6), new Position(0, 0));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_GOAL, move.getResult());
    }

    @Test
    public void testBoundsValidator() {
        validator = new BoundsValidator(validator, ruleset);
        Move move = new Move(new Position(0, 0), new Position(1, 1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(-1, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(0, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(new Position(0, 0), new Position(10, 10));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_BOUNDS, move.getResult());

        move = new Move(new Position(0, 0), new Position(5, 5));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_BOUNDS, move.getResult());

        move = new Move(new Position(0, 0), new Position(-5, 0));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_BOUNDS, move.getResult());

        move = new Move(new Position(0, 0), new Position(-8, 4));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_BOUNDS, move.getResult());

        move = new Move(new Position(0, 0), new Position(3, -5));
        validator.validateMove(move);
        assertEquals(MoveResult.OUT_OF_BOUNDS, move.getResult());
        
    }
}
