package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.ActivityLog;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<ActivityLog> getActivities(int userId, LocalDate startDate, LocalDate endDate) {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT id, user_id, activity_type, steps, distance_km, active_minutes, calories_burned, " +
                "log_date, log_time, data_source_id, notes FROM activity_logs " +
                "WHERE user_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date DESC, log_time DESC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapActivity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ActivityLog> getRecentActivities(int userId, int limit) {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT id, user_id, activity_type, steps, distance_km, active_minutes, calories_burned, " +
                "log_date, log_time, data_source_id, notes FROM activity_logs " +
                "WHERE user_id = ? ORDER BY log_date DESC, log_time DESC LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapActivity(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addActivity(ActivityLog log) {
        String sql = "INSERT INTO activity_logs (user_id, activity_type, steps, distance_km, active_minutes, " +
                "calories_burned, log_date, log_time, data_source_id, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getUserId());
            ps.setString(2, log.getActivityType());
            ps.setInt(3, log.getSteps());
            ps.setDouble(4, log.getDistanceKm());
            ps.setInt(5, log.getActiveMinutes());
            ps.setDouble(6, log.getCaloriesBurned());
            ps.setDate(7, Date.valueOf(log.getLogDate()));
            ps.setTime(8, log.getLogTime() != null ? Time.valueOf(log.getLogTime()) : Time.valueOf("12:00:00"));
            ps.setInt(9, log.getDataSourceId() > 0 ? log.getDataSourceId() : 1);
            ps.setString(10, log.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteActivity(int logId, int userId) {
        String sql = "DELETE FROM activity_logs WHERE id = ? AND user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getStepsForDate(int userId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(steps), 0) FROM activity_logs WHERE user_id = ? AND log_date = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<LocalDate, Integer> getDailyStepsForPastDays(int userId, int days) {
        Map<LocalDate, Integer> map = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().minusDays(days - 1);
        for (int i = 0; i < days; i++) {
            map.put(start.plusDays(i), 0);
        }

        String sql = "SELECT log_date, SUM(steps) as total_steps FROM activity_logs " +
                "WHERE user_id = ? AND log_date >= ? GROUP BY log_date ORDER BY log_date ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate d = rs.getDate("log_date").toLocalDate();
                    map.put(d, rs.getInt("total_steps"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private ActivityLog mapActivity(ResultSet rs) throws SQLException {
        ActivityLog log = new ActivityLog();
        log.setId(rs.getInt("id"));
        log.setUserId(rs.getInt("user_id"));
        log.setActivityType(rs.getString("activity_type"));
        log.setSteps(rs.getInt("steps"));
        log.setDistanceKm(rs.getDouble("distance_km"));
        log.setActiveMinutes(rs.getInt("active_minutes"));
        log.setCaloriesBurned(rs.getDouble("calories_burned"));
        Date d = rs.getDate("log_date");
        if (d != null) log.setLogDate(d.toLocalDate());
        Time t = rs.getTime("log_time");
        if (t != null) log.setLogTime(t.toLocalTime());
        log.setDataSourceId(rs.getInt("data_source_id"));
        log.setNotes(rs.getString("notes"));
        return log;
    }
}
