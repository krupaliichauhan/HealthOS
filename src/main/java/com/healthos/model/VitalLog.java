package com.healthos.model;

import java.time.LocalDateTime;

public class VitalLog {
    private int id;
    private int userId;
    private Integer heartRateBpm;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Double bloodOxygenSpo2;
    private Double temperatureCelsius;
    private Double weightKg;
    private Double heightCm;
    private Double bmi;
    private LocalDateTime logTimestamp;
    private int dataSourceId;
    private String notes;

    public VitalLog() {
        this.dataSourceId = 1;
        this.logTimestamp = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Integer getHeartRateBpm() { return heartRateBpm; }
    public void setHeartRateBpm(Integer heartRateBpm) { this.heartRateBpm = heartRateBpm; }

    public Integer getSystolicBp() { return systolicBp; }
    public void setSystolicBp(Integer systolicBp) { this.systolicBp = systolicBp; }

    public Integer getDiastolicBp() { return diastolicBp; }
    public void setDiastolicBp(Integer diastolicBp) { this.diastolicBp = diastolicBp; }

    public Double getBloodOxygenSpo2() { return bloodOxygenSpo2; }
    public void setBloodOxygenSpo2(Double bloodOxygenSpo2) { this.bloodOxygenSpo2 = bloodOxygenSpo2; }

    public Double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(Double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public Double getHeightCm() { return heightCm; }
    public void setHeightCm(Double heightCm) { this.heightCm = heightCm; }

    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }

    public LocalDateTime getLogTimestamp() { return logTimestamp; }
    public void setLogTimestamp(LocalDateTime logTimestamp) { this.logTimestamp = logTimestamp; }

    public int getDataSourceId() { return dataSourceId; }
    public void setDataSourceId(int dataSourceId) { this.dataSourceId = dataSourceId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
