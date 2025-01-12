package com.chinese_checkers.server.Game;

public class Player {
    public enum PawnColor {
        RED, BLUE, GREEN, YELLOW, BLACK, WHITE
    }

    private String name;
    private PawnColor color;
    private int id;

    public Player(String name, int id, PawnColor color) {
        this.name = name;
        this.id = id;
        this.color = color;
    }

    public Player(String name, int id) {
        this(name, id, null);
    }

    public Player(int id) {
        this(null, id, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(PawnColor color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public PawnColor getPawnColor() {
        return color;
    }
}
