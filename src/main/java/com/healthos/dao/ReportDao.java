package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.ReportRecord;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<ReportRecord> getReports(int userId, int limit) {
        List<ReportRecord> list = new ArrayList<>();
        String sql = "SELECT id, user_id, report_title, period_start, period_end, file_path, generated_at " +
                "FROM report_history WHERE user_id = ? ORDER BY generated_at DESC LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReportRecord r = new ReportRecord();
                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setReportTitle(rs.getString("report_title"));
                    Date s = rs.getDate("period_start");
                    if (s != null) r.setPeriodStart(s.toLocalDate());
                    Date e = rs.getDate("period_end");
                    if (e != null) r.setPeriodEnd(e.toLocalDate());
                    r.setFilePath(rs.getString("file_path"));
                    Timestamp g = rs.getTimestamp("generated_at");
                    if (g != null) r.setGeneratedAt(g.toLocalDateTime());
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addReportRecord(ReportRecord r) {
        String sql = "INSERT INTO report_history (user_id, report_title, period_start, period_end, file_path, generated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId());
            ps.setString(2, r.getReportTitle());
            ps.setDate(3, Date.valueOf(r.getPeriodStart()));
            ps.setDate(4, Date.valueOf(r.getPeriodEnd()));
            ps.setString(5, r.getFilePath());
            ps.setTimestamp(6, Timestamp.valueOf(r.getGeneratedAt() != null ? r.getGeneratedAt() : LocalDateTime.now()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
