package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.SleepLog;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SleepDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<SleepLog> getSleepLogs(int userId, int limit) {
        List<SleepLog> list = new ArrayList<>();
        String sql = "SELECT id, user_id, sleep_start, sleep_end, duration_minutes, quality_rating, " +
                "sleep_phase_deep_min, sleep_phase_light_min, log_date, data_source_id, notes " +
                "FROM sleep_logs WHERE user_id = ? ORDER BY log_date DESC, sleep_end DESC LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapSleep(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public SleepLog getLatestSleep(int userId) {
        String sql = "SELECT id, user_id, sleep_start, sleep_end, duration_minutes, quality_rating, " +
                "sleep_phase_deep_min, sleep_phase_light_min, log_date, data_source_id, notes " +
                "FROM sleep_logs WHERE user_id = ? ORDER BY log_date DESC LIMIT 1";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSleep(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addSleepLog(SleepLog s) {
        String sql = "INSERT INTO sleep_logs (user_id, sleep_start, sleep_end, duration_minutes, quality_rating, " +
                "sleep_phase_deep_min, sleep_phase_light_min, log_date, data_source_id, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(s.getSleepStart()));
            ps.setTimestamp(3, Timestamp.valueOf(s.getSleepEnd()));
            ps.setInt(4, s.getDurationMinutes());
            ps.setInt(5, s.getQualityRating());
            ps.setInt(6, s.getSleepPhaseDeepMin());
            ps.setInt(7, s.getSleepPhaseLightMin());
            ps.setDate(8, Date.valueOf(s.getLogDate()));
            ps.setInt(9, s.getDataSourceId() > 0 ? s.getDataSourceId() : 1);
            ps.setString(10, s.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSleepLog(int id, int userId) {
        String sql = "DELETE FROM sleep_logs WHERE id = ? AND user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<LocalDate, Double> getDailySleepHoursForPastDays(int userId, int days) {
        Map<LocalDate, Double> map = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().minusDays(days - 1);
        for (int i = 0; i < days; i++) {
            map.put(start.plusDays(i), 0.0);
        }

        String sql = "SELECT log_date, duration_minutes FROM sleep_logs WHERE user_id = ? AND log_date >= ? ORDER BY log_date ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate d = rs.getDate("log_date").toLocalDate();
                    double hours = rs.getInt("duration_minutes") / 60.0;
                    map.put(d, Math.round(hours * 10.0) / 10.0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private SleepLog mapSleep(ResultSet rs) throws SQLException {
        SleepLog s = new SleepLog();
        s.setId(rs.getInt("id"));
        s.setUserId(rs.getInt("user_id"));
        Timestamp start = rs.getTimestamp("sleep_start");
        if (start != null) s.setSleepStart(start.toLocalDateTime());
        Timestamp end = rs.getTimestamp("sleep_end");
        if (end != null) s.setSleepEnd(end.toLocalDateTime());
        s.setDurationMinutes(rs.getInt("duration_minutes"));
        s.setQualityRating(rs.getInt("quality_rating"));
        s.setSleepPhaseDeepMin(rs.getInt("sleep_phase_deep_min"));
        s.setSleepPhaseLightMin(rs.getInt("sleep_phase_light_min"));
        Date d = rs.getDate("log_date");
        if (d != null) s.setLogDate(d.toLocalDate());
        s.setDataSourceId(rs.getInt("data_source_id"));
        s.setNotes(rs.getString("notes"));
        return s;
    }
}
