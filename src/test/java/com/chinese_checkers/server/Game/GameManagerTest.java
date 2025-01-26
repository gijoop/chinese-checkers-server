package com.chinese_checkers.server.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.server.Game.Ruleset.CornerHelper;
import com.chinese_checkers.server.Game.Ruleset.StandardRuleset;

public class GameManagerTest {
    private GameManager gameManager;
    private CornerHelper cornerHelper;
    private StandardBoard board;
    private StandardRuleset ruleset;
    private Player playerA;
    private Player playerB;
    private Player playerC;

    @BeforeEach
    public void setUp() {
        board = new StandardBoard(5);
        cornerHelper = new CornerHelper(3, board.getSize());
        ruleset = new StandardRuleset(board, cornerHelper);
        gameManager = new GameManager(board, ruleset, 10);
        playerA = new Player("PlayerA", 1001);
        playerB = new Player("PlayerB", 1002);
        playerC = new Player("PlayerC", 1003);
    }

    @Test
    public void testInitializeGame() {
        GameStartMessage gameStartMessage = gameManager.initializeGame(new ArrayList<>(List.of(playerA, playerB, playerC)), null);
        assertEquals(5, gameStartMessage.getBoardSize());
        assertEquals(30, gameStartMessage.getPawns().size());
        assertEquals(10, gameStartMessage.getPawns().values().stream().filter(p -> p.getOwner().equals(playerA)).count());
        assertEquals(10, gameStartMessage.getPawns().values().stream().filter(p -> p.getOwner().equals(playerB)).count());
        assertEquals(10, gameStartMessage.getPawns().values().stream().filter(p -> p.getOwner().equals(playerC)).count());
    }

    @Test
    public void testCheckAndMove() {
        Player player = null;
        gameManager.initializeGame(new ArrayList<>(List.of(playerA, playerB, playerC)), null);
        switch(gameManager.getCurrentTurn()) {
            case Corner.UPPER_LEFT:
                player = playerA;
                break;
            case Corner.UPPER_RIGHT:
                player = playerB;
                break;
            case Corner.LOWER:
                player = playerC;
                break;
            default:
                assertTrue(false);
        }
        Pawn pawn = new Pawn(player);
        board.addPawn(pawn, new Position(0, 0));
        assertEquals(StandardRuleset.MoveResult.SUCCESS, gameManager.checkAndMove(pawn.getId(), new Position(1, 0), player));
        assertEquals(StandardRuleset.MoveResult.NOT_YOUR_TURN, gameManager.checkAndMove(pawn.getId(), new Position(0, 0), player));
    }

    @Test
    public void testCheckWin() {
        gameManager.initializeGame(new ArrayList<>(List.of(playerA, playerB, playerC)), null);
        Corner corner = playerA.getCorner();
        ArrayList<Position> winningPositions = ruleset.getStartingPositions(corner.getOpposite());
        ArrayList<Position> startingPositions = ruleset.getStartingPositions(corner);
        for(int i = 0; i < 10; i++) {
            board.movePawn(board.getPawnAt(startingPositions.get(i)), winningPositions.get(i));
        }
        assertTrue(gameManager.checkWin(playerA));
        board.movePawn(board.getPawnAt(winningPositions.get(0)), new Position(0, 0));
        assertFalse(gameManager.checkWin(playerA));
    }
}
