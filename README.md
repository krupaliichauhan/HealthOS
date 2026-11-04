# HealthOS Desktop Application 🌸✨
> **A Modern Personal Health & Wellness Tracking Ecosystem**  
> *Developed for Master of Computer Applications (MCA) Academic Evaluation*  
> Built with **Java 21**, **JavaFX**, **MySQL 8 (JDBC)**, **OpenPDF**, **Jackson**, **Apache Commons CSV**, **BCrypt**, and **JUnit 5**.

---

## 1. Executive Summary & Design Philosophy

**HealthOS** is not a generic hospital management or administrative CRUD system. It is a modern **personal wellness journal and proactive health companion**, combining **75% clean Apple Health-inspired minimalism** with **25% cute, soft pastel visual aesthetics**.

### Visual Identity & Palette
- **Warm Off-White Background**: `#F8FAF8`
- **Soft Mint**: `#DDF3E8` (Accent `#A8D5BE`, Dark `#225945`)
- **Pastel Teal**: `#78BDB1` (Brand Button `#64B1A4`)
- **Lavender**: `#E9E3F7` (Accent `#5E4694`)
- **Soft Blue**: `#E2EEFA` (Accent `#3FA8EB`)
- **Pastel Peach**: `#FCE8DD` (Accent `#F5A882`)
- **Dark Text**: `#34433E` (Subtitles `#6C7D76`)

### Custom Vector Illustrations
HealthOS features custom, crisp vector illustrations across all sections:
1. **Heart Character (`heart_cute.svg`)**: Blushing smiling heart with resting ECG pulse for Vitals & Heart Rate.
2. **Sleepy Moon (`moon_sleepy.svg`)**: Cozy crescent moon with nightcap, closed lashes, and stars for Sleep Tracking.
3. **Joyful Water Droplet (`water_droplet.svg`)**: Smiling droplet with glass shine for Hydration.
4. **Retro Sneaker (`activity_sneaker.svg`)**: Cheerful runner sneaker with speed lines for Activity & Steps.
5. **Nutrition Basket (`nutrition_basket.svg`)**: Cute avocado and rosy apple for Meal Tracking.
6. **Wellness Plant (`wellness_plant.svg`)**: Blooming potted plant for Health Goals and Habit Progress.
7. **Welcoming Wellness (`login_wellness.svg`)**: Serene meditation illustration on Authentication screen.
8. **Achievement Trophy (`trophy_achievement.svg`)**: Golden celebration cup for PDF Reports & Milestones.
9. **Sleeping Cloud (`empty_state.svg`)**: Cozy cloud with magnifying glass for Ingestion & Empty states.

---

## 2. Core Functional Modules

