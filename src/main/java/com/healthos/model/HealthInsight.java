package com.healthos.model;

import java.time.LocalDateTime;

public class HealthInsight {
    private int id;
    private int userId;
    private String category; // ACTIVITY, SLEEP, HYDRATION, NUTRITION, VITALS, GENERAL
    private String title;
    private String message;
    private String insightType; // ACHIEVEMENT, POSITIVE_TREND, GENTLE_REMINDER, TIP
    private boolean isRead;
    private LocalDateTime createdAt;

    public HealthInsight() {
        this.createdAt = LocalDateTime.now();
        this.insightType = "TIP";
    }

    public HealthInsight(int id, int userId, String category, String title, String message, String insightType, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.message = message;
        this.insightType = insightType;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getInsightType() { return insightType; }
    public void setInsightType(String insightType) { this.insightType = insightType; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
