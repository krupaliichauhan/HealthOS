package com.healthos.controller;

import com.healthos.dao.ActivityDao;
import com.healthos.dao.UserProfileDao;
import com.healthos.model.ActivityLog;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import com.healthos.util.DateUtil;
import com.healthos.util.HealthCalculations;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HealthOS Activity Tracker View.
 * Tracks daily steps, distance, active minutes, workouts, and calories burned
 * with goal completion rings, trend charts, and complete record management.
 */
public class ActivityView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final ActivityDao activityDao = new ActivityDao();
    private final UserProfileDao profileDao = new UserProfileDao();

    private final VBox rootContent = new VBox(22);
    private final TableView<ActivityLog> tableView = new TableView<>();
    private final ComboBox<String> typeCombo = new ComboBox<>();
    private final TextField stepsField = new TextField();
    private final TextField distField = new TextField();
    private final TextField minsField = new TextField();
    private final TextField calField = new TextField();
    private final TextField notesField = new TextField();
    private final DatePicker datePicker = new DatePicker(LocalDate.now());

    private final Label progressLabel = new Label();
    private final ProgressBar progressBar = new ProgressBar(0);
    private final BarChart<String, Number> barChart;

    public ActivityView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Steps");
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(220);

        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        int userId = user.getId();

        // Reload table
        List<ActivityLog> logs = activityDao.getActivities(userId, LocalDate.now().minusDays(30), LocalDate.now());
        tableView.setItems(FXCollections.observableArrayList(logs));

        // Reload goal progress
        UserProfile profile = profileDao.getProfile(userId);
        int goal = profile != null ? profile.getDailyStepGoal() : 10000;
        int todaySteps = activityDao.getStepsForDate(userId, LocalDate.now());
        double pct = HealthCalculations.calculateGoalProgress(todaySteps, goal);
        progressBar.setProgress(Math.min(1.0, pct / 100.0));
        progressLabel.setText(String.format("%,d", todaySteps) + " / " + String.format("%,d", goal) + " steps (" + pct + "%)");

        // Reload chart
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<LocalDate, Integer> past7Days = activityDao.getDailyStepsForPastDays(userId, 7);
        for (Map.Entry<LocalDate, Integer> entry : past7Days.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().format(DateUtil.SHORT_DATE), entry.getValue()));
        }
        barChart.getData().add(series);
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Activity & Movement Tracker 👟");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Log daily steps, walks, runs, and workouts to cultivate healthy momentum.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createSneaker(56),
                        IllustrationFactory.createFlameCalorie(48)
                )
        );
        rootContent.getChildren().add(header);

        // Daily Progress Card
        VBox goalCard = buildGoalCard();
        rootContent.getChildren().add(goalCard);

        // Top Split: Log Form + 7-Day Chart
        HBox split = new HBox(18);
        VBox formCard = buildFormCard();
        HBox.setHgrow(formCard, Priority.NEVER);
        formCard.setPrefWidth(380);

        VBox chartCard = new VBox(10);
        chartCard.getStyleClass().add("card");
        chartCard.setPadding(new Insets(18));
        HBox.setHgrow(chartCard, Priority.ALWAYS);
        Label chartTitle = new Label("7-Day Steps Trend");
        chartTitle.getStyleClass().add("text-h3");
        chartCard.getChildren().addAll(chartTitle, barChart);

        split.getChildren().addAll(formCard, chartCard);
        rootContent.getChildren().add(split);

        // History Table Card
        VBox tableCard = buildTableCard();
        rootContent.getChildren().add(tableCard);

        setContent(rootContent);
        refresh();
    }

    private VBox buildGoalCard() {
        VBox box = new VBox(10);
        box.getStyleClass().addAll("card", "card-pastel-mint");
        box.setPadding(new Insets(16, 20, 16, 20));

        HBox top = new HBox(12);
        top.setAlignment(Pos.CENTER_LEFT);
        VBox labelCol = new VBox(2);
        Label lbl = new Label("Today's Activity Goal Progress");
        lbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #225945; -fx-font-size: 14px;");
        Label subLbl = new Label("Keep moving! Every step fuels your daily energy.");
        subLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #4F806F;");
        labelCol.getChildren().addAll(lbl, subLbl);
        HBox.setHgrow(labelCol, Priority.ALWAYS);

        progressLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #225945; -fx-font-size: 13.5px;");
        top.getChildren().addAll(IllustrationFactory.createFlameCalorie(36), labelCol, progressLabel);

        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().addAll("progress-bar", "progress-bar-mint");

        box.getChildren().addAll(top, progressBar);
        return box;
    }

    private VBox buildFormCard() {
        VBox form = new VBox(12);
        form.getStyleClass().add("card");
        form.setPadding(new Insets(18));

        HBox formHeader = new HBox(10);
        formHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Record Exercise or Steps");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        formHeader.getChildren().addAll(title, IllustrationFactory.createSneaker(30));

        typeCombo.getItems().addAll("WALKING", "RUNNING", "CYCLING", "YOGA", "SWIMMING", "HIIT", "STRENGTH", "OTHER");
        typeCombo.setValue("WALKING");
        typeCombo.setMaxWidth(Double.MAX_VALUE);

        stepsField.setPromptText("Steps (e.g. 5000)");
        distField.setPromptText("Distance in km (e.g. 3.5)");
        minsField.setPromptText("Active minutes (e.g. 40)");
        calField.setPromptText("Calories burned (optional auto-calc)");
        notesField.setPromptText("Session notes (e.g. Morning park walk)");

        // Auto calculate calories when minutes or type changes
        minsField.textProperty().addListener((obs, oldV, newV) -> updateAutoCalories());
        typeCombo.valueProperty().addListener((obs, oldV, newV) -> updateAutoCalories());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Activity:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Steps:"), 0, 2);
        grid.add(stepsField, 1, 2);
        grid.add(new Label("Distance:"), 0, 3);
        grid.add(distField, 1, 3);
        grid.add(new Label("Minutes:"), 0, 4);
        grid.add(minsField, 1, 4);
        grid.add(new Label("Calories:"), 0, 5);
        grid.add(calField, 1, 5);
        grid.add(new Label("Notes:"), 0, 6);
        grid.add(notesField, 1, 6);

        Button saveBtn = new Button("Add Activity Log");
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> handleSaveActivity());

        form.getChildren().addAll(formHeader, grid, saveBtn);
        return form;
    }

    private void updateAutoCalories() {
        try {
            int mins = Integer.parseInt(minsField.getText().trim());
            String type = typeCombo.getValue();
            UserProfile p = authService.getCurrentProfile();
            double wt = p != null && p.getWeightKg() > 0 ? p.getWeightKg() : 60.0;
            double cal = HealthCalculations.estimateExerciseCalories(type, mins, wt);
            calField.setText(String.valueOf((int) cal));
        } catch (Exception ignore) {}
    }

    private void handleSaveActivity() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        try {
            String type = typeCombo.getValue();
            int steps = stepsField.getText().isEmpty() ? 0 : Integer.parseInt(stepsField.getText().trim());
            double dist = distField.getText().isEmpty() ? 0.0 : Double.parseDouble(distField.getText().trim());
            int mins = minsField.getText().isEmpty() ? 0 : Integer.parseInt(minsField.getText().trim());
            double cal = calField.getText().isEmpty() ? 0.0 : Double.parseDouble(calField.getText().trim());
            LocalDate date = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();
            String notes = notesField.getText() != null ? notesField.getText().trim() : "";

            ActivityLog log = new ActivityLog(0, user.getId(), type, steps, dist, mins, cal, date, LocalTime.now(), 1, notes);
            boolean ok = activityDao.addActivity(log);
            if (ok) {
                stepsField.clear();
                distField.clear();
                minsField.clear();
                calField.clear();
                notesField.clear();
                refresh();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values for steps, distance, and minutes.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private VBox buildTableCard() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(18));

        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Activity History Records");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        tableHeader.getChildren().addAll(title, IllustrationFactory.createFlameCalorie(26));

        VBox emptyPlaceholder = new VBox(8);
        emptyPlaceholder.setAlignment(Pos.CENTER);
        Label emptyText = new Label("No activity records found. Log your first workout above!");
        emptyText.setStyle("-fx-text-fill: #789088; -fx-font-size: 12px;");
        emptyPlaceholder.getChildren().addAll(IllustrationFactory.createEmptyState(54), emptyText);
        tableView.setPlaceholder(emptyPlaceholder);

        TableColumn<ActivityLog, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogDate().toString()));

        TableColumn<ActivityLog, String> typeCol = new TableColumn<>("Activity");
        typeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActivityType()));

        TableColumn<ActivityLog, String> stepsCol = new TableColumn<>("Steps");
        stepsCol.setCellValueFactory(data -> new SimpleStringProperty(String.format("%,d", data.getValue().getSteps())));

        TableColumn<ActivityLog, String> distCol = new TableColumn<>("Distance");
        distCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDistanceKm() + " km"));

        TableColumn<ActivityLog, String> minsCol = new TableColumn<>("Active Mins");
        minsCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActiveMinutes() + " m"));

        TableColumn<ActivityLog, String> calCol = new TableColumn<>("Calories");
        calCol.setCellValueFactory(data -> new SimpleStringProperty((int) data.getValue().getCaloriesBurned() + " kcal"));

        TableColumn<ActivityLog, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNotes() != null ? data.getValue().getNotes() : ""));
        notesCol.setPrefWidth(160);

        TableColumn<ActivityLog, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button delBtn = new Button("Delete");
            {
                delBtn.getStyleClass().add("btn-danger");
                delBtn.setOnAction(e -> {
                    ActivityLog item = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this activity log?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.YES) {
                        activityDao.deleteActivity(item.getId(), authService.getCurrentUser().getId());
                        refresh();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : delBtn);
            }
        });

        tableView.getColumns().addAll(dateCol, typeCol, stepsCol, distCol, minsCol, calCol, notesCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(260);

        box.getChildren().addAll(tableHeader, tableView);
        return box;
    }
}
