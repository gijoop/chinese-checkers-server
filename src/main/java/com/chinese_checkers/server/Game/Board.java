package com.chinese_checkers.server.Game;

import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Player.Corner;

import java.util.ArrayList;

import com.chinese_checkers.comms.Pawn;

/**
 * The Board interface defines the methods for interacting with the game board in Chinese Checkers.
 * It includes methods for adding, moving, and retrieving pawns, as well as checking if a position is occupied.
 */
public interface Board {
    /**
     * Adds a pawn to the specified position on the board.
     *
     * @param pawn the pawn to add
     * @param position the position to add the pawn to
     */
    public void addPawn(Pawn pawn, Position position);

    /**
     * Moves a pawn to the specified position on the board.
     *
     * @param pawn the pawn to move
     * @param position the position to move the pawn to
     */
    public void movePawn(Pawn pawn, Position position);

    /**
     * Retrieves the pawn at the specified position on the board.
     *
     * @param position the position to check
     * @return the pawn at the specified position, or null if no pawn is present
     */
    public Pawn getPawnAt(Position position);

    /**
     * Retrieves the pawn with the specified ID.
     *
     * @param id the ID of the pawn to retrieve
     * @return the pawn with the specified ID, or null if no pawn is found
     */
    public Pawn getPawnById(int id);

    /**
     * Retrieves the position of the specified pawn on the board.
     *
     * @param pawn the pawn to find
     * @return the position of the specified pawn
     */
    public Position getPositionOf(Pawn pawn);

    /**
     * Retrieves the size of the board.
     *
     * @return the size of the board
     */
    public Integer getSize();

    /**
     * Retrieves the pawns belonging to the player in the specified corner.
     *
     * @param corner the corner to get the pawns from
     * @return a list of pawns belonging to the player in the specified corner
     */
    public ArrayList<Pawn> getPlayerPawns(Corner corner);

    /**
     * Retrieves all the pawns on the board.
     *
     * @return a list of all the pawns on the board
     */
    public ArrayList<Pawn> getPawns();

    /**
     * Checks if the specified position on the board is occupied by a pawn.
     *
     * @param position the position to check
     * @return true if the position is occupied, false otherwise
     */
    public boolean isOccupied(Position position);

    /**
     * Prints the current state of the board.
     */
    public void printBoard();
}