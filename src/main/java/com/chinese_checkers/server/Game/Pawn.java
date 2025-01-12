package com.chinese_checkers.server.Game;

public class Pawn {
    private Player.PawnColor color;
    private Player owner;

    public Pawn(Player.PawnColor color, Player owner) {
        this.color = color;
        this.owner = owner;
    }

    public Player.PawnColor getColor() {
        return color;
    }

    public Player getOwner() {
        return owner;
    }
}
