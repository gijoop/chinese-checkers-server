package com.chinese_checkers.server;

import java.util.List;

import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.BaseBoard;
import com.chinese_checkers.server.Game.BaseMoveValidator;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.GameManager;
import com.chinese_checkers.server.Game.MoveValidator.MoveResult;

public class Test {
    public static void main(String[] args) {
        Board board = new BaseBoard(5);
        BaseMoveValidator moveValidator = new BaseMoveValidator();
        Player p1 = new Player("player1", 1000, Player.PawnColor.RED);
        Player p2 = new Player("player2", 1001, Player.PawnColor.BLUE);
        GameManager gm = new GameManager(board, moveValidator, 10);
        gm.initializeGame(List.of(p1, p2));
        board.printBoard();
        System.out.println("-------------------");
        MoveResult res = gm.checkAndMove(100, new Position(4, 5), p1);
        System.out.println(res);
        board.printBoard();
    }
}
