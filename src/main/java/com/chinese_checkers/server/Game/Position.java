package com.chinese_checkers.server.Game;

public class Position {
    private int q; // Axial column
    private int r; // Axial row

    public Position(int q, int r) {
        this.q = q;
        this.r = r;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return q == position.q && r == position.r;
    }

    @Override
    public int hashCode() {
        return 31 * q + r;
    }

    @Override
    public String toString() {
        return "(" + q + ", " + r + ")";
    }
}
