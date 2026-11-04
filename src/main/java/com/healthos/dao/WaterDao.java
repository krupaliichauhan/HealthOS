package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.WaterLog;

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

public class WaterDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public int getTodayWaterTotalMl(int userId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(amount_ml), 0) FROM water_logs WHERE user_id = ? AND log_date = ?";
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

    public List<WaterLog> getWaterLogs(int userId, LocalDate date) {
        List<WaterLog> list = new ArrayList<>();
        String sql = "SELECT id, user_id, amount_ml, log_date, log_time FROM water_logs " +
                "WHERE user_id = ? AND log_date = ? ORDER BY log_time DESC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapWater(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addWaterLog(WaterLog log) {
        String sql = "INSERT INTO water_logs (user_id, amount_ml, log_date, log_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, log.getUserId());
            ps.setInt(2, log.getAmountMl());
            ps.setDate(3, Date.valueOf(log.getLogDate()));
            ps.setTime(4, Time.valueOf(log.getLogTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWaterLog(int id, int userId) {
        String sql = "DELETE FROM water_logs WHERE id = ? AND user_id = ?";
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

    public Map<LocalDate, Integer> getDailyWaterForPastDays(int userId, int days) {
        Map<LocalDate, Integer> map = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().minusDays(days - 1);
        for (int i = 0; i < days; i++) {
            map.put(start.plusDays(i), 0);
        }

        String sql = "SELECT log_date, SUM(amount_ml) as total_ml FROM water_logs " +
                "WHERE user_id = ? AND log_date >= ? GROUP BY log_date ORDER BY log_date ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate d = rs.getDate("log_date").toLocalDate();
                    map.put(d, rs.getInt("total_ml"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private WaterLog mapWater(ResultSet rs) throws SQLException {
        WaterLog w = new WaterLog();
        w.setId(rs.getInt("id"));
        w.setUserId(rs.getInt("user_id"));
        w.setAmountMl(rs.getInt("amount_ml"));
        Date d = rs.getDate("log_date");
        if (d != null) w.setLogDate(d.toLocalDate());
        Time t = rs.getTime("log_time");
        if (t != null) w.setLogTime(t.toLocalTime());
        return w;
    }
}
