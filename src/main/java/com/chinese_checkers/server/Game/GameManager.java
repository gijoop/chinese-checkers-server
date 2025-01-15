package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.Collection;

import com.chinese_checkers.server.Game.Ruleset.MoveResult;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;

public class GameManager {
    private Board board;
    private Ruleset moveValidator;
    private int pawnsPerPlayer;
    private Corner currentTurn;
    private ArrayList<Corner> takenCorners;
    private boolean onlyJump;

    public GameManager(Board board, Ruleset moveValidator, int pawnsPerPlayer) {
        this.board = board;
        this.moveValidator = moveValidator;
        this.pawnsPerPlayer = pawnsPerPlayer;
        this.currentTurn = Corner.NONE;
        this.takenCorners = new ArrayList<>();
        this.onlyJump = false;
    }

    public GameStartMessage initializeGame(Collection<Player> players) {
        int playerCount = players.size();
        ArrayList<Corner> startingCorners = getStartingCorners(playerCount);
        GameStartMessage gameStartMessage = new GameStartMessage(playerCount);
        
        for(Player player : players) {
            Corner corner = startingCorners.remove(0);
            ArrayList<Position> startingPositions = board.getStartingPositions(corner);
            player.setCorner(corner);
            takenCorners.add(corner);

            for(int i = 0; i < pawnsPerPlayer; i++) {
                Pawn pawn = new Pawn(player);
                board.addPawn(pawn, startingPositions.get(i));
                gameStartMessage.addPawn(startingPositions.get(i), pawn);
            }
        }

        //currentTurn = players.stream().skip((int)(players.size() * Math.random())).findFirst().get().getColor();
        currentTurn = takenCorners.get(0);
        System.out.println("Starting turn for player " + currentTurn);
        return gameStartMessage;
    }

    public MoveResult checkAndMove(Integer pawnId, Position p, Player player) {
        Pawn pawn = board.getPawnById(pawnId);

        if(pawn == null) {
            return MoveResult.NONEXISTENT_PAWN;
        }

        if(player.getCorner() != currentTurn) {
            System.out.println("Player " + player.getName() + " tried to move when it's not their turn");
            return MoveResult.NOT_YOUR_TURN;
        }

        if(!pawn.getOwner().equals(player)) {
            System.out.println("Player " + player.getName() + " tried to move pawn " + pawnId + " which is owned by " + pawn.getOwner().getName());
            return MoveResult.INVALID_MOVE;
        }

        if(board.isOccupied(p)) {
            System.out.println("Player " + player.getName() + " tried to move to an occupied position");
            return MoveResult.OCCUPIED;
        }

        MoveResult result = moveValidator.isValidMove(board, pawn, p);
        if(result == MoveResult.SUCCESS && !onlyJump) {
            System.out.println("Player " + player.getName() + " moved pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ")");
            board.movePawn(pawn, p);
        }
        else if(result == MoveResult.SUCCESS_JUMP) {
            System.out.println("Player " + player.getName() + " jumped pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ")");
            board.movePawn(pawn, p);
            onlyJump = true;
            return result;
        }
        else {
            System.out.println("Player " + player.getName() + " tried to move pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ") but it was invalid: " + result);
            return result;
        }

        if(checkWin(player)) {
            System.out.println("Player " + player.getName() + " won the game!");
            return MoveResult.GAME_OVER;
        }
        
        System.out.print("Changing turn from " + currentTurn + " to ");
        currentTurn = getNextTurn();
        System.out.println(currentTurn);

        onlyJump = false;
        return result;
    }

    public boolean checkWin(Player player) {
        Corner goalCorner = player.getCorner().getOpposite();
        ArrayList<Position> winningPositions = board.getStartingPositions(goalCorner);
        for(Pawn pawn : board.getPlayerPawns(player.getCorner())) {
            if(!winningPositions.contains(board.getPositionOf(pawn))) {
                return false;
            }
        }
        return true;
    }

    public void endTurn(Player player) {
        if(player.getCorner() != currentTurn) {
            System.out.println("Player " + player.getName() + " tried to end turn when it's not their turn");
            return;
        }

        System.out.print("Changing turn from " + currentTurn + " to ");
        currentTurn = getNextTurn();
        System.out.println(currentTurn);
    }

    public Corner getNextTurn() {
        return takenCorners.get((takenCorners.indexOf(currentTurn) + 1) % takenCorners.size());
    }  

    private ArrayList<Corner> getStartingCorners(int playerCount) {
        ArrayList<Corner> startingCorners = new ArrayList<>();
        switch(playerCount) {
            case 2:
                startingCorners.add(Corner.UPPER);
                startingCorners.add(Corner.LOWER);
                break;
            case 3:
                startingCorners.add(Corner.UPPER_LEFT);
                startingCorners.add(Corner.UPPER_RIGHT);
                startingCorners.add(Corner.LOWER);
                break;
            case 4:
                startingCorners.add(Corner.UPPER_LEFT);
                startingCorners.add(Corner.LOWER_RIGHT);
                startingCorners.add(Corner.LOWER_LEFT);
                startingCorners.add(Corner.UPPER_RIGHT);
                break;
            case 6:
                startingCorners.add(Corner.UPPER);
                startingCorners.add(Corner.UPPER_LEFT);
                startingCorners.add(Corner.LOWER_LEFT);
                startingCorners.add(Corner.LOWER);
                startingCorners.add(Corner.LOWER_RIGHT);
                startingCorners.add(Corner.UPPER_RIGHT);
                break;
            default:
                break;
        }
        return startingCorners;
    }
}
