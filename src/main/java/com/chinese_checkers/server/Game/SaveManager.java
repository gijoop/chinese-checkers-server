package com.chinese_checkers.server.Game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.DBConnection.Game;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;
import com.chinese_checkers.server.DBConnection.GameRepository;
import com.chinese_checkers.server.DBConnection.DBMove;
import com.chinese_checkers.server.DBConnection.MoveRepository;
import com.chinese_checkers.server.DBConnection.SpringConfig;

public class SaveManager {
    private AnnotationConfigApplicationContext context;
    private GameRepository gameRepository;
    private MoveRepository moveRepository;
    private Game currentGame;
    private Corner currentTurn;

    private int move_counter;

    private static volatile SaveManager instance;

    private SaveManager() {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);

        gameRepository = new GameRepository(context.getBean("jdbcTemplate", JdbcTemplate.class));
        moveRepository = new MoveRepository(context.getBean("jdbcTemplate", JdbcTemplate.class));

        move_counter = 0;
    }

    public static SaveManager getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (SaveManager.class) {
            if (instance == null) {
                instance = new SaveManager();
            }
            return instance;
        }
    }

    public void newGame(int numPlayers, Ruleset.type ruleset, Corner currentTurn, int boardSize) {
        currentGame = new Game(numPlayers, ruleset, currentTurn, boardSize);
        gameRepository.save(currentGame);
    }

    public void saveMove(Move move) {
        DBMove dbmove = new DBMove(currentGame.getId(),
            ++move_counter, 
            move.getStart().getX(), 
            move.getStart().getY(), 
            move.getGoal().getX(), 
            move.getGoal().getY());
        moveRepository.save(dbmove);
    }
    
    public void loadGameToBoard(Game game, Board board) {
        currentGame = game;

        ArrayList<DBMove> moves = (ArrayList<DBMove>) moveRepository.findByGameId(currentGame.getId());
        for (DBMove move : moves) {
            Position start = new Position(move.getFromX(), move.getFromY());
            Position goal = new Position(move.getToX(), move.getToY());
            Pawn pawn = board.getPawnAt(start);
            board.movePawn(pawn, goal);
            currentTurn = pawn.getOwner().getCorner();
        }
        move_counter = moves.size();
    }

    public List<Game> getSaves() {
        return gameRepository.findAll();
    }

    public Game getGameById(int id) {
        return gameRepository.findById(id);
    }

    public Corner getCurrentTurn() {
        return currentTurn;
    }

    public void updateSave(Corner currentTurn) {
        currentGame.setCurrentTurn(currentTurn);
        gameRepository.update(currentGame);
    }
}
