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

public class PlayerConfigTest {

    private PlayerConfig playerConfigA;
    private PlayerConfig playerConfigB;
    private PlayerConfig playerConfigC;
    private PlayerConfig playerConfigD;
    private PlayerConfig playerConfigE;
    private PlayerConfig playerConfigF;

    @BeforeEach
    public void setUp() {
        Board board5 = new StandardBoard(5);
        Board board4 = new StandardBoard(4);
        playerConfigA = new PlayerConfig(2, board5);
        playerConfigB = new PlayerConfig(3, board5);
        playerConfigC = new PlayerConfig(4, board5);
        playerConfigD = new PlayerConfig(6, board5);
        playerConfigE = new PlayerConfig(2, board4);
        playerConfigF = new PlayerConfig(3, board4);
    }

    @Test
    public void testIsStartingCornerReverse() {
        assertTrue(PlayerConfig.isStartingCornerReverse(Corner.LOWER));
        assertTrue(PlayerConfig.isStartingCornerReverse(Corner.UPPER_LEFT));
        assertTrue(PlayerConfig.isStartingCornerReverse(Corner.UPPER_RIGHT));
    }

    @Test
    public void testInitializeCorners() {
        List<Corner> corners2 = playerConfigA.getStartingCorners();
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), corners2);

        List<Corner> corners3 = playerConfigB.getStartingCorners();
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), corners3);

        List<Corner> corners4 = playerConfigC.getStartingCorners();
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.LOWER_RIGHT, Corner.LOWER_LEFT, Corner.UPPER_RIGHT), corners4);

        List<Corner> corners6 = playerConfigD.getStartingCorners();
        assertEquals(List.of(Corner.UPPER, Corner.UPPER_LEFT, Corner.LOWER_LEFT, Corner.LOWER, Corner.LOWER_RIGHT, Corner.UPPER_RIGHT), corners6);

        List<Corner> corners2_4 = playerConfigE.getStartingCorners();
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), corners2_4);

        List<Corner> corners3_4 = playerConfigF.getStartingCorners();
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), corners3_4);
    }

    @Test
    public void testGetPlayerCount() {
        assertEquals(2, playerConfigA.getPlayerCount());
        assertEquals(3, playerConfigB.getPlayerCount());
        assertEquals(4, playerConfigC.getPlayerCount());
        assertEquals(6, playerConfigD.getPlayerCount());
        assertEquals(2, playerConfigE.getPlayerCount());
        assertEquals(3, playerConfigF.getPlayerCount());
    }

    @Test
    public void testGetBoardSize() {
        assertEquals(5, playerConfigA.getBoardSize());
        assertEquals(5, playerConfigB.getBoardSize());
        assertEquals(5, playerConfigC.getBoardSize());
        assertEquals(5, playerConfigD.getBoardSize());
        assertEquals(4, playerConfigE.getBoardSize());
        assertEquals(4, playerConfigF.getBoardSize());
    }

    @Test
    public void testGetOffset() {
        assertEquals(new Position(0, 4), playerConfigA.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 5), playerConfigB.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 5), playerConfigC.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 5), playerConfigD.getOffset(Corner.UPPER));
        assertEquals(new Position(0, 3), playerConfigE.getOffset(Corner.UPPER));
        assertEquals(new Position(1, 4), playerConfigF.getOffset(Corner.UPPER));

        assertEquals(new Position(-4, -4), playerConfigA.getOffset(Corner.LOWER));
        assertEquals(new Position(-4, -5), playerConfigB.getOffset(Corner.LOWER));
        assertEquals(new Position(-4, -5), playerConfigC.getOffset(Corner.LOWER));
        assertEquals(new Position(-4, -5), playerConfigD.getOffset(Corner.LOWER));
        assertEquals(new Position(-3, -3), playerConfigE.getOffset(Corner.LOWER));
        assertEquals(new Position(-3, -4), playerConfigF.getOffset(Corner.LOWER));

        assertEquals(new Position(-8, -4), playerConfigB.getOffset(Corner.LOWER_LEFT));
        assertEquals(new Position(-8, -4), playerConfigC.getOffset(Corner.LOWER_LEFT));
        assertEquals(new Position(-8, -4), playerConfigD.getOffset(Corner.LOWER_LEFT));
        assertEquals(new Position(-6, -3), playerConfigF.getOffset(Corner.LOWER_LEFT));
    }

    @Test
    public void testGetStartingCorners() {
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), playerConfigA.getStartingCorners());
        assertEquals(List.of(Corner.UPPER, Corner.LOWER), playerConfigE.getStartingCorners());
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), playerConfigB.getStartingCorners());
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER), playerConfigF.getStartingCorners());
        assertEquals(List.of(Corner.UPPER_LEFT, Corner.LOWER_RIGHT, Corner.LOWER_LEFT, Corner.UPPER_RIGHT), playerConfigC.getStartingCorners());
        assertEquals(List.of(Corner.UPPER, Corner.UPPER_LEFT, Corner.LOWER_LEFT, Corner.LOWER, Corner.LOWER_RIGHT, Corner.UPPER_RIGHT), playerConfigD.getStartingCorners());
    }
}