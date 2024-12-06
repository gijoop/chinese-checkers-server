package com.chinese_checkers;

public class Player {
    public enum CheckerColor {
        RED, BLUE, GREEN, YELLOW, BLACK, WHITE
    }

    private String name;
    private CheckerColor color;
    private int id;

    public Player(String name, int id, CheckerColor color) {
        this.name = name;
        this.id = id;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public CheckerColor getColor() {
        return color;
    }
}
