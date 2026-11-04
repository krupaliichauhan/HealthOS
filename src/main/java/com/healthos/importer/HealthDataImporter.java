package com.healthos.importer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.healthos.dao.ActivityDao;
import com.healthos.dao.VitalDao;
import com.healthos.model.ActivityLog;
import com.healthos.model.VitalLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HealthOS Data Importer.
 * Supports CSV and JSON batch importing with validation and provenance tracking.
 */
public class HealthDataImporter {

    private final ActivityDao activityDao = new ActivityDao();
    private final VitalDao vitalDao = new VitalDao();
    private final ObjectMapper objectMapper;

    public HealthDataImporter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public static class ImportResult {
        private int successCount = 0;
        private int errorCount = 0;
        private final List<String> logs = new ArrayList<>();

        public void addSuccess(String msg) {
            successCount++;
            logs.add("✓ " + msg);
        }

        public void addError(String msg) {
            errorCount++;
            logs.add("✗ " + msg);
        }

        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
        public List<String> getLogs() { return logs; }
    }

    /**
     * Imports activity logs from a CSV file.
     * Expected headers: activity_type, steps, distance_km, active_minutes, calories_burned, log_date, log_time, notes
     */
    public ImportResult importActivityCsv(File file, int userId) {
        ImportResult result = new ImportResult();
        if (file == null || !file.exists()) {
            result.addError("Selected CSV file does not exist.");
            return result;
        }

        try (Reader reader = new FileReader(file, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build())) {

            for (CSVRecord record : parser) {
                try {
                    String type = record.isSet("activity_type") ? record.get("activity_type") : "WALKING";
                    int steps = record.isSet("steps") ? Integer.parseInt(record.get("steps")) : 0;
                    double dist = record.isSet("distance_km") ? Double.parseDouble(record.get("distance_km")) : 0.0;
                    int mins = record.isSet("active_minutes") ? Integer.parseInt(record.get("active_minutes")) : 0;
                    double cal = record.isSet("calories_burned") ? Double.parseDouble(record.get("calories_burned")) : 0.0;
                    LocalDate date = record.isSet("log_date") ? LocalDate.parse(record.get("log_date")) : LocalDate.now();
                    LocalTime time = record.isSet("log_time") ? LocalTime.parse(record.get("log_time")) : LocalTime.now();
                    String notes = record.isSet("notes") ? record.get("notes") : "CSV Imported Record";

                    ActivityLog log = new ActivityLog(0, userId, type.toUpperCase(), steps, dist, mins, cal, date, time, 2, notes);
                    boolean saved = activityDao.addActivity(log);
                    if (saved) {
                        result.addSuccess("Row " + record.getRecordNumber() + ": " + type + " (" + steps + " steps) on " + date);
                    } else {
                        result.addError("Row " + record.getRecordNumber() + ": Failed to insert into database.");
                    }
                } catch (Exception rowEx) {
                    result.addError("Row " + record.getRecordNumber() + " format error: " + rowEx.getMessage());
                }
            }
        } catch (Exception e) {
            result.addError("Failed to parse CSV file: " + e.getMessage());
        }

        return result;
    }

    /**
     * Imports vitals from a JSON file.
     */
    public ImportResult importVitalsJson(File file, int userId) {
        ImportResult result = new ImportResult();
        if (file == null || !file.exists()) {
            result.addError("Selected JSON file does not exist.");
            return result;
        }

        try {
            List<Map<String, Object>> list = objectMapper.readValue(file, new TypeReference<>() {});
            int index = 0;
            for (Map<String, Object> map : list) {
                index++;
                try {
                    VitalLog v = new VitalLog();
                    v.setUserId(userId);
                    v.setDataSourceId(3); // JSON_IMPORT

                    if (map.containsKey("heartRateBpm") && map.get("heartRateBpm") != null) {
                        v.setHeartRateBpm(((Number) map.get("heartRateBpm")).intValue());
                    }
                    if (map.containsKey("systolicBp") && map.get("systolicBp") != null) {
                        v.setSystolicBp(((Number) map.get("systolicBp")).intValue());
                    }
                    if (map.containsKey("diastolicBp") && map.get("diastolicBp") != null) {
                        v.setDiastolicBp(((Number) map.get("diastolicBp")).intValue());
                    }
                    if (map.containsKey("bloodOxygenSpo2") && map.get("bloodOxygenSpo2") != null) {
                        v.setBloodOxygenSpo2(((Number) map.get("bloodOxygenSpo2")).doubleValue());
                    }
                    if (map.containsKey("temperatureCelsius") && map.get("temperatureCelsius") != null) {
                        v.setTemperatureCelsius(((Number) map.get("temperatureCelsius")).doubleValue());
                    }
                    if (map.containsKey("weightKg") && map.get("weightKg") != null) {
                        v.setWeightKg(((Number) map.get("weightKg")).doubleValue());
                    }
                    if (map.containsKey("heightCm") && map.get("heightCm") != null) {
                        v.setHeightCm(((Number) map.get("heightCm")).doubleValue());
                    }
                    if (map.containsKey("bmi") && map.get("bmi") != null) {
                        v.setBmi(((Number) map.get("bmi")).doubleValue());
                    }
                    v.setNotes((String) map.getOrDefault("notes", "JSON Imported Vitals"));

                    boolean saved = vitalDao.addVital(v);
                    if (saved) {
                        result.addSuccess("Item " + index + ": Vitals record imported successfully.");
                    } else {
                        result.addError("Item " + index + ": Database save failed.");
                    }
                } catch (Exception itemEx) {
                    result.addError("Item " + index + " error: " + itemEx.getMessage());
                }
            }
        } catch (Exception e) {
            result.addError("Failed to parse JSON file: " + e.getMessage());
        }

        return result;
    }
}
