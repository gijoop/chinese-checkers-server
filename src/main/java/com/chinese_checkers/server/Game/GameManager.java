package com.chinese_checkers.server.Game;

import java.util.Collection;
import java.util.HashMap;

import com.chinese_checkers.server.Connection.PlayerConnection;
import com.chinese_checkers.server.Game.MoveValidator.MoveResult;

public class GameManager {
    private Board board;
    private MoveValidator moveValidator;
    private HashMap<Pawn, Player> players;

    public GameManager(Board board, MoveValidator moveValidator) {
        this.board = board;
        this.moveValidator = moveValidator;
        players = new HashMap<>();
    }

    public void initializeGame(Collection<PlayerConnection> playerConnections) {
        for(PlayerConnection playerConnection : playerConnections) {
            Player player = playerConnection.getPlayer();
            players.put(new Pawn(player.getPawnColor(), player), player);
        }
    }

    public MoveResult checkAndMove(Pawn pawn, Position p) {
        MoveResult result = moveValidator.isValidMove(board, pawn, p);
        if(result == MoveResult.SUCCESS) {
            board.movePawn(pawn, p);
        }
        return result;
    }
}
