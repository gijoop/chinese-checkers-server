package com.chinese_checkers.server;

import java.util.List;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.StandardBoard;
import com.chinese_checkers.server.Game.StandardRuleset;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.GameManager;

public class Test {
    public static void main(String[] args) {
        Board board = new StandardBoard(5);
        StandardRuleset moveValidator = new StandardRuleset();
        Player p1 = new Player("playerA", 1000);
        Player p2 = new Player("playerB", 1001);

        GameManager gm = new GameManager(board, moveValidator, 10);

        gm.initializeGame(List.of(p1, p2));
        board.printBoard();

        System.out.println("-------------------");
        
        Pawn pawnA = board.getPawnAt(new Position(2, 5));
        Pawn pawnB = board.getPawnAt(new Position(-3, -6));
        System.out.println(pawnA + "pos: " + board.getPositionOf(pawnA));
        System.out.println(pawnB + "pos: " + board.getPositionOf(pawnB));
        System.out.println("pawnA valid moves: " + board.getValidMoves(board.getPositionOf(pawnA)));
        System.out.println("pawnB valid moves: " + board.getValidMoves(board.getPositionOf(pawnB)));
        gm.checkAndMove(pawnA.getId(), new Position(2, 4), p1);
        System.out.println(pawnA + "pos: " + board.getPositionOf(pawnA));
        System.out.println("pawnA valid moves: " + board.getValidMoves(board.getPositionOf(pawnA)));
        gm.checkAndMove(pawnB.getId(), new Position(-1, -4), p2);
        System.out.println(pawnB + "pos: " + board.getPositionOf(pawnB));
        gm.endTurn(p2);
        gm.endTurn(p1);
    }
}
