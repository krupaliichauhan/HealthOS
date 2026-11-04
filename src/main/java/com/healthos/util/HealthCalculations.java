package com.healthos.util;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * HealthOS Core Health & Wellness Calculations
 * Pure calculation methods for personal tracking and statistics.
 * Not intended for medical diagnosis.
 */
public final class HealthCalculations {

    private HealthCalculations() {}

    /**
     * Calculates Body Mass Index (BMI).
     * BMI = weight (kg) / (height (m))^2
     *
     * @param weightKg Weight in kilograms
     * @param heightCm Height in centimeters
     * @return BMI rounded to 2 decimal places, or 0.0 if invalid input
     */
    public static double calculateBmi(double weightKg, double heightCm) {
        if (weightKg <= 0 || heightCm <= 0) {
            return 0.0;
        }
        double heightMeters = heightCm / 100.0;
        double bmi = weightKg / (heightMeters * heightMeters);
        return Math.round(bmi * 100.0) / 100.0;
    }

    /**
     * Categorizes a BMI value into personal tracking ranges.
     */
    public static String getBmiCategory(double bmi) {
        if (bmi <= 0) {
            return "Not Recorded";
        } else if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal Weight";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    /**
     * Percentage progress toward a daily goal.
     * Progress (%) = (current / target) * 100
     */
    public static double calculateGoalProgress(double current, double target) {
        if (target <= 0) {
            return 0.0;
        }
        double percentage = (current / target) * 100.0;
        return Math.min(Math.round(percentage * 10.0) / 10.0, 999.0);
    }

    /**
     * Percentage change between two historical values.
     * ((current - previous) / previous) * 100
     */
    public static double calculatePercentageChange(double current, double previous) {
        if (previous == 0.0) {
            return current > 0 ? 100.0 : 0.0;
        }
        double change = ((current - previous) / previous) * 100.0;
        return Math.round(change * 10.0) / 10.0;
    }

    /**
     * Calculates sleep duration in minutes, handling sessions crossing midnight seamlessly.
     */
    public static int calculateSleepDurationMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        Duration duration = Duration.between(start, end);
        long minutes = duration.toMinutes();
        if (minutes < 0) {
            // End time is technically on the next calendar day
            minutes += 24 * 60;
        }
        return (int) Math.max(0, minutes);
    }

    /**
     * Estimates calories burned based on exercise type, duration, and user body weight.
     * Uses standard MET (Metabolic Equivalent of Task) values.
     */
    public static double estimateExerciseCalories(String activityType, int durationMinutes, double weightKg) {
        if (durationMinutes <= 0 || weightKg <= 0) {
            return 0.0;
        }
        double met;
        switch (activityType.toUpperCase()) {
            case "RUNNING": met = 9.8; break;
            case "CYCLING": met = 7.5; break;
            case "SWIMMING": met = 8.0; break;
            case "HIIT": met = 8.5; break;
            case "STRENGTH": met = 5.0; break;
            case "YOGA": met = 3.2; break;
            case "WALKING":
            default: met = 3.8; break;
        }
        // Calories = MET * weight (kg) * (duration / 60)
        double calories = met * weightKg * (durationMinutes / 60.0);
        return Math.round(calories * 10.0) / 10.0;
    }

    /**
     * Formats minutes into human-friendly "Xh Ym" string (e.g. 7h 45m).
     */
    public static String formatDurationHoursMinutes(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return hours + "h " + (mins < 10 ? "0" : "") + mins + "m";
    }
}
