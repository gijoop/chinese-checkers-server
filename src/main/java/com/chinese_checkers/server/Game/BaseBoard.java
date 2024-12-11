package com.chinese_checkers.server.Game;

import java.util.HashMap;

public class BaseBoard implements Board {
    private HashMap<Integer, Pawn> pawns;

    public BaseBoard(int numPlayers) {

        pawns = new HashMap<>();
        pawns.put(0, new Pawn(0, 0, 0, Player.PawnColor.RED));
    }

    @Override
    public void movePiece(Pawn pawn, int s, int q, int r) {
        return;
    }

    @Override
    public Pawn getPawnAt(int s, int q, int r) {
        return pawns.get(0);
    }

    @Override
    public Pawn getPawnById(int id) {
        return pawns.get(0);
    }
}
