package com.chinese_checkers.server.Game.Ruleset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;

public class PlayerConfig {
    private final int playerCount;
    private final int boardSize;
    private final Map<Corner, Position> cornerOffsets;
    private final List<Corner> startingCorners;

    public PlayerConfig(int playerCount, int boardSize) {
        this.playerCount = playerCount;
        this.boardSize = boardSize;
        this.cornerOffsets = initializeOffsets();
        this.startingCorners = initializeCorners();
    }

    public boolean isStartingCornerReverse(Corner corner) {
        return corner == Corner.LOWER || corner == Corner.UPPER_LEFT || corner == Corner.UPPER_RIGHT;
    }

    private Map<Corner, Position> initializeOffsets() {
        if (playerCount == 2) {
            return Map.of(
                Corner.UPPER, new Position(0, boardSize - 1),
                Corner.UPPER_RIGHT, new Position(boardSize, boardSize - 1),
                Corner.LOWER_RIGHT, new Position(1, -boardSize + 1),
                Corner.LOWER, new Position(-boardSize + 1, -(boardSize - 1)),
                Corner.LOWER_LEFT, new Position(-boardSize - (boardSize - 1) + 1, -boardSize + 1),
                Corner.UPPER_LEFT, new Position(-boardSize + 1, boardSize - 1)
            );
        } else {
            return Map.of(
                Corner.UPPER, new Position(1, boardSize),
                Corner.UPPER_RIGHT, new Position(boardSize, boardSize - 1),
                Corner.LOWER_RIGHT, new Position(1, -boardSize + 1),
                Corner.LOWER, new Position(-boardSize + 1, -boardSize),
                Corner.LOWER_LEFT, new Position(-boardSize - (boardSize - 1) + 1, -boardSize + 1),
                Corner.UPPER_LEFT, new Position(-boardSize + 1, boardSize - 1)
            );
        }
    }

    private List<Corner> initializeCorners() {
        if (playerCount == 2) {
            return List.of(Corner.UPPER, Corner.LOWER);
        } else if (playerCount == 3) {
            return List.of(Corner.UPPER_LEFT, Corner.UPPER_RIGHT, Corner.LOWER);
        } else if (playerCount == 4) {
            return List.of(Corner.UPPER_LEFT, Corner.LOWER_RIGHT, Corner.LOWER_LEFT, Corner.UPPER_RIGHT);
        } else if (playerCount == 6) {
            return List.of(Corner.UPPER, Corner.UPPER_LEFT, Corner.LOWER_LEFT, Corner.LOWER, Corner.LOWER_RIGHT, Corner.UPPER_RIGHT);
        } else {
            return new ArrayList<>();
        }
    }

    public Position getOffset(Corner corner) {
        return cornerOffsets.get(corner);
    }

    public ArrayList<Corner> getStartingCorners() {
        return new ArrayList<>(startingCorners);
    }
}
