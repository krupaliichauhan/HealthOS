package com.healthos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SleepLog {
    private int id;
    private int userId;
    private LocalDateTime sleepStart;
    private LocalDateTime sleepEnd;
    private int durationMinutes;
    private int qualityRating; // 1 to 5
    private int sleepPhaseDeepMin;
    private int sleepPhaseLightMin;
    private LocalDate logDate;
    private int dataSourceId;
    private String notes;

    public SleepLog() {
        this.dataSourceId = 1;
        this.qualityRating = 4;
        this.logDate = LocalDate.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getSleepStart() { return sleepStart; }
    public void setSleepStart(LocalDateTime sleepStart) { this.sleepStart = sleepStart; }

    public LocalDateTime getSleepEnd() { return sleepEnd; }
    public void setSleepEnd(LocalDateTime sleepEnd) { this.sleepEnd = sleepEnd; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getQualityRating() { return qualityRating; }
    public void setQualityRating(int qualityRating) { this.qualityRating = qualityRating; }

    public int getSleepPhaseDeepMin() { return sleepPhaseDeepMin; }
    public void setSleepPhaseDeepMin(int sleepPhaseDeepMin) { this.sleepPhaseDeepMin = sleepPhaseDeepMin; }

    public int getSleepPhaseLightMin() { return sleepPhaseLightMin; }
    public void setSleepPhaseLightMin(int sleepPhaseLightMin) { this.sleepPhaseLightMin = sleepPhaseLightMin; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public int getDataSourceId() { return dataSourceId; }
    public void setDataSourceId(int dataSourceId) { this.dataSourceId = dataSourceId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
