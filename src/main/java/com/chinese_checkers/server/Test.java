package com.chinese_checkers.server;

import com.chinese_checkers.server.Game.BaseBoard;
import com.chinese_checkers.server.Game.Board;

public class Test {
    public static void main(String[] args) {
        Board board = new BaseBoard(5);
        System.out.println(board.getStartingPositions(Board.Corner.UPPER));
    }
    
}
