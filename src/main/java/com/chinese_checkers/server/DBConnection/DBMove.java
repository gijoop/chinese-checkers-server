package com.chinese_checkers.server.DBConnection;

public class DBMove {
    private int gameId;
    private int moveNumber;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public DBMove() {
    }

    public DBMove(int gameId, int moveNumber, int fromX, int fromY, int toX, int toY) {
        this.gameId = gameId;
        this.moveNumber = moveNumber;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
    }
}