| Module | Features & Capabilities |
| :--- | :--- |
| **A. Authentication & Security** | BCrypt password hashing, password visibility toggle, registration, login form validation, and quick 1-click viva demo fill (`fairy@healthos.app` / `fairy123`). |
| **B. Main Dashboard** | Personalized greeting ("Good morning, Fairy! ✨"), daily goal progress bars, 5 metric summary cards, 7-day activity bar chart, sleep trend line chart, recent activity feed, and instant quick action buttons. |
| **C. Activity Tracker** | Logs daily steps, distance (km), duration, and calories burned for 8 workout types (Walking, Running, Cycling, Yoga, Swimming, HIIT, Strength, Other). Includes MET-based calorie estimation, daily step goal progress, and 7-day trend chart. |
| **D. Vitals & Biometrics** | Heart rate (BPM), blood pressure (Systolic/Diastolic mmHg), SpO2 (%), body temperature (°C), weight (kg), height (cm), auto-calculated BMI with categoric classification badges (Underweight, Normal, Overweight, Obese), and longitudinal vitals charts. |
| **E. Sleep Tracker** | Records bedtime and wake-up time with **proper midnight rollover handling**, auto-calculated duration in hours & minutes, quality rating (1–5 ★), deep vs light sleep phases, and 7-day sleep duration chart. |
| **F. Water Tracker** | Visual hydration progress bar, quick presets (+200ml, +250ml, +500ml, +750ml, custom ml), remaining water goal calculation, and historical hydration table. |
| **G. Nutrition & Meals** | Searchable built-in food library (20+ wholesome items), categorizes Breakfast/Lunch/Dinner/Snacks, tracks calories, protein, carbs, and fats, and compares against daily macronutrient budget. |
| **H. Health Analytics** | Multi-dimensional analytics view with 7, 14, and 30-day range filters, moving average indicators, activity bar chart, sleep duration line chart, hydration chart, and macronutrient distribution pie chart. |
| **I. Smart Health Insights** | Rule-based insight engine calculating 7-day moving averages and percentage changes against prior periods to deliver actionable positive reinforcement and gentle sleep/hydration reminders. |
| **J. PDF Health Reports** | Exports clean, Apple Health-inspired wellness reports via **OpenPDF** with user profile header, date range selection, metric summaries, data tables, and custom file save dialog. |
| **K. CSV & JSON Ingestion** | Batch imports activity logs from CSV (via **Apache Commons CSV**) and vitals from JSON (via **Jackson**), with comprehensive validation and data source provenance tracking. |
| **L. Settings & Database** | User profile editor, daily target goal configuration, live MySQL 8 connection test and credential switcher, and logout. |

---

## 3. Database Architecture (MySQL 8.x)

HealthOS uses a fully normalized, relational database named `healthos` with 15 tables, foreign keys, timestamps, and cascading delete rules:

1. `data_sources` — Provenance tracking (`MANUAL_ENTRY`, `CSV_IMPORT`, `JSON_IMPORT`, `WEARABLE_SYNC`).
2. `users` — User credentials with BCrypt hashes.
3. `user_profiles` — Biometric baselines, daily goals, and theme preferences.
4. `activity_logs` — Steps, distance, duration, and calories burned.
5. `vital_logs` — Heart rate, BP, SpO2, body temperature, weight, height, and BMI.
6. `sleep_logs` — Sleep start, sleep end, duration, and quality ratings.
7. `weight_logs` — Weight tracking logs over time.
8. `water_logs` — Daily hydration entries with timestamps.
9. `food_items` — Nutrition catalog with calories, protein, carbs, fats, and fiber.
10. `meals` — Daily meal aggregations (Breakfast, Lunch, Dinner, Snack).
11. `meal_items` — Junction table linking meals to food items with quantities.
12. `health_goals` — Active and achieved health targets.
13. `health_insights` — Generated rule-based insight cards.
14. `notifications` — System reminders and wellness alerts.
15. `report_history` — Audit log of exported PDF reports.

### Zero-Crash Viva & Demo Architecture
To guarantee that the application **never crashes during academic project demonstrations or viva evaluations**:
- The application connects to **MySQL 8** using official JDBC (`com.mysql.cj.jdbc.Driver`).
- If MySQL is offline or local credentials differ, `DatabaseConnection` gracefully activates its **embedded resilient database mode** (using embedded H2 in MySQL compatibility mode) and initializes the schema automatically.
- Users can test and connect their local MySQL credentials anytime via **Preferences & Settings** without restarting the application!

---

## 4. Setup & Running Instructions on Windows

### Prerequisites
- **Java Development Kit (JDK) 21** or higher.
- **Apache Maven 3.9+** (already pre-configured in project with `mvnw.cmd`).
- *(Optional)* **MySQL Server 8.0 / 8.4** running on `localhost:3306`.

### Option A: Launch using the Standalone Executable Fat JAR (Fastest)
The project includes a pre-packaged uber-JAR containing JavaFX 21, MySQL Connector/J, and all dependencies:
```cmd
cd C:\Users\admin\.gemini\antigravity\scratch\healthos
run.bat
```
*Or directly:*
```cmd
java -jar target\healthos-desktop-1.0.0.jar
```

