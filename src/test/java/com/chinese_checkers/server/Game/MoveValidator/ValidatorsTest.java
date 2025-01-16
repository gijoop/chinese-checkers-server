package com.chinese_checkers.server.Game.MoveValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Move;
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
    private Pawn testPawn;

    @BeforeEach
    public void setUp() {
        board = new StandardBoard(5);
        playerConfig = new PlayerConfig(2, board);
        ruleset = new StandardRuleset(board, playerConfig);

        testPawn = new Pawn(null);
        pawnA = new Pawn(null);
        pawnB = new Pawn(null);
        board.addPawn(pawnA, new Position(1, 1));
        board.addPawn(pawnB, new Position(1, 3));
        board.addPawn(testPawn, new Position(0, 0));
    }

    @Test
    public void testOccupiedValidator() {
        validator = new OccupiedValidator(null, board);

        Move move = new Move(testPawn, new Position(1, 1));
        validator.validateMove(move);
        assertEquals(MoveResult.OCCUPIED, move.getResult());

        move = new Move(testPawn, new Position(1, 3));
        validator.validateMove(move);
        assertEquals(MoveResult.OCCUPIED, move.getResult());

        move = new Move(pawnA, new Position(2, 2));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(pawnA, new Position(10, 10));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());
    }

    @Test
    public void testReachablePositionValidator() {
        validator = new ReachablePositionValidator(null, board, ruleset);

        Move move = new Move(testPawn, new Position(1, 1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(testPawn, new Position(-1, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(testPawn, new Position(0, -1));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS, move.getResult());

        move = new Move(testPawn, new Position(1, 3));
        validator.validateMove(move);
        assertEquals(MoveResult.UNREACHABLE, move.getResult());

        move = new Move(testPawn, new Position(2, 2));
        validator.validateMove(move);
        assertEquals(MoveResult.SUCCESS_JUMP, move.getResult());

        move = new Move(testPawn, new Position(10, 10));
        validator.validateMove(move);
        assertEquals(MoveResult.UNREACHABLE, move.getResult());
    }
}
