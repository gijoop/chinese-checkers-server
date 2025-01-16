package com.chinese_checkers.server;

import java.util.List;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.StandardBoard;
import com.chinese_checkers.server.Game.Ruleset.PlayerConfig;
import com.chinese_checkers.server.Game.Ruleset.StandardRuleset;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.GameManager;

public class Test {
    public static void main(String[] args) {
        Board board = new StandardBoard(5);
        PlayerConfig config = new PlayerConfig(2, board);
        StandardRuleset ruleset = new StandardRuleset(board, config);
        Player p1 = new Player("playerA", 1000);
        Player p2 = new Player("playerB", 1001);

        GameManager gm = new GameManager(board, ruleset, 15);

        gm.initializeGame(List.of(p1, p2));
        board.printBoard();

        // System.out.println("-------------------");
        
        Pawn pawnUpA = board.getPawnAt(new Position(2, 5));
        Pawn pawnUpB = board.getPawnAt(new Position(1, 4));
        Pawn pawnDownA = board.getPawnAt(new Position(-2, -5));
        Pawn pawnDownB = board.getPawnAt(new Position(0, -4));

        System.out.println(pawnUpA + " : " + board.getPositionOf(pawnUpA));
        System.out.println(pawnUpB + " : " + board.getPositionOf(pawnUpB));
        System.out.println(pawnDownA + " : " + board.getPositionOf(pawnDownA));
        System.out.println(pawnDownB + " : " + board.getPositionOf(pawnDownB));

        gm.checkAndMove(pawnUpA.getId(), new Position(0, 3), p1);
        gm.endTurn(p1);
        gm.checkAndMove(pawnDownA.getId(), new Position(-2, -3), p2);
        gm.endTurn(p2);
        gm.checkAndMove(pawnUpB.getId(), new Position(9, 3), p1);
    }
}
