package com.chinese_checkers.server.Game.Ruleset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;

/**
 * The CornerHelper class provides utility methods for handling corner-related configurations in the game.
 * It initializes player count, board size, corner offsets, and starting corners based on the number of players.
 */
public class CornerHelper {
    private final int playerCount;
    private final int boardSize;
    private final Map<Corner, Position> cornerOffsets;
    private final List<Corner> startingCorners;

    /**
     * Constructs a CornerHelper object with the specified player count and board.
     *
     * @param playerCount the number of players in the game
     * @param board the game board
     */
    public CornerHelper(int playerCount, int boardSize) {
        this.playerCount = playerCount;
        this.boardSize = boardSize;
        this.cornerOffsets = initializeOffsets();
        this.startingCorners = initializeCorners();
    }

    /**
     * Determines if the starting corner is a reversed triangle.
     *
     * @param corner the corner to check
     * @return true if the corner is in reversed triangle, false otherwise
     */
    public static boolean isStartingCornerReverse(Corner corner) {
        return corner == Corner.LOWER || corner == Corner.UPPER_LEFT || corner == Corner.UPPER_RIGHT;
    }

    /**
     * Initializes the corner offsets based on the number of players.
     *
     * @return a map of corner offsets
     */
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

    /**
     * Initializes the starting corners based on the number of players.
     *
     * @return a list of starting corners
     */
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

    /**
     * Gets the number of players in the game.
     *
     * @return the number of players
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * Gets the size of the board.
     *
     * @return the board size
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Gets the offset position, so loweftmost corner 
     * of base/reversed triangle representing board corner.
     *
     * @param corner the corner to get the offset for
     * @return the offset position
     */
    public Position getOffset(Corner corner) {
        return cornerOffsets.get(corner);
    }

    /**
     * Gets the starting corners for the players.
     *
     * @return a list of starting corners
     */
    public ArrayList<Corner> getStartingCorners() {
        return new ArrayList<>(startingCorners);
    }
}