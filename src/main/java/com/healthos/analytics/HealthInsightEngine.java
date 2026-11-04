package com.healthos.analytics;

import com.healthos.dao.ActivityDao;
import com.healthos.dao.InsightDao;
import com.healthos.dao.SleepDao;
import com.healthos.dao.VitalDao;
import com.healthos.dao.WaterDao;
import com.healthos.model.HealthInsight;
import com.healthos.model.SleepLog;
import com.healthos.model.UserProfile;
import com.healthos.model.VitalLog;
import com.healthos.util.HealthCalculations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HealthOS Smart Rule-Based Insight Engine.
 * Evaluates moving averages, goal compliance, and longitudinal trends
 * from stored historical data to generate actionable wellness insights.
 * Note: Purely wellness recommendations; does not make medical diagnoses.
 */
public class HealthInsightEngine {

    private final ActivityDao activityDao = new ActivityDao();
    private final WaterDao waterDao = new WaterDao();
    private final SleepDao sleepDao = new SleepDao();
    private final VitalDao vitalDao = new VitalDao();
    private final InsightDao insightDao = new InsightDao();

    /**
     * Generates fresh, intelligent insights based on the user's historical records.
     */
    public List<HealthInsight> generateInsights(int userId, UserProfile profile) {
        List<HealthInsight> list = new ArrayList<>();

        // 1. Activity Trend Analysis: Compare Last 7 Days vs Prior 7 Days
        evaluateActivityTrends(userId, profile, list);

        // 2. Hydration Streak & Compliance
        evaluateHydrationTrends(userId, profile, list);

        // 3. Sleep Regularity & Quality
        evaluateSleepTrends(userId, profile, list);

        // 4. Vitals & Weight Stability
        evaluateVitalTrends(userId, profile, list);

        // Persist newly generated insights to database if needed
        for (HealthInsight in : list) {
            insightDao.addInsight(in);
        }

        return list;
    }

    private void evaluateActivityTrends(int userId, UserProfile profile, List<HealthInsight> list) {
        Map<LocalDate, Integer> past14Days = activityDao.getDailyStepsForPastDays(userId, 14);
        int targetGoal = profile != null ? profile.getDailyStepGoal() : 10000;

        int currentWeekSum = 0;
        int previousWeekSum = 0;
        int daysCurrentMet = 0;

        LocalDate today = LocalDate.now();
        for (Map.Entry<LocalDate, Integer> entry : past14Days.entrySet()) {
            LocalDate d = entry.getKey();
            int steps = entry.getValue();
            if (d.isAfter(today.minusDays(7))) {
                currentWeekSum += steps;
                if (steps >= targetGoal) daysCurrentMet++;
            } else {
                previousWeekSum += steps;
            }
        }

        double currentWeekAvg = currentWeekSum / 7.0;
        double previousWeekAvg = previousWeekSum / 7.0;

        if (previousWeekAvg > 0) {
            double percentChange = HealthCalculations.calculatePercentageChange(currentWeekAvg, previousWeekAvg);
            if (percentChange >= 8.0) {
                list.add(new HealthInsight(0, userId, "ACTIVITY",
                        "Weekly Activity Surged! 🏃‍♀️",
                        "Your average daily steps (" + (int)currentWeekAvg + ") increased by " + Math.abs(percentChange) +
                                "% compared to last week. Wonderful physical momentum!",
                        "ACHIEVEMENT", false, LocalDateTime.now()));
            } else if (percentChange <= -12.0) {
                list.add(new HealthInsight(0, userId, "ACTIVITY",
                        "Step Pace Slowed Down 👟",
                        "Your daily steps dipped by " + Math.abs(percentChange) +
                                "% this week. Try taking a short 15-minute rejuvenating walk during study breaks.",
                        "GENTLE_REMINDER", false, LocalDateTime.now()));
            } else {
                list.add(new HealthInsight(0, userId, "ACTIVITY",
                        "Consistent Activity Baseline ✨",
                        "You maintained a steady active pace this week, averaging " + (int)currentWeekAvg +
                                " steps daily. Steady habits create long-term health.",
                        "POSITIVE_TREND", false, LocalDateTime.now()));
            }
        }

        if (daysCurrentMet >= 4) {
            list.add(new HealthInsight(0, userId, "ACTIVITY",
                    "Daily Step Goal Achieved! 🏆",
                    "You reached your " + targetGoal + " daily steps target on " + daysCurrentMet +
                            " days over the past week. Stellar dedication!",
                    "ACHIEVEMENT", false, LocalDateTime.now()));
        }
    }

