package com.healthos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReportRecord {
    private int id;
    private int userId;
    private String reportTitle;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String filePath;
    private LocalDateTime generatedAt;

    public ReportRecord() {
        this.generatedAt = LocalDateTime.now();
    }

    public ReportRecord(int id, int userId, String reportTitle, LocalDate periodStart, LocalDate periodEnd, String filePath, LocalDateTime generatedAt) {
        this.id = id;
        this.userId = userId;
        this.reportTitle = reportTitle;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.filePath = filePath;
        this.generatedAt = generatedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getReportTitle() { return reportTitle; }
    public void setReportTitle(String reportTitle) { this.reportTitle = reportTitle; }

    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }

    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