### Option B: Run with Maven
```cmd
cd C:\Users\admin\.gemini\antigravity\scratch\healthos
mvnw.cmd javafx:run
```

### Option C: Import into IntelliJ IDEA on Windows
1. Open **IntelliJ IDEA**.
2. Select **File → Open...** and navigate to:  
   `C:\Users\admin\.gemini\antigravity\scratch\healthos`
3. Click **Open as Project** and choose **Maven Project**.
4. Allow IntelliJ to sync the `pom.xml`.
5. Set Project SDK to **Java 21** in **File → Project Structure → Project**.
6. In Project Explorer, locate `src/main/java/com/healthos/Main.java`.
7. Right-click `Main.java` and select **Run 'Main.main()'**.

---

## 5. MySQL Database Configuration (Optional)

If you wish to run HealthOS with your local MySQL 8 database service:
1. Open MySQL Command Line Client or MySQL Workbench.
2. Execute the schema script:
   ```cmd
   mysql -u root -p < src\main\resources\com\healthos\db\schema.sql
   mysql -u root -p < src\main\resources\com\healthos\db\sample_data.sql
   ```
3. Edit `src/main/resources/com/healthos/db/db.properties` with your credentials:
   ```properties
   db.host=localhost
   db.port=3306
   db.name=healthos
   db.user=root
   db.password=fairysoni
   ```
4. Alternatively, open **HealthOS → Preferences & Settings → Database Connectivity & Engine Manager**, enter your host, username, and password, and click **Test & Connect to MySQL 8**.

---

## 6. Pre-Loaded Test Accounts for Demo
- **User 1 (Default Demo Account)**:
  - **Email**: `fairy@healthos.app`
  - **Password**: `fairy123`
  - *Loaded with 14 days of realistic activity, vitals, sleep, water, and meal logs.*
- **User 2**:
  - **Email**: `demo@healthos.app`
  - **Password**: `demo123`
- **New Registration**:
  - Click **"Create One"** on the login screen to register an account. Passwords will be securely hashed with BCrypt.

---

## 7. Running Unit Tests

Run the automated test suite with JUnit 5:
```cmd
cd C:\Users\admin\.gemini\antigravity\scratch\healthos
mvnw.cmd test
```
**Test Coverage Includes:**
- Body Mass Index (BMI) calculation & categorical ranges.
- Goal completion percentage & zero-division handling.
- Percentage change comparison across historical windows.
- Sleep duration calculation handling midnight rollover (e.g. 23:15 to 07:00).
- MET-based active calorie calculation across workout types.
- BCrypt salt hashing and password verification.

---

## 8. Package & Architectural Structure

