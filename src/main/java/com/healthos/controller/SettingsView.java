package com.healthos.controller;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * HealthOS Settings & User Profile View.
 * Configures personal biometric parameters, daily target goals,
 * and live MySQL 8 database connection credentials.
 */
public class SettingsView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final DatabaseConnection dbConn = DatabaseConnection.getInstance();
    private final Consumer<Void> onLogout;

    private final VBox rootContent = new VBox(22);

    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField ageField = new TextField();
    private final ComboBox<String> genderCombo = new ComboBox<>();
    private final TextField htField = new TextField();
    private final TextField wtField = new TextField();

    private final TextField stepGoalField = new TextField();
    private final TextField waterGoalField = new TextField();
    private final TextField calGoalField = new TextField();
    private final TextField sleepGoalField = new TextField();

    private final Label dbStatusLabel = new Label();
    private final TextField dbHostField = new TextField("localhost");
    private final TextField dbPortField = new TextField("3306");
    private final TextField dbNameField = new TextField("healthos");
    private final TextField dbUserField = new TextField("root");
    private final PasswordField dbPassField = new PasswordField();

    public SettingsView(Consumer<Void> onLogout) {
        this.onLogout = onLogout;
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        UserProfile p = authService.getCurrentProfile();

        if (user != null) {
            nameField.setText(user.getFullName());
            emailField.setText(user.getEmail());
            emailField.setDisable(true);
        }

        if (p != null) {
            ageField.setText(String.valueOf(p.getAge()));
            genderCombo.setValue(p.getGender() != null ? p.getGender() : "PREFER_NOT_TO_SAY");
            htField.setText(String.valueOf(p.getHeightCm()));
            wtField.setText(String.valueOf(p.getWeightKg()));

            stepGoalField.setText(String.valueOf(p.getDailyStepGoal()));
            waterGoalField.setText(String.valueOf(p.getDailyWaterGoalMl()));
            calGoalField.setText(String.valueOf(p.getDailyCalorieGoal()));
            sleepGoalField.setText(String.valueOf(p.getDailySleepGoalMinutes()));
        }

        dbStatusLabel.setText(dbConn.getStatusMessage());
        dbHostField.setText(dbConn.getHost());
        dbPortField.setText(String.valueOf(dbConn.getPort()));
        dbNameField.setText(dbConn.getDbName());
        dbUserField.setText(dbConn.getUser());
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Preferences & Settings ⚙️");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Customize your profile, daily target goals, and MySQL 8 database connection.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createWellnessPlant(54),
                        IllustrationFactory.createDatabaseServer(48)
                )
        );
        rootContent.getChildren().add(header);

        // Split: Profile & Goals
        HBox split = new HBox(18);
        VBox profCard = buildProfileCard();
        VBox goalsCard = buildGoalsCard();
        HBox.setHgrow(profCard, Priority.ALWAYS);
        HBox.setHgrow(goalsCard, Priority.ALWAYS);
        split.getChildren().addAll(profCard, goalsCard);
        rootContent.getChildren().add(split);

        // Database Configuration Card
        VBox dbCard = buildDatabaseCard();
        rootContent.getChildren().add(dbCard);

        // Logout Section
        HBox logoutRow = new HBox();
        logoutRow.setAlignment(Pos.CENTER_RIGHT);
        Button logoutBtn = new Button("Sign Out of HealthOS");
        logoutBtn.getStyleClass().add("btn-danger");
        logoutBtn.setOnAction(e -> onLogout.accept(null));
        logoutRow.getChildren().add(logoutBtn);
        rootContent.getChildren().add(logoutRow);

        setContent(rootContent);
        refresh();
    }

    private VBox buildProfileCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(18));

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Personal Biometric Profile");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        titleRow.getChildren().addAll(title, IllustrationFactory.createWeighingScale(28));

        genderCombo.getItems().addAll("FEMALE", "MALE", "NON_BINARY", "OTHER", "PREFER_NOT_TO_SAY");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);
        grid.add(genderCombo, 1, 3);
        grid.add(new Label("Height (cm):"), 0, 4);
        grid.add(htField, 1, 4);
        grid.add(new Label("Weight (kg):"), 0, 5);
        grid.add(wtField, 1, 5);

        Button saveBtn = new Button("Save Profile Changes");
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setOnAction(e -> handleSaveProfile());

        card.getChildren().addAll(titleRow, grid, saveBtn);
        return card;
    }

    private VBox buildGoalsCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(18));

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Daily Target Wellness Goals");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        titleRow.getChildren().addAll(title, IllustrationFactory.createTrophy(28));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Daily Steps:"), 0, 0);
        grid.add(stepGoalField, 1, 0);
        grid.add(new Label("Water Intake (ml):"), 0, 1);
        grid.add(waterGoalField, 1, 1);
        grid.add(new Label("Daily Calories (kcal):"), 0, 2);
        grid.add(calGoalField, 1, 2);
        grid.add(new Label("Sleep Target (minutes):"), 0, 3);
        grid.add(sleepGoalField, 1, 3);

        Button saveBtn = new Button("Save Target Goals");
        saveBtn.getStyleClass().add("btn-secondary");
        saveBtn.setOnAction(e -> handleSaveProfile());

        card.getChildren().addAll(titleRow, grid, saveBtn);
        return card;
    }

    private void handleSaveProfile() {
        UserProfile p = authService.getCurrentProfile();
        if (p == null) return;

        try {
            p.setAge(Integer.parseInt(ageField.getText().trim()));
            p.setGender(genderCombo.getValue());
            p.setHeightCm(Double.parseDouble(htField.getText().trim()));
            p.setWeightKg(Double.parseDouble(wtField.getText().trim()));

            p.setDailyStepGoal(Integer.parseInt(stepGoalField.getText().trim()));
            p.setDailyWaterGoalMl(Integer.parseInt(waterGoalField.getText().trim()));
            p.setDailyCalorieGoal(Integer.parseInt(calGoalField.getText().trim()));
            p.setDailySleepGoalMinutes(Integer.parseInt(sleepGoalField.getText().trim()));

            boolean ok = authService.updateProfile(p);
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Profile and wellness goals updated successfully!", ButtonType.OK);
                a.showAndWait();
            }
        } catch (NumberFormatException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values for age, height, weight, and goals.", ButtonType.OK);
            a.showAndWait();
        }
    }

    private VBox buildDatabaseCard() {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("card", "card-pastel-mint");
        card.setPadding(new Insets(20));

        HBox cardHeader = new HBox(12);
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Database Connectivity & Engine Manager");
        title.setStyle("-fx-font-weight: 800; -fx-text-fill: #225945; -fx-font-size: 15px;");
        HBox.setHgrow(title, Priority.ALWAYS);
        cardHeader.getChildren().addAll(title, IllustrationFactory.createDatabaseServer(34));

        dbStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #225945;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);

        grid.add(new Label("MySQL Host:"), 0, 0);
        grid.add(dbHostField, 1, 0);
        grid.add(new Label("Port:"), 2, 0);
        grid.add(dbPortField, 3, 0);

        grid.add(new Label("Database Name:"), 0, 1);
        grid.add(dbNameField, 1, 1);
        grid.add(new Label("Username:"), 2, 1);
        grid.add(dbUserField, 3, 1);

        grid.add(new Label("Password:"), 0, 2);
        grid.add(dbPassField, 1, 2);

        Button reconnectBtn = new Button("🔌 Test & Connect to MySQL 8");
        reconnectBtn.getStyleClass().add("btn-primary");
        reconnectBtn.setOnAction(e -> handleReconnectDb());

        card.getChildren().addAll(cardHeader, dbStatusLabel, grid, reconnectBtn);
        return card;
    }

    private void handleReconnectDb() {
        String h = dbHostField.getText().trim();
        int p = Integer.parseInt(dbPortField.getText().trim());
        String d = dbNameField.getText().trim();
        String u = dbUserField.getText().trim();
        String pwd = dbPassField.getText().trim();

        boolean ok = dbConn.testAndApplyCredentials(h, p, d, u, pwd);
        dbStatusLabel.setText(dbConn.getStatusMessage());
        if (ok) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Successfully connected to MySQL 8 database: " + d, ButtonType.OK);
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.WARNING, "MySQL connection failed. Continuing in resilient Embedded Database mode.", ButtonType.OK);
            a.showAndWait();
        }
    }
}
