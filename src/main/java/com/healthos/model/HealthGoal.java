package com.healthos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HealthGoal {
    private int id;
    private int userId;
    private String goalType; // DAILY_STEPS, DAILY_WATER, DAILY_CALORIES, DAILY_SLEEP, TARGET_WEIGHT
    private double targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // ACTIVE, ACHIEVED, PAUSED, CANCELLED
    private LocalDateTime createdAt;

    public HealthGoal() {
        this.status = "ACTIVE";
        this.startDate = LocalDate.now();
    }

    public HealthGoal(int id, int userId, String goalType, double targetValue, LocalDate startDate, LocalDate endDate, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }

    public double getTargetValue() { return targetValue; }
    public void setTargetValue(double targetValue) { this.targetValue = targetValue; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
