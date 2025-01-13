package com.chinese_checkers.server.Game;

import java.util.Collection;

import com.chinese_checkers.server.Connection.PlayerConnection;
import com.chinese_checkers.server.Game.MoveValidator.MoveResult;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.comms.Pawn;

public class GameManager {
    private Board board;
    private MoveValidator moveValidator;

    public GameManager(Board board, MoveValidator moveValidator) {
        this.board = board;
        this.moveValidator = moveValidator;
    }

    public void initializeGame(Collection<PlayerConnection> playerConnections) {
        for(PlayerConnection playerConnection : playerConnections) {
            Player player = playerConnection.getPlayer();
            Pawn pawn = new Pawn(player);
        }
    }

    public MoveResult checkAndMove(Integer pawnId, Position p) {
        Pawn pawn = board.getPawnById(pawnId);
        MoveResult result = moveValidator.isValidMove(board, pawn, p);
        if(result == MoveResult.SUCCESS) {
            board.movePawn(pawn, p);
        }
        return result;
    }
}