```
C:\Users\admin\.gemini\antigravity\scratch\healthos\
├── pom.xml                                 # Maven Project Object Model
├── mvnw.cmd / mvnw                         # Maven Wrapper scripts
├── run.bat / mvn_run.bat                   # 1-Click Windows execution scripts
├── schema.sql / sample_data.sql            # MySQL 8 database scripts
├── src
│   ├── main
│   │   ├── java\com\healthos
│   │   │   ├── Main.java                   # Standard Main launcher (JavaFX safe)
│   │   │   ├── HealthOSApp.java            # JavaFX Application lifecycle
│   │   │   ├── analytics                   # Rule-based Smart Insight Engine
│   │   │   │   └── HealthInsightEngine.java
│   │   │   ├── controller                  # JavaFX Controllers & Views
│   │   │   │   ├── LoginView.java
│   │   │   │   ├── MainLayoutView.java
│   │   │   │   ├── DashboardView.java
│   │   │   │   ├── ActivityView.java
│   │   │   │   ├── VitalsView.java
│   │   │   │   ├── SleepView.java
│   │   │   │   ├── WaterView.java
│   │   │   │   ├── NutritionView.java
│   │   │   │   ├── AnalyticsView.java
│   │   │   │   ├── ReportsView.java
│   │   │   │   ├── ImporterView.java
│   │   │   │   └── SettingsView.java
│   │   │   ├── dao                         # JDBC Data Access Objects
│   │   │   │   ├── UserDao.java
│   │   │   │   ├── UserProfileDao.java
│   │   │   │   ├── ActivityDao.java
│   │   │   │   ├── VitalDao.java
│   │   │   │   ├── SleepDao.java
│   │   │   │   ├── WaterDao.java
│   │   │   │   ├── NutritionDao.java
│   │   │   │   ├── HealthGoalDao.java
│   │   │   │   ├── InsightDao.java
│   │   │   │   └── ReportDao.java
│   │   │   ├── database                    # JDBC Connection Manager & Fallback
│   │   │   │   └── DatabaseConnection.java
│   │   │   ├── importer                    # CSV & JSON Ingestion
│   │   │   │   └── HealthDataImporter.java
│   │   │   ├── model                       # POJO Entity Models
│   │   │   │   ├── User.java
│   │   │   │   ├── UserProfile.java
│   │   │   │   ├── ActivityLog.java
│   │   │   │   ├── VitalLog.java
│   │   │   │   ├── SleepLog.java
│   │   │   │   ├── WaterLog.java
│   │   │   │   ├── FoodItem.java
│   │   │   │   ├── Meal.java
│   │   │   │   ├── MealItem.java
│   │   │   │   ├── HealthGoal.java
│   │   │   │   ├── HealthInsight.java
│   │   │   │   ├── Notification.java
│   │   │   │   └── ReportRecord.java
│   │   │   ├── reports                     # OpenPDF Health Report Generation
│   │   │   │   └── HealthReportService.java
│   │   │   ├── ui                          # Custom Vector Illustrations
│   │   │   │   └── IllustrationFactory.java
│   │   │   └── util                        # Calculations & Utilities
│   │   │       ├── HealthCalculations.java
│   │   │       ├── PasswordUtil.java
│   │   │       └── DateUtil.java
│   │   └── resources\com\healthos
│   │       ├── db                          # SQL schema, seeds & properties
│   │       │   ├── schema.sql
│   │       │   ├── sample_data.sql
│   │       │   └── db.properties
│   │       ├── illustrations               # Custom SVG assets
│   │       │   ├── heart_cute.svg
│   │       │   ├── moon_sleepy.svg
│   │       │   ├── water_droplet.svg
│   │       │   ├── activity_sneaker.svg
│   │       │   ├── nutrition_basket.svg
│   │       │   ├── wellness_plant.svg
│   │       │   ├── login_wellness.svg
│   │       │   ├── trophy_achievement.svg
│   │       │   └── empty_state.svg
│   │       ├── samples                     # Interoperability test files
│   │       │   ├── sample_activities.csv
│   │       │   └── sample_vitals.json
│   │       └── styles                      # Pastel Apple Health Design System
│   │           └── main.css
│   └── test\java\com\healthos              # JUnit 5 Test Suites
│       ├── HealthCalculationsTest.java
│       └── PasswordUtilTest.java
```

---

## 9. Academic Project Viva & Presentation Tips

1. **Demonstrating Security**:
   - Highlight that passwords are salted and hashed using BCrypt (`PasswordUtil.java`), never stored in plaintext.
   - Show that all database interactions use parameterized SQL `PreparedStatement` to prevent SQL Injection.
2. **Demonstrating Data Integrity & Calculations**:
   - Run the JUnit test suite to demonstrate verified calculation formulas (BMI, MET energy expenditure, percentage differences, and midnight sleep duration).
3. **Demonstrating PDF Report Generation**:
   - In the **Reports** tab, choose a date range and click **Export & Save PDF Report**. Open the resulting file to show the OpenPDF pastel header, tables, and formatted metric summaries.
4. **Demonstrating CSV/JSON Import**:
   - In the **Data Ingestion** tab, download the sample templates, select them, and show the live parsing validation log.
5. **Zero-Crash Resiliency**:
   - Explain the dual-engine connection architecture: direct connection to MySQL 8 with an automatic, zero-crash fallback for offline laptop presentations.
