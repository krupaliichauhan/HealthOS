package com.healthos.controller;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.User;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HealthOS Main Shell / Layout View.
 * Implements Apple Health-inspired sidebar navigation with active indicator styling,
 * user profile status pill, and responsive dynamic view swapping.
 */
public class MainLayoutView extends BorderPane {

    private final AuthService authService = AuthService.getInstance();
    private final Consumer<Void> onLogout;

    private final StackPane contentArea = new StackPane();
    private final Map<String, Button> navButtons = new HashMap<>();

    // Sub-views
    private DashboardView dashboardView;
    private ActivityView activityView;
    private VitalsView vitalsView;
    private SleepView sleepView;
    private WaterView waterView;
    private NutritionView nutritionView;
    private AnalyticsView analyticsView;
    private ReportsView reportsView;
    private ImporterView importerView;
    private SettingsView settingsView;

    private String currentView = "DASHBOARD";

    public MainLayoutView(Consumer<Void> onLogout) {
        this.onLogout = onLogout;
        getStyleClass().add("app-container");
        buildUI();
    }

    private void buildUI() {
        // Instantiate sub-views
        dashboardView = new DashboardView(this::navigateTo, this::refreshCurrentView);
        activityView = new ActivityView();
        vitalsView = new VitalsView();
        sleepView = new SleepView();
        waterView = new WaterView();
        nutritionView = new NutritionView();
        analyticsView = new AnalyticsView();
        reportsView = new ReportsView();
        importerView = new ImporterView();
        settingsView = new SettingsView(v -> onLogout.accept(null));

        // Setup Sidebar
        VBox sidebar = buildSidebar();
        setLeft(sidebar);

        // Setup Content Area
        contentArea.setStyle("-fx-background-color: #F8FAF8;");
        setCenter(contentArea);

        // Default Navigation to Dashboard
        navigateTo("DASHBOARD");
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.getStyleClass().add("sidebar");

        // Top Logo
        HBox logo = (HBox) IllustrationFactory.createAppLogo(140, 38);
        logo.getStyleClass().add("sidebar-logo-container");

        Label sectionNav = new Label("TRACKING & WELLNESS");
        sectionNav.getStyleClass().add("nav-section-title");

        Button btnDashboard = createNavButton("🏠  Dashboard", "DASHBOARD");
        Button btnActivity = createNavButton("👟  Activity & Steps", "ACTIVITY");
        Button btnVitals = createNavButton("❤️  Vitals & BMI", "VITALS");
        Button btnSleep = createNavButton("🌙  Sleep Tracker", "SLEEP");
        Button btnWater = createNavButton("💧  Water Intake", "WATER");
        Button btnNutrition = createNavButton("🥑  Nutrition & Meals", "NUTRITION");

        Label sectionInsights = new Label("ANALYTICS & EXPORTS");
        sectionInsights.getStyleClass().add("nav-section-title");

        Button btnAnalytics = createNavButton("📊  Health Analytics", "ANALYTICS");
        Button btnReports = createNavButton("📄  PDF Health Reports", "REPORTS");
        Button btnImporter = createNavButton("📥  Data Ingestion", "IMPORTER");
        Button btnSettings = createNavButton("⚙️  Preferences", "SETTINGS");

        VBox navList = new VBox(4,
                sectionNav,
                btnDashboard, btnActivity, btnVitals, btnSleep, btnWater, btnNutrition,
                sectionInsights,
                btnAnalytics, btnReports, btnImporter, btnSettings
        );
        VBox.setVgrow(navList, Priority.ALWAYS);

        // Bottom User Profile Card
        VBox userCard = buildUserMiniCard();

        sidebar.getChildren().addAll(logo, navList, userCard);
        return sidebar;
    }

    private Button createNavButton(String text, String viewKey) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> navigateTo(viewKey));
        navButtons.put(viewKey, btn);
        return btn;
    }

    private VBox buildUserMiniCard() {
        VBox card = new VBox(6);
        card.getStyleClass().add("user-mini-card");

        User user = authService.getCurrentUser();
        String name = user != null ? user.getFullName() : "Fairy Soni";
        String email = user != null ? user.getEmail() : "fairy@healthos.app";

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label(name.substring(0, Math.min(1, name.length())).toUpperCase());
        avatar.setStyle("-fx-background-color: #DDF3E8; -fx-text-fill: #225945; -fx-font-weight: bold; " +
                "-fx-padding: 6 10; -fx-background-radius: 50%; -fx-font-size: 13px;");

        VBox info = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-size: 12.5px; -fx-font-weight: bold; -fx-text-fill: #34433E;");
        Label emailLbl = new Label(email);
        emailLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #6C7D76;");
        info.getChildren().addAll(nameLbl, emailLbl);
        row.getChildren().addAll(avatar, info);

        // Status badge
        DatabaseConnection db = DatabaseConnection.getInstance();
        Label dbPill = new Label(db.isFallbackActive() ? "● Resilient Local DB" : "● MySQL 8 Connected");
        dbPill.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: " +
                (db.isFallbackActive() ? "#5E4694;" : "#225945;") + " -fx-padding: 2 6; -fx-background-color: " +
                (db.isFallbackActive() ? "#E9E3F7;" : "#DDF3E8;") + " -fx-background-radius: 10px;");

        card.getChildren().addAll(row, dbPill);
        return card;
    }

    public void navigateTo(String viewKey) {
        this.currentView = viewKey;

        // Update active nav button styles
        for (Map.Entry<String, Button> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(viewKey)) {
                if (!entry.getValue().getStyleClass().contains("nav-button-active")) {
                    entry.getValue().getStyleClass().add("nav-button-active");
                }
            } else {
                entry.getValue().getStyleClass().remove("nav-button-active");
            }
        }

        Node targetNode = switch (viewKey) {
            case "ACTIVITY" -> {
                activityView.refresh();
                yield activityView;
            }
            case "VITALS" -> {
                vitalsView.refresh();
                yield vitalsView;
            }
            case "SLEEP" -> {
                sleepView.refresh();
                yield sleepView;
            }
            case "WATER" -> {
                waterView.refresh();
                yield waterView;
            }
            case "NUTRITION" -> {
                nutritionView.refresh();
                yield nutritionView;
            }
            case "ANALYTICS" -> {
                analyticsView.refresh();
                yield analyticsView;
            }
            case "REPORTS" -> {
                reportsView.refresh();
                yield reportsView;
            }
            case "IMPORTER" -> importerView;
            case "SETTINGS" -> {
                settingsView.refresh();
                yield settingsView;
            }
            default -> {
                dashboardView.refresh();
                yield dashboardView;
            }
        };

        contentArea.getChildren().setAll(targetNode);
    }

    private void refreshCurrentView() {
        navigateTo(currentView);
    }
}
