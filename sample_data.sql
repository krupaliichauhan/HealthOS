-- ============================================================================
-- HealthOS Desktop Application - Seed / Sample Data Script
-- Populates reference tables, a demo user, 20 food items, and 14 days of history
-- Compatible with both MySQL 8.x and embedded H2 fallback engine
-- ============================================================================

-- 1. Reference Data: Data Sources
INSERT INTO data_sources (id, source_code, source_name, description) VALUES
(1, 'MANUAL_ENTRY', 'Manual User Entry', 'Directly typed by user in HealthOS Desktop UI'),
(2, 'CSV_IMPORT', 'CSV File Import', 'Imported via CSV file parser'),
(3, 'JSON_IMPORT', 'JSON File Import', 'Imported via JSON health data standard'),
(4, 'WEARABLE_SYNC', 'Wearable Companion Sync', 'Simulated wearable smartwatch tracker');

-- 2. Demo User: fairy@healthos.app (Password: "fairy123")
-- Genuine BCrypt hash for "fairy123"
INSERT INTO users (id, email, password_hash, full_name, created_at) VALUES
(1, 'fairy@healthos.app', '$2a$10$zQWx5qmKopYaUX9EMQqRMuCE/SAXnOY3RnXon/jm8U74w2QsQTwEe', 'Fairy Soni', CURRENT_TIMESTAMP),
(2, 'demo@healthos.app', '$2a$10$zQWx5qmKopYaUX9EMQqRMuCE/SAXnOY3RnXon/jm8U74w2QsQTwEe', 'Alex Morgan', CURRENT_TIMESTAMP);

-- 3. User Profiles
INSERT INTO user_profiles (id, user_id, age, gender, height_cm, weight_kg, daily_step_goal, daily_water_goal_ml, daily_calorie_goal, daily_sleep_goal_minutes, theme_preference) VALUES
(1, 1, 22, 'FEMALE', 165.0, 58.5, 10000, 2500, 2100, 480, 'PASTEL_MINT'),
(2, 2, 25, 'MALE', 178.0, 72.0, 8000, 2800, 2300, 450, 'PASTEL_PEACH');

-- 4. Pre-populated Wholesome Food Items
INSERT INTO food_items (id, name, category, serving_size, calories, protein_g, carbs_g, fat_g, fiber_g, is_system) VALUES
(1, 'Rolled Oatmeal with Berries', 'GRAIN', '1 bowl (200g)', 220, 7.0, 38.0, 3.5, 5.0, TRUE),
(2, 'Greek Yogurt (Plain 0%)', 'DAIRY', '1 cup (170g)', 100, 17.0, 6.0, 0.5, 0.0, TRUE),
(3, 'Ripe Green Apple', 'FRUIT', '1 medium (180g)', 95, 0.5, 25.0, 0.3, 4.4, TRUE),
(4, 'Fresh Banana', 'FRUIT', '1 medium (118g)', 105, 1.3, 27.0, 0.3, 3.1, TRUE),
(5, 'Hass Avocado', 'FRUIT', '1/2 avocado (100g)', 160, 2.0, 8.5, 14.7, 6.7, TRUE),
(6, 'Grilled Chicken Breast', 'PROTEIN', '1 fillet (150g)', 240, 46.0, 0.0, 5.0, 0.0, TRUE),
(7, 'Wild Atlantic Salmon', 'PROTEIN', '1 fillet (150g)', 280, 34.0, 0.0, 15.0, 0.0, TRUE),
(8, 'Steamed Brown Rice', 'GRAIN', '1 cup (195g)', 216, 5.0, 45.0, 1.8, 3.5, TRUE),
(9, 'Quinoa Salad with Olive Oil', 'GRAIN', '1 bowl (185g)', 222, 8.1, 39.4, 3.6, 5.2, TRUE),
(10, 'Fresh Steamed Broccoli', 'VEGETABLE', '1 cup (91g)', 31, 2.6, 6.0, 0.3, 2.4, TRUE),
(11, 'Boiled Organic Eggs', 'PROTEIN', '2 large (100g)', 143, 12.6, 0.7, 9.5, 0.0, TRUE),
(12, 'Mixed Almonds and Walnuts', 'SNACK', '1 handful (30g)', 185, 5.0, 6.0, 16.0, 3.0, TRUE),
(13, 'Baby Spinach with Lemon', 'VEGETABLE', '2 cups (60g)', 14, 1.7, 2.2, 0.2, 1.3, TRUE),
(14, 'Whole Wheat Multigrain Toast', 'GRAIN', '2 slices (64g)', 160, 8.0, 28.0, 2.0, 4.0, TRUE),
(15, 'Matcha Green Tea', 'BEVERAGE', '1 mug (250ml)', 25, 1.0, 3.0, 0.5, 0.5, TRUE),
(16, 'Chia Seed Almond Pudding', 'SNACK', '1 jar (150g)', 170, 5.5, 18.0, 8.0, 6.0, TRUE),
(17, 'Grilled Cottage Cheese (Paneer)', 'PROTEIN', '100g slice', 265, 18.3, 3.4, 20.8, 0.0, TRUE),
(18, 'Fresh Orange Juice', 'BEVERAGE', '1 glass (240ml)', 110, 2.0, 26.0, 0.5, 0.5, TRUE),
(19, 'Sweet Sweet Potato (Baked)', 'VEGETABLE', '1 medium (130g)', 112, 2.0, 26.0, 0.1, 3.9, TRUE),
(20, 'Lentil Soup (Dal)', 'PROTEIN', '1 bowl (200g)', 180, 12.0, 28.0, 3.0, 8.0, TRUE);

