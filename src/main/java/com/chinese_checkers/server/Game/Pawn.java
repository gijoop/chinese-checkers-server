package com.chinese_checkers.server.Game;

public class Pawn {
    private int s;
    private int q;
    private int r;
    private Player.PawnColor color;

    public Pawn(int s, int q, int r, Player.PawnColor color) {
        this.s = s;
        this.q = q;
        this.r = r;
        this.color = color;
    }

    public int getS() {
        return s;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public void setS(int s) {
        this.s = s;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Player.PawnColor getColor() {
        return color;
    }
}
