package com.healthos.model;

import java.time.LocalDateTime;

public class UserProfile {
    private int id;
    private int userId;
    private int age;
    private String gender;
    private double heightCm;
    private double weightKg;
    private int dailyStepGoal;
    private int dailyWaterGoalMl;
    private int dailyCalorieGoal;
    private int dailySleepGoalMinutes;
    private String themePreference;
    private LocalDateTime updatedAt;

    public UserProfile() {
        this.dailyStepGoal = 10000;
        this.dailyWaterGoalMl = 2500;
        this.dailyCalorieGoal = 2000;
        this.dailySleepGoalMinutes = 480;
        this.themePreference = "PASTEL_MINT";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public int getDailyStepGoal() { return dailyStepGoal; }
    public void setDailyStepGoal(int dailyStepGoal) { this.dailyStepGoal = dailyStepGoal; }

    public int getDailyWaterGoalMl() { return dailyWaterGoalMl; }
    public void setDailyWaterGoalMl(int dailyWaterGoalMl) { this.dailyWaterGoalMl = dailyWaterGoalMl; }

    public int getDailyCalorieGoal() { return dailyCalorieGoal; }
    public void setDailyCalorieGoal(int dailyCalorieGoal) { this.dailyCalorieGoal = dailyCalorieGoal; }

    public int getDailySleepGoalMinutes() { return dailySleepGoalMinutes; }
    public void setDailySleepGoalMinutes(int dailySleepGoalMinutes) { this.dailySleepGoalMinutes = dailySleepGoalMinutes; }

    public String getThemePreference() { return themePreference; }
    public void setThemePreference(String themePreference) { this.themePreference = themePreference; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
