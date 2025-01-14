package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chinese_checkers.server.Game.MoveValidator.MoveResult;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;

public class GameManager {
    private Board board;
    private MoveValidator moveValidator;
    private int pawnsPerPlayer;
    private Player.PawnColor currentTurn;
    private ArrayList<Player.PawnColor> avaiableColors;

    public GameManager(Board board, MoveValidator moveValidator, int pawnsPerPlayer) {
        this.board = board;
        this.moveValidator = moveValidator;
        this.pawnsPerPlayer = pawnsPerPlayer;
        this.currentTurn = Player.PawnColor.NONE;
        this.avaiableColors = new ArrayList<>(List.of(Player.PawnColor.values()));
    }

    public GameStartMessage initializeGame(Collection<Player> players) {
        int playerCount = players.size();
        ArrayList<Board.Corner> startingCorners = getStartingCorners(playerCount);
        GameStartMessage gameStartMessage = new GameStartMessage(playerCount);
        
        for(Player player : players) {
            Board.Corner corner = startingCorners.remove(0);
            ArrayList<Position> startingPositions = board.getStartingPositions(corner);
            player.setColor(avaiableColors.remove(0));

            for(int i = 0; i < pawnsPerPlayer; i++) {
                Pawn pawn = new Pawn(player);
                board.addPawn(pawn, startingPositions.get(i));
                gameStartMessage.addPawn(startingPositions.get(i), pawn);
            }
        }

        return gameStartMessage;
    }

    public MoveResult checkAndMove(Integer pawnId, Position p, Player player) {
        Pawn pawn = board.getPawnById(pawnId);

        if(pawn == null) {
            return MoveResult.NONEXISTENT_PAWN;
        }

        if(player.getPawnColor() != currentTurn) {
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
        if(result == MoveResult.SUCCESS) {
            board.movePawn(pawn, p);
        }
        return result;
    }

    private ArrayList<Board.Corner> getStartingCorners(int playerCount) {
        ArrayList<Board.Corner> startingCorners = new ArrayList<>();
        switch(playerCount) {
            case 2:
                startingCorners.add(Board.Corner.UPPER);
                startingCorners.add(Board.Corner.LOWER);
                break;
            case 3:
                startingCorners.add(Board.Corner.UPPER_LEFT);
                startingCorners.add(Board.Corner.UPPER_RIGHT);
                startingCorners.add(Board.Corner.LOWER);
                break;
            case 4:
                startingCorners.add(Board.Corner.UPPER_LEFT);
                startingCorners.add(Board.Corner.LOWER_RIGHT);
                startingCorners.add(Board.Corner.LOWER_LEFT);
                startingCorners.add(Board.Corner.UPPER_RIGHT);
                break;
            case 6:
                startingCorners.add(Board.Corner.UPPER);
                startingCorners.add(Board.Corner.UPPER_LEFT);
                startingCorners.add(Board.Corner.LOWER_LEFT);
                startingCorners.add(Board.Corner.LOWER);
                startingCorners.add(Board.Corner.LOWER_RIGHT);
                startingCorners.add(Board.Corner.UPPER_RIGHT);
                break;
            default:
                break;
        }
        return startingCorners;
    }
}
