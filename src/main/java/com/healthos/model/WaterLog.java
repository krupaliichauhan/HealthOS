package com.healthos.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class WaterLog {
    private int id;
    private int userId;
    private int amountMl;
    private LocalDate logDate;
    private LocalTime logTime;

    public WaterLog() {
        this.logDate = LocalDate.now();
        this.logTime = LocalTime.now();
    }

    public WaterLog(int id, int userId, int amountMl, LocalDate logDate, LocalTime logTime) {
        this.id = id;
        this.userId = userId;
        this.amountMl = amountMl;
        this.logDate = logDate;
        this.logTime = logTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAmountMl() { return amountMl; }
    public void setAmountMl(int amountMl) { this.amountMl = amountMl; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public LocalTime getLogTime() { return logTime; }
    public void setLogTime(LocalTime logTime) { this.logTime = logTime; }
}