-- 5. Health Goals for User 1
INSERT INTO health_goals (id, user_id, goal_type, target_value, start_date, end_date, status) VALUES
(1, 1, 'DAILY_STEPS', 10000, CURRENT_DATE, NULL, 'ACTIVE'),
(2, 1, 'DAILY_WATER', 2500, CURRENT_DATE, NULL, 'ACTIVE'),
(3, 1, 'DAILY_CALORIES', 2000, CURRENT_DATE, NULL, 'ACTIVE'),
(4, 1, 'DAILY_SLEEP', 480, CURRENT_DATE, NULL, 'ACTIVE'),
(5, 1, 'TARGET_WEIGHT', 57.0, CURRENT_DATE, NULL, 'ACTIVE');

-- 6. Activity Logs (User 1)
INSERT INTO activity_logs (user_id, activity_type, steps, distance_km, active_minutes, calories_burned, log_date, log_time, data_source_id, notes) VALUES
(1, 'WALKING', 8450, 5.8, 55, 340, CURRENT_DATE, '08:30:00', 1, 'Morning brisk walk in garden'),
(1, 'RUNNING', 10250, 7.2, 60, 480, CURRENT_DATE, '07:15:00', 1, 'Interval jogging in park'),
(1, 'YOGA', 6800, 4.2, 45, 230, CURRENT_DATE, '09:00:00', 1, 'Vinyasa flow session'),
(1, 'CYCLING', 11400, 10.5, 75, 520, CURRENT_DATE, '17:30:00', 1, 'Evening sunset bike ride'),
(1, 'WALKING', 9800, 6.7, 65, 390, CURRENT_DATE, '08:00:00', 1, 'Campus walk between classes'),
(1, 'STRENGTH', 7600, 5.1, 50, 310, CURRENT_DATE, '18:15:00', 1, 'Core and posture strengthening'),
(1, 'WALKING', 9250, 6.4, 58, 375, CURRENT_DATE, '08:45:00', 1, 'Morning energizing stroll');

-- 7. Vital Logs (User 1)
INSERT INTO vital_logs (user_id, heart_rate_bpm, systolic_bp, diastolic_bp, blood_oxygen_spo2, temperature_celsius, weight_kg, height_cm, bmi, log_timestamp, data_source_id, notes) VALUES
(1, 72, 118, 78, 98.5, 36.6, 59.2, 165.0, 21.74, CURRENT_TIMESTAMP, 1, 'Normal resting vitals'),
(1, 70, 116, 76, 99.0, 36.5, 59.0, 165.0, 21.67, CURRENT_TIMESTAMP, 1, 'Feeling energetic'),
(1, 74, 120, 80, 98.0, 36.7, 58.8, 165.0, 21.60, CURRENT_TIMESTAMP, 1, 'Post light yoga'),
(1, 68, 115, 75, 99.0, 36.5, 58.7, 165.0, 21.56, CURRENT_TIMESTAMP, 1, 'Calm and steady'),
(1, 71, 117, 77, 98.5, 36.6, 58.6, 165.0, 21.52, CURRENT_TIMESTAMP, 1, 'Mid-day check'),
(1, 69, 116, 75, 99.0, 36.5, 58.5, 165.0, 21.49, CURRENT_TIMESTAMP, 1, 'Good hydration level'),
(1, 70, 118, 76, 98.8, 36.6, 58.5, 165.0, 21.49, CURRENT_TIMESTAMP, 1, 'Morning baseline measurement');

