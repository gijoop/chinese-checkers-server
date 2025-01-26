package com.chinese_checkers.server.Game.Ruleset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.StandardBoard;

public class StandardRulesetTest {

    @Test
    public void testGetStartingPositionsSize5() {
        StandardBoard board = new StandardBoard(5);
        CornerHelper cornerHelper = new CornerHelper(3, board.getSize());
        Ruleset ruleset = new StandardRuleset(board, cornerHelper);

        ArrayList<Position> startingPositions = ruleset.getStartingPositions(Corner.UPPER);
        assertEquals(10, startingPositions.size());
        assertTrue(startingPositions.containsAll(new ArrayList<Position>() {
            {
                add(new Position(1, 5));
                add(new Position(2, 5));
                add(new Position(3, 5));
                add(new Position(4, 5));
                add(new Position(2, 6));
                add(new Position(3, 6));
                add(new Position(4, 6));
                add(new Position(3, 7));
                add(new Position(4, 7));
                add(new Position(4, 8));
            }
        }));

        startingPositions = ruleset.getStartingPositions(Corner.LOWER_LEFT);
        assertEquals(10, startingPositions.size());
        assertTrue(startingPositions.containsAll(new ArrayList<Position>() {
            {
                add(new Position(-8, -4));
                add(new Position(-7, -4));
                add(new Position(-6, -4));
                add(new Position(-5, -4));
                add(new Position(-7, -3));
                add(new Position(-6, -3));
                add(new Position(-5, -3));
                add(new Position(-6, -2));
                add(new Position(-5, -2));
                add(new Position(-5, -1));
            }
        }));

        startingPositions = ruleset.getStartingPositions(Corner.UPPER_RIGHT);
        assertEquals(10, startingPositions.size());
        assertTrue(startingPositions.containsAll(new ArrayList<Position>() {
            {
                add(new Position(5, 4));
                add(new Position(6, 4));
                add(new Position(7, 4));
                add(new Position(8, 4));
                add(new Position(5, 3));
                add(new Position(6, 3));
                add(new Position(7, 3));
                add(new Position(5, 2));
                add(new Position(6, 2));
                add(new Position(5, 1));
            }
        }));

    }

    @Test
    public void testGetStartingPositionsSize4() {
        StandardBoard board = new StandardBoard(4);
        CornerHelper cornerHelper = new CornerHelper(3, board.getSize());
        Ruleset ruleset = new StandardRuleset(board, cornerHelper);

        ArrayList<Position> startingPositions = ruleset.getStartingPositions(Corner.UPPER);
        assertEquals(6, startingPositions.size());
        assertTrue(startingPositions.containsAll(new ArrayList<Position>() {
            {
                add(new Position(1, 4));
                add(new Position(2, 4));
                add(new Position(3, 4));
                add(new Position(2, 5));
                add(new Position(3, 5));
                add(new Position(3, 6));
            }
        }));

        startingPositions = ruleset.getStartingPositions(Corner.LOWER_LEFT);
        assertEquals(6, startingPositions.size());
        assertTrue(startingPositions.containsAll(new ArrayList<Position>() {
            {
                add(new Position(-6, -3));
                add(new Position(-5, -3));
                add(new Position(-4, -3));
                add(new Position(-5, -2));
                add(new Position(-4, -2));
                add(new Position(-4, -1));
            }
        }));

        startingPositions = ruleset.getStartingPositions(Corner.UPPER_RIGHT);
        assertEquals(6, startingPositions.size());
        assertTrue(startingPositions.containsAll(new ArrayList<Position>() {
            {
                add(new Position(4, 3));
                add(new Position(5, 3));
                add(new Position(6, 3));
                add(new Position(4, 2));
                add(new Position(5, 2));
                add(new Position(4, 1));
            }
        }));
    }


    @Test
    public void testIsInBounds() {
        StandardBoard board = new StandardBoard(5);
        CornerHelper cornerHelper = new CornerHelper(3, board.getSize());
        Ruleset ruleset = new StandardRuleset(board, cornerHelper);

        assertTrue(ruleset.isInBounds(new Position(0, 0)));
        assertTrue(ruleset.isInBounds(new Position(4, 4)));
        assertTrue(ruleset.isInBounds(new Position(-4, -4)));
        assertTrue(ruleset.isInBounds(new Position(4, -4)));
        assertTrue(ruleset.isInBounds(new Position(-4, 4)));

        assertFalse(ruleset.isInBounds(new Position(8, 0)));
        assertFalse(ruleset.isInBounds(new Position(-8, 0)));
        assertFalse(ruleset.isInBounds(new Position(0, -8)));
        assertFalse(ruleset.isInBounds(new Position(0, 8)));
        assertFalse(ruleset.isInBounds(new Position(8, 8)));
        assertFalse(ruleset.isInBounds(new Position(-8, -8)));
    }

    @Test
    public void testGetReachableMoves() {
        StandardBoard board = new StandardBoard(5);
        CornerHelper cornerHelper = new CornerHelper(3, board.getSize());
        Ruleset ruleset = new StandardRuleset(board, cornerHelper);
        //Player player = new Player("Player1", 1000);
        Pawn pawn = new Pawn(null);

        board.addPawn(pawn, new Position(0, 0));

        ArrayList<Position> reachableMoves = ruleset.getReachableMoves(new Position(0, 0));
        assertEquals(6, reachableMoves.size());
        assertTrue(reachableMoves.contains(new Position(1, 0)));
        assertTrue(reachableMoves.contains(new Position(0, 1)));
        assertTrue(reachableMoves.contains(new Position(-1, 0)));
        assertTrue(reachableMoves.contains(new Position(0, -1)));
        assertTrue(reachableMoves.contains(new Position(1, 1)));
        assertTrue(reachableMoves.contains(new Position(-1, -1)));
    }

    @Test
    public void testGetReachableJumps() {
        StandardBoard board = new StandardBoard(5);
        CornerHelper cornerHelper = new CornerHelper(3, board.getSize());
        Ruleset ruleset = new StandardRuleset(board, cornerHelper);
        Pawn pawnA = new Pawn(null);
        Pawn pawnB = new Pawn(null);
        Pawn pawnC = new Pawn(null);

        board.addPawn(pawnA, new Position(0, 1));
        board.addPawn(pawnB, new Position(1, 0));
        board.addPawn(pawnC, new Position(-1, 0));

        ArrayList<Position> reachableJumps = ruleset.getReachableJumps(new Position(0, 0));
        assertEquals(3, reachableJumps.size());
        assertTrue(reachableJumps.containsAll(new ArrayList<Position>() {
            {
                add(new Position(2, 0));
                add(new Position(0, 2));
                add(new Position(-2, 0));
            }
        }));
    }
}

