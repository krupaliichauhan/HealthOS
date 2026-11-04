package com.healthos;

import com.healthos.util.HealthCalculations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HealthOS Health Calculations Test Suite")
class HealthCalculationsTest {

    @Test
    @DisplayName("Should correctly calculate BMI and classify into proper category")
    void testCalculateBmi() {
        // Height 165 cm, Weight 58.5 kg -> BMI ~ 21.49 (Normal)
        double bmi = HealthCalculations.calculateBmi(58.5, 165.0);
        assertEquals(21.49, bmi, 0.05);
        assertEquals("Normal Weight", HealthCalculations.getBmiCategory(bmi));

        // Underweight case
        double lowBmi = HealthCalculations.calculateBmi(45.0, 170.0);
        assertEquals("Underweight", HealthCalculations.getBmiCategory(lowBmi));

        // Overweight case
        double highBmi = HealthCalculations.calculateBmi(82.0, 170.0);
        assertEquals("Overweight", HealthCalculations.getBmiCategory(highBmi));

        // Obese case
        double obeseBmi = HealthCalculations.calculateBmi(105.0, 170.0);
        assertEquals("Obese", HealthCalculations.getBmiCategory(obeseBmi));

        // Zero / Negative boundary handling
        assertEquals(0.0, HealthCalculations.calculateBmi(0, 170.0));
        assertEquals(0.0, HealthCalculations.calculateBmi(60, 0));
    }

    @Test
    @DisplayName("Should correctly calculate goal progress percentage and handle edge cases")
    void testGoalProgress() {
        assertEquals(50.0, HealthCalculations.calculateGoalProgress(5000, 10000), 0.1);
        assertEquals(100.0, HealthCalculations.calculateGoalProgress(10000, 10000), 0.1);
        assertEquals(120.0, HealthCalculations.calculateGoalProgress(12000, 10000), 0.1);
        assertEquals(0.0, HealthCalculations.calculateGoalProgress(0, 10000), 0.1);
        assertEquals(0.0, HealthCalculations.calculateGoalProgress(5000, 0), 0.1);
    }

    @Test
    @DisplayName("Should correctly calculate percentage change between periods")
    void testPercentageChange() {
        // 10000 vs 8000 -> +25%
        assertEquals(25.0, HealthCalculations.calculatePercentageChange(10000, 8000), 0.1);
        // 7000 vs 10000 -> -30%
        assertEquals(-30.0, HealthCalculations.calculatePercentageChange(7000, 10000), 0.1);
        // Zero handling
        assertEquals(100.0, HealthCalculations.calculatePercentageChange(5000, 0), 0.1);
        assertEquals(0.0, HealthCalculations.calculatePercentageChange(0, 0), 0.1);
    }

    @Test
    @DisplayName("Should correctly compute sleep duration across midnight rollover")
    void testSleepDurationAcrossMidnight() {
        LocalDateTime start = LocalDateTime.of(2026, 11, 3, 23, 15);
        LocalDateTime end = LocalDateTime.of(2026, 11, 4, 7, 0);

        int durationMin = HealthCalculations.calculateSleepDurationMinutes(start, end);
        // 465 minutes = 7 hours 45 minutes
        assertEquals(465, durationMin);
        assertEquals("7h 45m", HealthCalculations.formatDurationHoursMinutes(durationMin));
    }

    @Test
    @DisplayName("Should estimate exercise calories using MET calculations")
    void testExerciseCalories() {
        // Running 60 mins at 60kg: MET 9.8 * 60 * 1 = ~588 kcal
        double runningCal = HealthCalculations.estimateExerciseCalories("RUNNING", 60, 60.0);
        assertTrue(runningCal > 550 && runningCal < 620);

        // Walking 60 mins at 60kg: MET 3.8 * 60 * 1 = ~228 kcal
        double walkingCal = HealthCalculations.estimateExerciseCalories("WALKING", 60, 60.0);
        assertTrue(walkingCal > 200 && walkingCal < 250);
    }
}
