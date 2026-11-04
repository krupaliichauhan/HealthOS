-- ============================================================================
-- HealthOS Desktop Application - MySQL 8.x Database Schema
-- MCA Academic Project - Personal Health and Wellness Tracking Ecosystem
-- ============================================================================

CREATE DATABASE IF NOT EXISTS healthos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE healthos;

-- 1. DATA SOURCES TABLE (Reference table for provenance)
CREATE TABLE IF NOT EXISTS data_sources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    source_code VARCHAR(50) NOT NULL UNIQUE,
    source_name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB;

-- 2. USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. USER PROFILES TABLE
CREATE TABLE IF NOT EXISTS user_profiles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    age INT DEFAULT 22,
    gender VARCHAR(30) DEFAULT 'PREFER_NOT_TO_SAY',
    height_cm DOUBLE DEFAULT 168.0,
    weight_kg DOUBLE DEFAULT 62.0,
    daily_step_goal INT DEFAULT 10000,
    daily_water_goal_ml INT DEFAULT 2500,
    daily_calorie_goal INT DEFAULT 2000,
    daily_sleep_goal_minutes INT DEFAULT 480,
    theme_preference VARCHAR(50) DEFAULT 'PASTEL_MINT',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. ACTIVITY LOGS TABLE
CREATE TABLE IF NOT EXISTS activity_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    steps INT DEFAULT 0,
    distance_km DOUBLE DEFAULT 0.0,
    active_minutes INT DEFAULT 0,
    calories_burned DOUBLE DEFAULT 0.0,
    log_date DATE NOT NULL,
    log_time TIME DEFAULT '12:00:00',
    data_source_id INT DEFAULT 1,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activity_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_activity_source FOREIGN KEY (data_source_id) REFERENCES data_sources(id)
) ENGINE=InnoDB;

-- 5. VITAL LOGS TABLE
CREATE TABLE IF NOT EXISTS vital_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    heart_rate_bpm INT,
    systolic_bp INT,
    diastolic_bp INT,
    blood_oxygen_spo2 DOUBLE,
    temperature_celsius DOUBLE,
    weight_kg DOUBLE,
    height_cm DOUBLE,
    bmi DOUBLE,
    log_timestamp TIMESTAMP NOT NULL,
    data_source_id INT DEFAULT 1,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vitals_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_vitals_source FOREIGN KEY (data_source_id) REFERENCES data_sources(id)
) ENGINE=InnoDB;

-- 6. SLEEP LOGS TABLE
CREATE TABLE IF NOT EXISTS sleep_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    sleep_start TIMESTAMP NOT NULL,
    sleep_end TIMESTAMP NOT NULL,
    duration_minutes INT NOT NULL,
    quality_rating INT NOT NULL CHECK (quality_rating BETWEEN 1 AND 5),
    sleep_phase_deep_min INT DEFAULT 0,
    sleep_phase_light_min INT DEFAULT 0,
    log_date DATE NOT NULL,
    data_source_id INT DEFAULT 1,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sleep_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_sleep_source FOREIGN KEY (data_source_id) REFERENCES data_sources(id)
) ENGINE=InnoDB;

-- 7. WEIGHT LOGS TABLE
CREATE TABLE IF NOT EXISTS weight_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    weight_kg DOUBLE NOT NULL,
    bmi DOUBLE,
    log_date DATE NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_weight_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. WATER LOGS TABLE
CREATE TABLE IF NOT EXISTS water_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount_ml INT NOT NULL,
    log_date DATE NOT NULL,
    log_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_water_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9. FOOD ITEMS TABLE
CREATE TABLE IF NOT EXISTS food_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    serving_size VARCHAR(50) NOT NULL,
    calories DOUBLE NOT NULL,
    protein_g DOUBLE NOT NULL,
    carbs_g DOUBLE NOT NULL,
    fat_g DOUBLE NOT NULL,
    fiber_g DOUBLE DEFAULT 0.0,
    is_system BOOLEAN DEFAULT TRUE,
    created_by_user_id INT,
    CONSTRAINT fk_food_user FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 10. MEALS TABLE
CREATE TABLE IF NOT EXISTS meals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    meal_type VARCHAR(30) NOT NULL,
    meal_date DATE NOT NULL,
    meal_time TIME DEFAULT '12:00:00',
    total_calories DOUBLE DEFAULT 0.0,
    total_protein DOUBLE DEFAULT 0.0,
    total_carbs DOUBLE DEFAULT 0.0,
    total_fat DOUBLE DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_meal_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 11. MEAL ITEMS TABLE
CREATE TABLE IF NOT EXISTS meal_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    meal_id INT NOT NULL,
    food_item_id INT NOT NULL,
    quantity DOUBLE DEFAULT 1.0,
    calories DOUBLE NOT NULL,
    protein_g DOUBLE NOT NULL,
    carbs_g DOUBLE NOT NULL,
    fat_g DOUBLE NOT NULL,
    CONSTRAINT fk_mealitem_meal FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    CONSTRAINT fk_mealitem_food FOREIGN KEY (food_item_id) REFERENCES food_items(id)
) ENGINE=InnoDB;

-- 12. HEALTH GOALS TABLE
CREATE TABLE IF NOT EXISTS health_goals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_type VARCHAR(50) NOT NULL,
    target_value DOUBLE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 13. HEALTH INSIGHTS TABLE
CREATE TABLE IF NOT EXISTS health_insights (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    insight_type VARCHAR(30) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_insight_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 14. NOTIFICATIONS TABLE
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    notification_type VARCHAR(50) DEFAULT 'INFO',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 15. REPORT HISTORY TABLE
CREATE TABLE IF NOT EXISTS report_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    report_title VARCHAR(150) NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;
