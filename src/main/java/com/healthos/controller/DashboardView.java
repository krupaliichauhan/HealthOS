package com.healthos.controller;

import com.healthos.analytics.HealthInsightEngine;
import com.healthos.dao.ActivityDao;
import com.healthos.dao.NutritionDao;
import com.healthos.dao.SleepDao;
import com.healthos.dao.VitalDao;
import com.healthos.dao.WaterDao;
import com.healthos.model.ActivityLog;
import com.healthos.model.HealthInsight;
import com.healthos.model.Meal;
import com.healthos.model.SleepLog;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.model.VitalLog;
import com.healthos.model.WaterLog;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import com.healthos.util.DateUtil;
import com.healthos.util.HealthCalculations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HealthOS Main Dashboard View.
 * The primary showcase screen combining Apple Health-inspired metric rings/cards,
 * cute pastel character illustrations, weekly charts, live insights, and quick actions.
 */
public class DashboardView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final ActivityDao activityDao = new ActivityDao();
    private final VitalDao vitalDao = new VitalDao();
    private final SleepDao sleepDao = new SleepDao();
    private final WaterDao waterDao = new WaterDao();
    private final NutritionDao nutritionDao = new NutritionDao();
    private final HealthInsightEngine insightEngine = new HealthInsightEngine();

    private final Consumer<String> onNavigate;
    private final Runnable onDataRefreshNeeded;

    private final VBox rootContent = new VBox(24);

    public DashboardView(Consumer<String> onNavigate, Runnable onDataRefreshNeeded) {
        this.onNavigate = onNavigate;
        this.onDataRefreshNeeded = onDataRefreshNeeded;
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        buildUI();
    }

    public void refresh() {
        rootContent.getChildren().clear();
        buildContent();
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));
        buildContent();
        setContent(rootContent);
    }

    private void buildContent() {
        User user = authService.getCurrentUser();
        UserProfile profile = authService.getCurrentProfile();
        int userId = user != null ? user.getId() : 1;

        // 1. Personalized Greeting & Top Banner
        rootContent.getChildren().add(buildHeaderBanner(user));

        // 2. Quick Action Bar
        rootContent.getChildren().add(buildQuickActionBar(userId));

        // 3. Smart Health Insights Carousel / Banner
        rootContent.getChildren().add(buildInsightBanner(userId, profile));

        // 4. Metric Cards Grid (Steps, Heart Rate, Sleep, Water, Calories)
        rootContent.getChildren().add(buildMetricCardsGrid(userId, profile));

        // 5. Charts Row (Weekly Steps BarChart + Sleep Trend LineChart)
        rootContent.getChildren().add(buildChartsRow(userId));

        // 6. Recent Activities Summary
        rootContent.getChildren().add(buildRecentActivitiesSection(userId));
    }

    private HBox buildHeaderBanner(User user) {
        HBox banner = new HBox(16);
        banner.setAlignment(Pos.CENTER_LEFT);

        VBox titleCol = new VBox(4);
        HBox.setHgrow(titleCol, Priority.ALWAYS);

        LocalTime now = LocalTime.now();
        String timeGreeting = now.getHour() < 12 ? "Good morning" : now.getHour() < 17 ? "Good afternoon" : "Good evening";
        String userName = user != null ? user.getFullName().split(" ")[0] : "Fairy";

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label greetingLbl = new Label(timeGreeting + ", " + userName + "! ✨");
        greetingLbl.getStyleClass().add("text-h1");

        Label badge = new Label("HealthOS Active");
        badge.getStyleClass().addAll("badge", "badge-mint");

        titleRow.getChildren().addAll(greetingLbl, badge);

        Label subTitle = new Label(DateUtil.formatFriendly(LocalDate.now()) + " • Let's make today a vibrant, healthy one.");
        subTitle.getStyleClass().add("text-muted");

        titleCol.getChildren().addAll(titleRow, subTitle);
        banner.getChildren().addAll(titleCol, IllustrationFactory.createWellnessPlant(48));
        return banner;
    }

    private HBox buildQuickActionBar(int userId) {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);

        Button addWaterBtn = new Button("💧 +250ml Water");
        addWaterBtn.getStyleClass().add("btn-mint");
        addWaterBtn.setOnAction(e -> {
            waterDao.addWaterLog(new WaterLog(0, userId, 250, LocalDate.now(), LocalTime.now()));
            refresh();
        });

        Button logStepsBtn = new Button("👟 Log Activity");
        logStepsBtn.getStyleClass().add("btn-secondary");
        logStepsBtn.setOnAction(e -> onNavigate.accept("ACTIVITY"));

        Button addVitalsBtn = new Button("❤️ Add Vitals");
        addVitalsBtn.getStyleClass().add("btn-secondary");
        addVitalsBtn.setOnAction(e -> onNavigate.accept("VITALS"));

        Button logMealBtn = new Button("🥑 Add Meal");
        logMealBtn.getStyleClass().add("btn-secondary");
        logMealBtn.setOnAction(e -> onNavigate.accept("NUTRITION"));

        Button genReportBtn = new Button("📄 Generate PDF Report");
        genReportBtn.getStyleClass().add("btn-primary");
        genReportBtn.setOnAction(e -> onNavigate.accept("REPORTS"));

        bar.getChildren().addAll(addWaterBtn, logStepsBtn, addVitalsBtn, logMealBtn, genReportBtn);
        return bar;
    }

    private VBox buildInsightBanner(int userId, UserProfile profile) {
        VBox box = new VBox(8);
        box.getStyleClass().addAll("card", "card-pastel-mint");
        box.setPadding(new Insets(16, 20, 16, 20));

        List<HealthInsight> insights = insightEngine.generateInsights(userId, profile);
        HealthInsight topInsight = !insights.isEmpty() ? insights.get(0) :
                new HealthInsight(0, userId, "GENERAL", "Great Consistency! 🌸", "Tracking your daily habits helps you achieve long-term wellness.", "ACHIEVEMENT", false, null);

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        Label spark = new Label("✨ SMART HEALTH INSIGHT");
        spark.setStyle("-fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: #225945;");

        Label categoryBadge = new Label(topInsight.getCategory());
        categoryBadge.getStyleClass().addAll("badge", "badge-teal");
        header.getChildren().addAll(spark, categoryBadge);

        Label title = new Label(topInsight.getTitle());
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #225945;");

        Label msg = new Label(topInsight.getMessage());
        msg.setStyle("-fx-font-size: 13px; -fx-text-fill: #34433E;");
        msg.setWrapText(true);

        HBox contentRow = new HBox(16);
        contentRow.setAlignment(Pos.CENTER_LEFT);
        VBox textCol = new VBox(6);
        HBox.setHgrow(textCol, Priority.ALWAYS);
        textCol.getChildren().addAll(header, title, msg);
        contentRow.getChildren().addAll(IllustrationFactory.createAnalyticsOwl(44), textCol);

        box.getChildren().add(contentRow);
        return box;
    }

    private GridPane buildMetricCardsGrid(int userId, UserProfile profile) {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        int targetSteps = profile != null ? profile.getDailyStepGoal() : 10000;
        int targetWater = profile != null ? profile.getDailyWaterGoalMl() : 2500;
        int targetCalories = profile != null ? profile.getDailyCalorieGoal() : 2000;

        int todaySteps = activityDao.getStepsForDate(userId, LocalDate.now());
        int todayWater = waterDao.getTodayWaterTotalMl(userId, LocalDate.now());
        VitalLog latestVital = vitalDao.getLatestVital(userId);
        SleepLog latestSleep = sleepDao.getLatestSleep(userId);
        List<Meal> todayMeals = nutritionDao.getMealsForDate(userId, LocalDate.now());

        double todayCalories = 0;
        for (Meal m : todayMeals) {
            todayCalories += m.getTotalCalories();
        }

        // Card 1: Steps & Motion
        VBox stepsCard = createMetricCard("Daily Steps",
                String.format("%,d", todaySteps), "steps",
                IllustrationFactory.createSneaker(48),
                HealthCalculations.calculateGoalProgress(todaySteps, targetSteps),
                "Goal: " + String.format("%,d", targetSteps), "progress-bar-mint", "ACTIVITY");

        // Card 2: Resting Heart Rate
        int hr = latestVital != null && latestVital.getHeartRateBpm() != null ? latestVital.getHeartRateBpm() : 70;
        VBox hrCard = createMetricCard("Heart Rate",
                String.valueOf(hr), "BPM",
                IllustrationFactory.createHeart(48),
                100.0,
                "Resting baseline • Optimal", "progress-bar-peach", "VITALS");

        // Card 3: Sleep Duration
        int sleepMin = latestSleep != null ? latestSleep.getDurationMinutes() : 490;
        VBox sleepCard = createMetricCard("Last Sleep",
                HealthCalculations.formatDurationHoursMinutes(sleepMin), "hours",
                IllustrationFactory.createSleepyMoon(48),
                HealthCalculations.calculateGoalProgress(sleepMin, 480),
                "Quality: " + (latestSleep != null ? latestSleep.getQualityRating() : 5) + "/5 ★", "progress-bar-lavender", "SLEEP");

        // Card 4: Water Hydration
        VBox waterCard = createMetricCard("Water Intake",
                String.format("%,d", todayWater), "ml",
                IllustrationFactory.createWaterDrop(48),
                HealthCalculations.calculateGoalProgress(todayWater, targetWater),
                "Remaining: " + Math.max(0, targetWater - todayWater) + " ml", "progress-bar-mint", "WATER");

        // Card 5: Nutrition Calories
        VBox calCard = createMetricCard("Calories Eaten",
                String.format("%,d", (int)todayCalories), "kcal",
                IllustrationFactory.createNutrition(48),
                HealthCalculations.calculateGoalProgress(todayCalories, targetCalories),
                "Budget: " + targetCalories + " kcal", "progress-bar-peach", "NUTRITION");

        grid.add(stepsCard, 0, 0);
        grid.add(hrCard, 1, 0);
        grid.add(sleepCard, 2, 0);
        grid.add(waterCard, 3, 0);
        grid.add(calCard, 4, 0);

        for (int i = 0; i < 5; i++) {
            GridPane.setHgrow(grid.getChildren().get(i), Priority.ALWAYS);
        }

        return grid;
    }

    private VBox createMetricCard(String title, String value, String unit, Node ill, double progressPct, String subtext, String progressClass, String targetNav) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("card", "card-clickable");
        card.setPadding(new Insets(18));
        card.setOnMouseClicked(e -> onNavigate.accept(targetNav));

        HBox topRow = new HBox(8);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #6C7D76;");
        HBox.setHgrow(titleLbl, Priority.ALWAYS);
        topRow.getChildren().addAll(titleLbl, ill);

        HBox valRow = new HBox(6);
        valRow.setAlignment(Pos.BASELINE_LEFT);
        Label valLbl = new Label(value);
        valLbl.getStyleClass().add("text-metric-value");
        Label unitLbl = new Label(unit);
        unitLbl.getStyleClass().add("text-metric-unit");
        valRow.getChildren().addAll(valLbl, unitLbl);

        ProgressBar pb = new ProgressBar(Math.min(1.0, progressPct / 100.0));
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.getStyleClass().addAll("progress-bar", progressClass);

        HBox botRow = new HBox(6);
        botRow.setAlignment(Pos.CENTER_LEFT);
        Label subLbl = new Label(subtext);
        subLbl.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #6C7D76;");
        botRow.getChildren().add(subLbl);

        card.getChildren().addAll(topRow, valRow, pb, botRow);
        return card;
    }

    private HBox buildChartsRow(int userId) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER);

        // Chart 1: 7-Day Steps Bar Chart
        VBox stepsBox = new VBox(10);
        stepsBox.getStyleClass().add("card");
        stepsBox.setPadding(new Insets(18));
        HBox.setHgrow(stepsBox, Priority.ALWAYS);

        Label stepsTitle = new Label("Weekly Activity Trend (Steps)");
        stepsTitle.getStyleClass().add("text-h3");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Steps");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(230);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<LocalDate, Integer> past7Days = activityDao.getDailyStepsForPastDays(userId, 7);
        for (Map.Entry<LocalDate, Integer> entry : past7Days.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().format(DateUtil.SHORT_DATE), entry.getValue()));
        }
        barChart.getData().add(series);
        stepsBox.getChildren().addAll(stepsTitle, barChart);

        // Chart 2: 7-Day Sleep Duration Line Chart
        VBox sleepBox = new VBox(10);
        sleepBox.getStyleClass().add("card");
        sleepBox.setPadding(new Insets(18));
        HBox.setHgrow(sleepBox, Priority.ALWAYS);

        Label sleepTitle = new Label("Sleep Pattern Trend (Hours)");
        sleepTitle.getStyleClass().add("text-h3");

        CategoryAxis sleepXAxis = new CategoryAxis();
        NumberAxis sleepYAxis = new NumberAxis(4, 10, 1);
        sleepYAxis.setLabel("Hours");
        LineChart<String, Number> lineChart = new LineChart<>(sleepXAxis, sleepYAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPrefHeight(230);

        XYChart.Series<String, Number> sleepSeries = new XYChart.Series<>();
        Map<LocalDate, Double> sleepDays = sleepDao.getDailySleepHoursForPastDays(userId, 7);
        for (Map.Entry<LocalDate, Double> entry : sleepDays.entrySet()) {
            sleepSeries.getData().add(new XYChart.Data<>(entry.getKey().format(DateUtil.SHORT_DATE), entry.getValue()));
        }
        lineChart.getData().add(sleepSeries);
        sleepBox.getChildren().addAll(sleepTitle, lineChart);

        row.getChildren().addAll(stepsBox, sleepBox);
        return row;
    }

    private VBox buildRecentActivitiesSection(int userId) {
        VBox box = new VBox(12);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(20));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Recent Health Activities");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);

        Button viewAllBtn = new Button("View Activity Tracker →");
        viewAllBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4A9C8F; -fx-font-weight: bold; -fx-cursor: hand;");
        viewAllBtn.setOnAction(e -> onNavigate.accept("ACTIVITY"));
        header.getChildren().addAll(title, viewAllBtn);

        VBox list = new VBox(8);
        List<ActivityLog> recents = activityDao.getRecentActivities(userId, 4);
        for (ActivityLog act : recents) {
            HBox item = new HBox(14);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 14, 10, 14));
            item.setStyle("-fx-background-color: #FAFDFB; -fx-background-radius: 12px; -fx-border-color: #EBF1ED; -fx-border-radius: 12px; -fx-border-width: 1px;");

            Label typeIcon = new Label(act.getActivityType().equals("RUNNING") ? "🏃" : act.getActivityType().equals("CYCLING") ? "🚴" : act.getActivityType().equals("YOGA") ? "🧘" : "👟");
            typeIcon.setStyle("-fx-font-size: 18px;");

            VBox info = new VBox(2);
            Label actType = new Label(act.getActivityType() + " • " + act.getNotes());
            actType.setStyle("-fx-font-weight: bold; -fx-text-fill: #34433E;");
            Label dateLbl = new Label(DateUtil.formatFriendly(act.getLogDate()) + " at " + DateUtil.formatTime(act.getLogTime()));
            dateLbl.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #6C7D76;");
            info.getChildren().addAll(actType, dateLbl);
            HBox.setHgrow(info, Priority.ALWAYS);

            Label stats = new Label(String.format("%,d", act.getSteps()) + " steps  |  " + act.getDistanceKm() + " km  |  " + (int)act.getCaloriesBurned() + " kcal");
            stats.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #78BDB1;");

            item.getChildren().addAll(typeIcon, info, stats);
            list.getChildren().add(item);
        }

        if (recents.isEmpty()) {
            Label empty = new Label("No activities logged yet. Click 'Log Activity' above to record your first session!");
            empty.setStyle("-fx-text-fill: #6C7D76; -fx-font-style: italic;");
            list.getChildren().add(empty);
        }

        box.getChildren().addAll(header, list);
        return box;
    }
}
