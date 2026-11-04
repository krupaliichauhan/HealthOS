package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.HealthInsight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InsightDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<HealthInsight> getInsights(int userId, int limit) {
        List<HealthInsight> list = new ArrayList<>();
        String sql = "SELECT id, user_id, category, title, message, insight_type, is_read, created_at " +
                "FROM health_insights WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapInsight(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addInsight(HealthInsight in) {
        String sql = "INSERT INTO health_insights (user_id, category, title, message, insight_type, is_read, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, in.getUserId());
            ps.setString(2, in.getCategory());
            ps.setString(3, in.getTitle());
            ps.setString(4, in.getMessage());
            ps.setString(5, in.getInsightType());
            ps.setBoolean(6, in.isRead());
            ps.setTimestamp(7, Timestamp.valueOf(in.getCreatedAt() != null ? in.getCreatedAt() : LocalDateTime.now()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HealthInsight mapInsight(ResultSet rs) throws SQLException {
        HealthInsight in = new HealthInsight();
        in.setId(rs.getInt("id"));
        in.setUserId(rs.getInt("user_id"));
        in.setCategory(rs.getString("category"));
        in.setTitle(rs.getString("title"));
        in.setMessage(rs.getString("message"));
        in.setInsightType(rs.getString("insight_type"));
        in.setRead(rs.getBoolean("is_read"));
        Timestamp c = rs.getTimestamp("created_at");
        if (c != null) in.setCreatedAt(c.toLocalDateTime());
        return in;
    }
}
