package com.chinese_checkers.server.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;

public class StandardBoardTest {
    private StandardBoard board;

    @BeforeEach
    public void setUp() {
        board = new StandardBoard(20);
    }

    @Test
    public void testAddPawn() {
        Position pos = new Position(1, 1);
        Pawn pawn = new Pawn(null);
        board.addPawn(pawn, pos);
        assertEquals(pawn, board.getPawnAt(pos));
    }

    @Test
    public void testMovePawn() {
        Pawn pawn = new Pawn(null);
        Position oldPosition = new Position(1, 1);
        Position newPosition = new Position(1, 2);
        board.addPawn(pawn, oldPosition);
        board.movePawn(pawn, newPosition);
        assertEquals(pawn, board.getPawnAt(newPosition));
        assertNull(board.getPawnAt(oldPosition));
    }

    @Test
    public void testGetPawnAt() {
        Pawn pawn = new Pawn(null);
        Position position = new Position(1, 1);
        board.addPawn(pawn, position);
        assertEquals(pawn, board.getPawnAt(position));
    }

    @Test
    public void testGetPawnById() {
        Pawn pawn = new Pawn(null);
        Position position = new Position(1, 1);
        board.addPawn(pawn, position);
        assertEquals(pawn, board.getPawnById(pawn.getId()));
    }

    @Test
    public void testGetPositionOf() {
        Pawn pawn = new Pawn(null);
        Position position = new Position(1, 1);
        board.addPawn(pawn, position);
        assertEquals(position, board.getPositionOf(pawn));
    }

    @Test
    public void testGetSize() {
        Board testBoardC = new StandardBoard(3);
        Board testBoardA = new StandardBoard(5);
        Board testBoardB = new StandardBoard(7);
        assertEquals(3, testBoardC.getSize());
        assertEquals(5, testBoardA.getSize());
        assertEquals(7, testBoardB.getSize());
    }

    @Test
    public void testGetPlayerPawns() {
        Player player = new Player("player", 1001);
        Player player2 = new Player("player2", 1002);
        player.setCorner(Player.Corner.LOWER_LEFT);
        player2.setCorner(Player.Corner.LOWER_RIGHT);
        Pawn pawnA = new Pawn(player);
        Pawn pawnB = new Pawn(player);
        Pawn pawnC = new Pawn(player2);
        board.addPawn(pawnA, new Position(1, 1));
        board.addPawn(pawnB, new Position(1, 2));
        board.addPawn(pawnC, new Position(1, 3));
        assertEquals(2, board.getPlayerPawns(Player.Corner.LOWER_LEFT).size());
        assertTrue(board.getPlayerPawns(Player.Corner.LOWER_LEFT).containsAll(new ArrayList<Pawn>() {{
            add(pawnA);
            add(pawnB);
        }}));
    }

    @Test
    public void testIsOccupied() {
        Position position = new Position(1, 1);
        Pawn pawn = new Pawn(null);
        board.addPawn(pawn, position);
        assertTrue(board.isOccupied(position));
        assertFalse(board.isOccupied(new Position(1, 2)));
    }
}