    private void evaluateHydrationTrends(int userId, UserProfile profile, List<HealthInsight> list) {
        int waterGoal = profile != null ? profile.getDailyWaterGoalMl() : 2500;
        Map<LocalDate, Integer> waterData = waterDao.getDailyWaterForPastDays(userId, 7);

        int daysMet = 0;
        int totalMl = 0;
        for (int ml : waterData.values()) {
            totalMl += ml;
            if (ml >= (waterGoal * 0.9)) { // 90%+ meets goal
                daysMet++;
            }
        }

        double avgMl = totalMl / 7.0;
        if (daysMet >= 5) {
            list.add(new HealthInsight(0, userId, "HYDRATION",
                    "Hydration Champion! 💧",
                    "You met your hydration target (" + waterGoal + " ml) on " + daysMet +
                            " out of 7 days! Ample hydration supports cognitive clarity and energy.",
                    "POSITIVE_TREND", false, LocalDateTime.now()));
        } else if (avgMl < (waterGoal * 0.7)) {
            list.add(new HealthInsight(0, userId, "HYDRATION",
                    "Sip A Little More Water 🌊",
                    "Your weekly average was " + (int)avgMl + " ml, which is below your " + waterGoal +
                            " ml target. Keep a friendly pastel water bottle near your desk!",
                    "TIP", false, LocalDateTime.now()));
        }
    }

    private void evaluateSleepTrends(int userId, UserProfile profile, List<HealthInsight> list) {
        Map<LocalDate, Double> sleepData = sleepDao.getDailySleepHoursForPastDays(userId, 7);
        double totalHours = 0.0;
        int count = 0;
        for (double h : sleepData.values()) {
            if (h > 0) {
                totalHours += h;
                count++;
            }
        }

        if (count > 0) {
            double avgSleep = totalHours / count;
            if (avgSleep >= 7.5 && avgSleep <= 9.0) {
                list.add(new HealthInsight(0, userId, "SLEEP",
                        "Restorative Sleep Routine 🌙",
                        "You averaged " + Math.round(avgSleep * 10.0)/10.0 +
                                " hours of sleep nightly. Excellent sleep hygiene powers your mood and physical recovery.",
                        "POSITIVE_TREND", false, LocalDateTime.now()));
            } else if (avgSleep < 6.5) {
                list.add(new HealthInsight(0, userId, "SLEEP",
                        "Sleep Deficit Detected 😴",
                        "Your weekly sleep averaged " + Math.round(avgSleep * 10.0)/10.0 +
                                " hours. Prioritizing at least 7.5 hours tonight can replenish mental fatigue.",
                        "GENTLE_REMINDER", false, LocalDateTime.now()));
            }
        }
    }

    private void evaluateVitalTrends(int userId, UserProfile profile, List<HealthInsight> list) {
        VitalLog latest = vitalDao.getLatestVital(userId);
        if (latest != null) {
            if (latest.getHeartRateBpm() != null) {
                int hr = latest.getHeartRateBpm();
                if (hr >= 60 && hr <= 85) {
                    list.add(new HealthInsight(0, userId, "VITALS",
                            "Healthy Resting Heart Rate ❤️",
                            "Your latest resting heart rate of " + hr + " BPM indicates healthy cardiovascular recovery.",
                            "TIP", false, LocalDateTime.now()));
                }
            }

            if (latest.getBmi() != null && latest.getBmi() > 0) {
                double bmi = latest.getBmi();
                String category = HealthCalculations.getBmiCategory(bmi);
                if (category.equals("Normal Weight")) {
                    list.add(new HealthInsight(0, userId, "VITALS",
                            "Optimal BMI Range ✨",
                            "Your Body Mass Index is " + bmi + " (" + category + "). Keep nurturing your body with balanced meals and daily motion.",
                            "POSITIVE_TREND", false, LocalDateTime.now()));
                }
            }
        }
    }
}
