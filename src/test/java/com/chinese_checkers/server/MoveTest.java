package com.chinese_checkers.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;

public class MoveTest {
    private Pawn pawn;
    private Position goal;
    private Move move;

    @BeforeEach
    public void setUp() {
        pawn = new Pawn(null);
        goal = new Position(1, 1);
        move = new Move(pawn, goal);
    }

    @Test
    public void testInitialMoveResult() {
        assertEquals(MoveResult.SUCCESS, move.getResult());
    }

    @Test
    public void testSetResult() {
        move.setResult(MoveResult.OCCUPIED);
        assertEquals(MoveResult.OCCUPIED, move.getResult());
    }

    @Test
    public void testSetResultCannotChangeToSuccess() {
        move.setResult(MoveResult.OCCUPIED);
        move.setResult(MoveResult.SUCCESS);
        assertNotEquals(MoveResult.SUCCESS, move.getResult());
    }
}
