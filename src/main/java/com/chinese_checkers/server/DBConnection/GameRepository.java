package com.chinese_checkers.server.DBConnection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;

public class GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Game game) {
        String sql = "INSERT INTO game (num_players, ruleset, current_turn, board_size) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, 
            game.getNumPlayers(), 
            game.getRuleset().toString(), 
            game.getCurrentTurn().toString(), 
            game.getBoardSize());
        game.setId(jdbcTemplate.queryForObject("SELECT MAX(id) FROM game", Integer.class));
    }

    public void update(Game game) {
        String sql = "UPDATE game SET num_players = ?, ruleset = ?, current_turn = ?, board_size = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
            game.getNumPlayers(), 
            game.getRuleset().toString(), 
            game.getCurrentTurn().toString(), 
            game.getBoardSize(), 
            game.getId());
    }

    public Game findById(int id) {
        String sql = "SELECT * FROM game WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new GameRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Game> findAll() {
        String sql = "SELECT * FROM game";
        try {
            return jdbcTemplate.query(sql, new GameRowMapper());
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM game WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class GameRowMapper implements RowMapper<Game> {
        
        public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Game(
                    rs.getInt("id"),
                    rs.getDate("last_save_date"),
                    rs.getInt("num_players"),
                    Ruleset.type.valueOf(rs.getString("ruleset")),
                    Corner.valueOf(rs.getString("current_turn")),
                    rs.getInt("board_size")
            );
        }
    }
}
