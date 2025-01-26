package com.chinese_checkers.server.DBConnection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MoveRepository {

    private final JdbcTemplate jdbcTemplate;

    public MoveRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(DBMove move) {
        String sql = "INSERT INTO move (game_id, move_number, from_x, from_y, to_x, to_y) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, move.getGameId(), move.getMoveNumber(),
                move.getFromX(), move.getFromY(), move.getToX(), move.getToY());
    }

    public DBMove findById(int id) {
        String sql = "SELECT * FROM move WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new MoveRowMapper(), id);
    }

    public List<DBMove> findAll() {
        String sql = "SELECT * FROM move";
        return jdbcTemplate.query(sql, new MoveRowMapper());
    }

    public List<DBMove> findByGameId(int gameId) {
        String sql = "SELECT * FROM move WHERE game_id = ? ORDER BY move_number ASC";
        return jdbcTemplate.query(sql, new MoveRowMapper(), gameId);
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM move WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class MoveRowMapper implements RowMapper<DBMove> {
        @Override
        public DBMove mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DBMove(
                    rs.getInt("game_id"),
                    rs.getInt("move_number"),
                    rs.getInt("from_x"),
                    rs.getInt("from_y"),
                    rs.getInt("to_x"),
                    rs.getInt("to_y")
            );
        }
    }
}
