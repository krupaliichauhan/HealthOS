package com.healthos.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ActivityLog {
    private int id;
    private int userId;
    private String activityType;
    private int steps;
    private double distanceKm;
    private int activeMinutes;
    private double caloriesBurned;
    private LocalDate logDate;
    private LocalTime logTime;
    private int dataSourceId;
    private String notes;

    public ActivityLog() {
        this.dataSourceId = 1;
        this.logDate = LocalDate.now();
        this.logTime = LocalTime.now();
    }

    public ActivityLog(int id, int userId, String activityType, int steps, double distanceKm, int activeMinutes, double caloriesBurned, LocalDate logDate, LocalTime logTime, int dataSourceId, String notes) {
        this.id = id;
        this.userId = userId;
        this.activityType = activityType;
        this.steps = steps;
        this.distanceKm = distanceKm;
        this.activeMinutes = activeMinutes;
        this.caloriesBurned = caloriesBurned;
        this.logDate = logDate;
        this.logTime = logTime;
        this.dataSourceId = dataSourceId;
        this.notes = notes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }

    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public int getActiveMinutes() { return activeMinutes; }
    public void setActiveMinutes(int activeMinutes) { this.activeMinutes = activeMinutes; }

    public double getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(double caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public LocalTime getLogTime() { return logTime; }
    public void setLogTime(LocalTime logTime) { this.logTime = logTime; }

    public int getDataSourceId() { return dataSourceId; }
    public void setDataSourceId(int dataSourceId) { this.dataSourceId = dataSourceId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
