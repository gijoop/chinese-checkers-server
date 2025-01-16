package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Ruleset.PlayerConfig;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.Game.Ruleset.Ruleset.MoveResult;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Message.FromServer.GameStartMessage;

public class GameManager {
    private Board board;
    private Ruleset ruleset;
    private int pawnsPerPlayer;
    private Optional<Corner> currentTurn;
    private ArrayList<Corner> takenCorners;
    private boolean hasJumped;
    private PlayerConfig playerConfig;

    public GameManager(Board board, Ruleset ruleset, int pawnsPerPlayer) {
        this.board = board;
        this.ruleset = ruleset;
        this.pawnsPerPlayer = pawnsPerPlayer;
        this.currentTurn = Optional.empty();
        this.takenCorners = new ArrayList<>();
        this.hasJumped = false;
    }

    public GameStartMessage initializeGame(Collection<Player> players) {
        playerConfig = new PlayerConfig(players.size(), board);
        ArrayList<Corner> startingCorners = playerConfig.getStartingCorners();
        GameStartMessage gameStartMessage = new GameStartMessage(board.getSize());
        
        for(Player player : players) {
            Corner corner = startingCorners.remove(0);
            player.setCorner(corner);
            takenCorners.add(corner);

            ArrayList<Position> startingPositions = ruleset.getStartingPositions(player.getCorner());
            for(int i = 0; i < pawnsPerPlayer; i++) {
                Pawn pawn = new Pawn(player);
                board.addPawn(pawn, startingPositions.get(i));
                gameStartMessage.addPawn(startingPositions.get(i), pawn);
            }
        }


        currentTurn = Optional.of(players.stream().skip((int)(players.size() * Math.random())).findFirst().get().getCorner());
        return gameStartMessage;
    }

    public MoveResult checkAndMove(Integer pawnId, Position p, Player player) {
        if(!isPlayerTurn(player)) {
            System.out.println("Player " + player.getName() + " tried to move when it's not their turn");
            return MoveResult.NOT_YOUR_TURN;
        }
        
        Pawn pawn = board.getPawnById(pawnId);

        if(!isValidPawn(pawn, player)) {
            System.out.println("Player " + player.getName() + " tried to move a pawn that doesn't belong to them or doesn't exist");
            return MoveResult.INVALID_PAWN;
        }

        MoveResult result = ruleset.validateMove(pawn, p);

        if(result == MoveResult.SUCCESS && !hasJumped) {
            System.out.println("Player " + player.getName() + " moved pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ")");
            board.movePawn(pawn, p);
        }
        else if(result == MoveResult.SUCCESS_JUMP) {
            System.out.println("Player " + player.getName() + " jumped pawn " + pawnId + " to (" + p.getX() + ", " + p.getY() + ")");
            board.movePawn(pawn, p);
            hasJumped = true;
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
        
        endTurn(player);
        return result;
    }

    private boolean isPlayerTurn(Player player) {
        return player.getCorner().equals(currentTurn.orElse(null));
    }
    
    private boolean isValidPawn(Pawn pawn, Player player) {
        return pawn != null && pawn.getOwner().equals(player);
    }

    public boolean checkWin(Player player) {
        Corner goalCorner = player.getCorner().getOpposite();
        Set<Position> winningPositions = new HashSet<>(ruleset.getStartingPositions(goalCorner));
        return board.getPlayerPawns(player.getCorner()).stream()
                    .map(board::getPositionOf)
                    .allMatch(winningPositions::contains);
    }

    public Corner getCurrentTurn() {
        return currentTurn.orElse(null);
    }

    public void endTurn(Player player) {
        if(player.getCorner() != currentTurn.get()) {
            System.out.println("Player " + player.getName() + " tried to end turn when it's not their turn");
            return;
        }

        System.out.print("Changing turn from " + currentTurn + " to ");
        setNextTurn();
        System.out.println(currentTurn);
    }

    private void setNextTurn() {
        currentTurn = currentTurn.map(corner -> 
            takenCorners.get((takenCorners.indexOf(corner) + 1) % takenCorners.size())
        );
        hasJumped = false;
    } 
}