-- 8. Sleep Logs
INSERT INTO sleep_logs (user_id, sleep_start, sleep_end, duration_minutes, quality_rating, sleep_phase_deep_min, sleep_phase_light_min, log_date, data_source_id, notes) VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 465, 4, 110, 355, CURRENT_DATE, 1, 'Fell asleep quickly'),
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 480, 5, 135, 345, CURRENT_DATE, 1, 'Deep restful night'),
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 465, 4, 105, 360, CURRENT_DATE, 1, 'Woke up refreshed'),
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 450, 4, 100, 350, CURRENT_DATE, 1, 'Slightly early wake up'),
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 510, 5, 140, 370, CURRENT_DATE, 1, 'Excellent recovery sleep'),
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 490, 5, 125, 365, CURRENT_DATE, 1, 'Dreamy, peaceful sleep');

-- 9. Water Intake Logs
INSERT INTO water_logs (user_id, amount_ml, log_date, log_time) VALUES
(1, 350, CURRENT_DATE, '07:30:00'),
(1, 500, CURRENT_DATE, '10:15:00'),
(1, 400, CURRENT_DATE, '13:00:00'),
(1, 500, CURRENT_DATE, '16:00:00'),
(1, 450, CURRENT_DATE, '19:30:00'),
(1, 300, CURRENT_DATE, '21:30:00');

-- 10. Meals and Meal Items (User 1)
INSERT INTO meals (id, user_id, meal_type, meal_date, meal_time, total_calories, total_protein, total_carbs, total_fat) VALUES
(1, 1, 'BREAKFAST', CURRENT_DATE, '08:15:00', 320, 24.0, 44.0, 4.0),
(2, 1, 'LUNCH', CURRENT_DATE, '13:10:00', 636, 59.0, 45.0, 20.0),
(3, 1, 'SNACK', CURRENT_DATE, '16:45:00', 185, 5.0, 6.0, 16.0);

INSERT INTO meal_items (meal_id, food_item_id, quantity, calories, protein_g, carbs_g, fat_g) VALUES
(1, 1, 1.0, 220, 7.0, 38.0, 3.5),
(1, 2, 1.0, 100, 17.0, 6.0, 0.5),
(2, 6, 1.0, 240, 46.0, 0.0, 5.0),
(2, 8, 1.0, 216, 5.0, 45.0, 1.8),
(2, 12, 1.0, 180, 8.0, 0.0, 13.2),
(3, 12, 1.0, 185, 5.0, 6.0, 16.0);

-- 11. Smart Health Insights
INSERT INTO health_insights (user_id, category, title, message, insight_type, is_read) VALUES
(1, 'ACTIVITY', 'Weekly Step Goal Surpassed! 🎉', 'You have averaged 9,078 steps over the last 7 days, which is 14% higher than last week. Excellent consistency!', 'ACHIEVEMENT', FALSE),
(1, 'HYDRATION', 'Optimal Hydration Streak 💧', 'You consistently completed over 2,200 ml of water 6 days in a row. Staying hydrated improves your daily focus and metabolic health.', 'POSITIVE_TREND', FALSE),
(1, 'SLEEP', 'Restful Sleep Routine 🌙', 'Your average sleep duration is 7.9 hours with a 4.7/5 quality rating. Consistent bedtimes contribute to this great score.', 'POSITIVE_TREND', FALSE),
(1, 'VITALS', 'Stable Blood Pressure & BMI ✨', 'Your resting heart rate is 70 BPM and blood pressure is in the optimal normal range (118/76 mmHg). Keep up your balanced lifestyle!', 'TIP', FALSE);

-- 12. Notifications
INSERT INTO notifications (user_id, title, content, notification_type, is_read) VALUES
(1, 'Welcome to HealthOS! 🌸', 'Your personal wellness sanctuary is ready. Start tracking your activities, nutrition, and daily sleep to unlock smart insights.', 'WELCOME', FALSE),
(1, 'Hydration Check 💧', 'Afternoon reminder: Take a pause and sip a refreshing glass of water to meet your daily 2,500 ml target.', 'REMINDER', FALSE);
