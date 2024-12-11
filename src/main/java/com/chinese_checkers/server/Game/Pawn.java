package com.chinese_checkers.server.Game;

public class Pawn {
    private int x;
    private int y;
    private int z;
    private Player.PawnColor color;

    public Pawn(int x, int y, int z, Player.PawnColor color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Player.PawnColor getColor() {
        return color;
    }
}
