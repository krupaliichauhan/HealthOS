package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserProfileDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public UserProfile getProfile(int userId) {
        String sql = "SELECT id, user_id, age, gender, height_cm, weight_kg, daily_step_goal, " +
                "daily_water_goal_ml, daily_calorie_goal, daily_sleep_goal_minutes, theme_preference, updated_at " +
                "FROM user_profiles WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProfile(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProfile(UserProfile profile) {
        String sql = "UPDATE user_profiles SET age = ?, gender = ?, height_cm = ?, weight_kg = ?, " +
                "daily_step_goal = ?, daily_water_goal_ml = ?, daily_calorie_goal = ?, daily_sleep_goal_minutes = ?, " +
                "theme_preference = ?, updated_at = ? WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, profile.getAge());
            ps.setString(2, profile.getGender());
            ps.setDouble(3, profile.getHeightCm());
            ps.setDouble(4, profile.getWeightKg());
            ps.setInt(5, profile.getDailyStepGoal());
            ps.setInt(6, profile.getDailyWaterGoalMl());
            ps.setInt(7, profile.getDailyCalorieGoal());
            ps.setInt(8, profile.getDailySleepGoalMinutes());
            ps.setString(9, profile.getThemePreference());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(11, profile.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private UserProfile mapProfile(ResultSet rs) throws SQLException {
        UserProfile p = new UserProfile();
        p.setId(rs.getInt("id"));
        p.setUserId(rs.getInt("user_id"));
        p.setAge(rs.getInt("age"));
        p.setGender(rs.getString("gender"));
        p.setHeightCm(rs.getDouble("height_cm"));
        p.setWeightKg(rs.getDouble("weight_kg"));
        p.setDailyStepGoal(rs.getInt("daily_step_goal"));
        p.setDailyWaterGoalMl(rs.getInt("daily_water_goal_ml"));
        p.setDailyCalorieGoal(rs.getInt("daily_calorie_goal"));
        p.setDailySleepGoalMinutes(rs.getInt("daily_sleep_goal_minutes"));
        p.setThemePreference(rs.getString("theme_preference"));
        Timestamp uat = rs.getTimestamp("updated_at");
        if (uat != null) p.setUpdatedAt(uat.toLocalDateTime());
        return p;
    }
}
