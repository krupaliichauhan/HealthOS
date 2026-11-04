package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.VitalLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VitalDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<VitalLog> getVitals(int userId, int limit) {
        List<VitalLog> list = new ArrayList<>();
        String sql = "SELECT id, user_id, heart_rate_bpm, systolic_bp, diastolic_bp, blood_oxygen_spo2, " +
                "temperature_celsius, weight_kg, height_cm, bmi, log_timestamp, data_source_id, notes " +
                "FROM vital_logs WHERE user_id = ? ORDER BY log_timestamp DESC LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapVital(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public VitalLog getLatestVital(int userId) {
        String sql = "SELECT id, user_id, heart_rate_bpm, systolic_bp, diastolic_bp, blood_oxygen_spo2, " +
                "temperature_celsius, weight_kg, height_cm, bmi, log_timestamp, data_source_id, notes " +
                "FROM vital_logs WHERE user_id = ? ORDER BY log_timestamp DESC LIMIT 1";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapVital(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addVital(VitalLog v) {
        String sql = "INSERT INTO vital_logs (user_id, heart_rate_bpm, systolic_bp, diastolic_bp, blood_oxygen_spo2, " +
                "temperature_celsius, weight_kg, height_cm, bmi, log_timestamp, data_source_id, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getUserId());
            if (v.getHeartRateBpm() != null) ps.setInt(2, v.getHeartRateBpm()); else ps.setNull(2, java.sql.Types.INTEGER);
            if (v.getSystolicBp() != null) ps.setInt(3, v.getSystolicBp()); else ps.setNull(3, java.sql.Types.INTEGER);
            if (v.getDiastolicBp() != null) ps.setInt(4, v.getDiastolicBp()); else ps.setNull(4, java.sql.Types.INTEGER);
            if (v.getBloodOxygenSpo2() != null) ps.setDouble(5, v.getBloodOxygenSpo2()); else ps.setNull(5, java.sql.Types.DOUBLE);
            if (v.getTemperatureCelsius() != null) ps.setDouble(6, v.getTemperatureCelsius()); else ps.setNull(6, java.sql.Types.DOUBLE);
            if (v.getWeightKg() != null) ps.setDouble(7, v.getWeightKg()); else ps.setNull(7, java.sql.Types.DOUBLE);
            if (v.getHeightCm() != null) ps.setDouble(8, v.getHeightCm()); else ps.setNull(8, java.sql.Types.DOUBLE);
            if (v.getBmi() != null) ps.setDouble(9, v.getBmi()); else ps.setNull(9, java.sql.Types.DOUBLE);
            ps.setTimestamp(10, Timestamp.valueOf(v.getLogTimestamp() != null ? v.getLogTimestamp() : LocalDateTime.now()));
            ps.setInt(11, v.getDataSourceId() > 0 ? v.getDataSourceId() : 1);
            ps.setString(12, v.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVital(int id, int userId) {
        String sql = "DELETE FROM vital_logs WHERE id = ? AND user_id = ?";
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

    private VitalLog mapVital(ResultSet rs) throws SQLException {
        VitalLog v = new VitalLog();
        v.setId(rs.getInt("id"));
        v.setUserId(rs.getInt("user_id"));
        int hr = rs.getInt("heart_rate_bpm");
        if (!rs.wasNull()) v.setHeartRateBpm(hr);
        int sys = rs.getInt("systolic_bp");
        if (!rs.wasNull()) v.setSystolicBp(sys);
        int dia = rs.getInt("diastolic_bp");
        if (!rs.wasNull()) v.setDiastolicBp(dia);
        double spo2 = rs.getDouble("blood_oxygen_spo2");
        if (!rs.wasNull()) v.setBloodOxygenSpo2(spo2);
        double temp = rs.getDouble("temperature_celsius");
        if (!rs.wasNull()) v.setTemperatureCelsius(temp);
        double wt = rs.getDouble("weight_kg");
        if (!rs.wasNull()) v.setWeightKg(wt);
        double ht = rs.getDouble("height_cm");
        if (!rs.wasNull()) v.setHeightCm(ht);
        double bmi = rs.getDouble("bmi");
        if (!rs.wasNull()) v.setBmi(bmi);
        Timestamp ts = rs.getTimestamp("log_timestamp");
        if (ts != null) v.setLogTimestamp(ts.toLocalDateTime());
        v.setDataSourceId(rs.getInt("data_source_id"));
        v.setNotes(rs.getString("notes"));
        return v;
    }
}
