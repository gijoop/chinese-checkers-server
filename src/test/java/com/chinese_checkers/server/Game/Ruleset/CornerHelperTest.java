package com.chinese_checkers.server.Game.Ruleset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.StandardBoard;

public class CornerHelperTest {

    private CornerHelper cornerHelperA;
    private CornerHelper cornerHelperB;
    private CornerHelper cornerHelperC;
    private CornerHelper cornerHelperD;
    private CornerHelper cornerHelperE;
    private CornerHelper cornerHelperF;

    @BeforeEach
    public void setUp() {
        Board board5 = new StandardBoard(5);
        Board board4 = new StandardBoard(4);
        cornerHelperA = new CornerHelper(2, board5.getSize());
        cornerHelperB = new CornerHelper(3, board5.getSize());
        cornerHelperC = new CornerHelper(4, board5.getSize());
        cornerHelperD = new CornerHelper(6, board5.getSize());
        cornerHelperE = new CornerHelper(2, board4.getSize());
        cornerHelperF = new CornerHelper(3, board4.getSize());
    }

    @Test
    public void testIsStartingCornerReverse() {
        assertTrue(CornerHelper.isStartingCornerReverse(Corner.LOWER));
        assertTrue(CornerHelper.isStartingCornerReverse(Corner.UPPER_LEFT));
        assertTrue(CornerHelper.isStartingCornerReverse(Corner.UPPER_RIGHT));
    }

    @Test
    public void testInitializeCorners() {
        List<Corner> corners2 = cornerHelperA.getStartingCorners();
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), corners2);

        List<Corner> corners3 = cornerHelperB.getStartingCorners();
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), corners3);

        List<Corner> corners4 = cornerHelperC.getStartingCorners();
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.LOWER_RIGHT, Corner.LOWER_LEFT, Corner.UPPER_RIGHT), corners4);

        List<Corner> corners6 = cornerHelperD.getStartingCorners();
        assertEquals(List.of(Corner.UPPER, Corner.UPPER_LEFT, Corner.LOWER_LEFT, Corner.LOWER, Corner.LOWER_RIGHT, Corner.UPPER_RIGHT), corners6);

        List<Corner> corners2_4 = cornerHelperE.getStartingCorners();
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), corners2_4);

        List<Corner> corners3_4 = cornerHelperF.getStartingCorners();
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), corners3_4);
    }

    @Test
    public void testGetPlayerCount() {
        assertEquals(2, cornerHelperA.getPlayerCount());
        assertEquals(3, cornerHelperB.getPlayerCount());
        assertEquals(4, cornerHelperC.getPlayerCount());
        assertEquals(6, cornerHelperD.getPlayerCount());
        assertEquals(2, cornerHelperE.getPlayerCount());
        assertEquals(3, cornerHelperF.getPlayerCount());
    }

    @Test
    public void testGetBoardSize() {
        assertEquals(5, cornerHelperA.getBoardSize());
        assertEquals(5, cornerHelperB.getBoardSize());
        assertEquals(5, cornerHelperC.getBoardSize());
        assertEquals(5, cornerHelperD.getBoardSize());
        assertEquals(4, cornerHelperE.getBoardSize());
        assertEquals(4, cornerHelperF.getBoardSize());
    }

    @Test
    public void testGetOffset() {
        assertEquals(new Position(0, 4), cornerHelperA.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 5), cornerHelperB.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 5), cornerHelperC.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 5), cornerHelperD.getOffset(Corner.UPPER));
        assertEquals(new Position(0, 3), cornerHelperE.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 4), cornerHelperF.getOffset(Corner.UPPER));

        assertEquals(new Position(-4, -4), cornerHelperA.getOffset(Corner.LOWER));
        assertEquals(new Position(-4, -5), cornerHelperB.getOffset(Corner.LOWER));
        assertEquals(new Position(-4, -5), cornerHelperC.getOffset(Corner.LOWER));
        assertEquals(new Position(-4, -5), cornerHelperD.getOffset(Corner.LOWER));
        assertEquals(new Position(-3, -3), cornerHelperE.getOffset(Corner.LOWER));
        assertEquals(new Position(-3, -4), cornerHelperF.getOffset(Corner.LOWER));

        assertEquals(new Position(-8, -4), cornerHelperB.getOffset(Corner.LOWER_LEFT));
        assertEquals(new Position(-8, -4), cornerHelperC.getOffset(Corner.LOWER_LEFT));
        assertEquals(new Position(-8, -4), cornerHelperD.getOffset(Corner.LOWER_LEFT));
        assertEquals(new Position(-6, -3), cornerHelperF.getOffset(Corner.LOWER_LEFT));
    }

    @Test
    public void testGetStartingCorners() {
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), cornerHelperA.getStartingCorners());
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), cornerHelperE.getStartingCorners());
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), cornerHelperB.getStartingCorners());
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), cornerHelperF.getStartingCorners());
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.LOWER_RIGHT, Corner.LOWER_LEFT, Corner.UPPER_RIGHT), cornerHelperC.getStartingCorners());
        assertEquals(List.of(Corner.UPPER, Corner.UPPER_LEFT, Corner.LOWER_LEFT, Corner.LOWER, Corner.LOWER_RIGHT, Corner.UPPER_RIGHT), cornerHelperD.getStartingCorners());
    }
}