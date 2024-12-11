package com.chinese_checkers.server.Game;

import java.util.HashMap;

public class BaseBoard implements Board {
    private HashMap<Integer, Pawn> pawns;

    public BaseBoard() {
        pawns = new HashMap<>();
    }

    @Override
    public void setPawns(Pawn[] pawnsArray) {
        pawns.clear();
        for (int i = 0; i < pawnsArray.length; i++) {
            if (pawnsArray[i] != null) {
                pawns.put(i, pawnsArray[i]);
            }
        }
    }

    @Override
    public Pawn[] getPawns() {
        return pawns.values().toArray(new Pawn[0]);
    }

    @Override
    public Pawn[] getPawns(Player.PawnColor color) {
        return pawns.values().stream()
                .filter(pawn -> pawn.getColor() == color)
                .toArray(Pawn[]::new);
    }

    @Override
    public void movePiece(Pawn pawn, int s, int q, int r) {
        int key = getKey(pawn.getS(), pawn.getQ(), pawn.getR());
        pawns.remove(key);
        pawn.setS(s);
        pawn.setQ(q);
        pawn.setR(r);
        key = getKey(s, q, r);
        pawns.put(key, pawn);
    }

    @Override
    public Pawn getPawnAt(int s, int q, int r) {
        int key = getKey(s, q, r);
        return pawns.get(key);
    }

    @Override
    public Pawn getPawnById(int id) {
        return pawns.get(id);
    }

    private int getKey(int s, int q, int r) {
        return s * 10000 + q * 100 + r;
    }
}
