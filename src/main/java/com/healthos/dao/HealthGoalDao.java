package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.HealthGoal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HealthGoalDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<HealthGoal> getActiveGoals(int userId) {
        List<HealthGoal> list = new ArrayList<>();
        String sql = "SELECT id, user_id, goal_type, target_value, start_date, end_date, status, created_at " +
                "FROM health_goals WHERE user_id = ? AND status = 'ACTIVE' ORDER BY id ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapGoal(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addGoal(HealthGoal g) {
        String sql = "INSERT INTO health_goals (user_id, goal_type, target_value, start_date, end_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, g.getUserId());
            ps.setString(2, g.getGoalType());
            ps.setDouble(3, g.getTargetValue());
            ps.setDate(4, Date.valueOf(g.getStartDate() != null ? g.getStartDate() : LocalDate.now()));
            if (g.getEndDate() != null) ps.setDate(5, Date.valueOf(g.getEndDate())); else ps.setNull(5, java.sql.Types.DATE);
            ps.setString(6, g.getStatus() != null ? g.getStatus() : "ACTIVE");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGoalTarget(int goalId, int userId, double targetValue) {
        String sql = "UPDATE health_goals SET target_value = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, targetValue);
            ps.setInt(2, goalId);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HealthGoal mapGoal(ResultSet rs) throws SQLException {
        HealthGoal g = new HealthGoal();
        g.setId(rs.getInt("id"));
        g.setUserId(rs.getInt("user_id"));
        g.setGoalType(rs.getString("goal_type"));
        g.setTargetValue(rs.getDouble("target_value"));
        Date s = rs.getDate("start_date");
        if (s != null) g.setStartDate(s.toLocalDate());
        Date e = rs.getDate("end_date");
        if (e != null) g.setEndDate(e.toLocalDate());
        g.setStatus(rs.getString("status"));
        Timestamp c = rs.getTimestamp("created_at");
        if (c != null) g.setCreatedAt(c.toLocalDateTime());
        return g;
    }
}
