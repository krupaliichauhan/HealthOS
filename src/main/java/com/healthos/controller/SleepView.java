package com.healthos.controller;

import com.healthos.dao.SleepDao;
import com.healthos.model.SleepLog;
import com.healthos.model.User;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import com.healthos.util.DateUtil;
import com.healthos.util.HealthCalculations;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HealthOS Sleep Tracker View.
 * Accurately tracks bedtime, wake-up time (supporting midnight rollover),
 * sleep duration, quality rating (1-5 stars), and sleep hygiene trends.
 */
public class SleepView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final SleepDao sleepDao = new SleepDao();

    private final VBox rootContent = new VBox(22);
    private final TableView<SleepLog> tableView = new TableView<>();

    private final DatePicker datePicker = new DatePicker(LocalDate.now());
    private final ComboBox<String> startHourCombo = new ComboBox<>();
    private final ComboBox<String> endHourCombo = new ComboBox<>();
    private final ComboBox<Integer> qualityCombo = new ComboBox<>();
    private final TextField notesField = new TextField();
    private final Label durationPreviewLabel = new Label("Calculated Duration: 8h 00m");

    private final LineChart<String, Number> sleepChart;

    public SleepView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(4, 12, 1);
        yAxis.setLabel("Hours of Sleep");
        sleepChart = new LineChart<>(xAxis, yAxis);
        sleepChart.setLegendVisible(false);
        sleepChart.setPrefHeight(230);

        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        int userId = user.getId();

        List<SleepLog> logs = sleepDao.getSleepLogs(userId, 30);
        tableView.setItems(FXCollections.observableArrayList(logs));

        // Reload Chart
        sleepChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<LocalDate, Double> sleepDays = sleepDao.getDailySleepHoursForPastDays(userId, 7);
        for (Map.Entry<LocalDate, Double> entry : sleepDays.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().format(DateUtil.SHORT_DATE), entry.getValue()));
        }
        sleepChart.getData().add(series);
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Sleep Quality & Recovery Tracker 🌙");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Restful sleep revitalizes cognitive focus, emotional balance, and physical cellular repair.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createSleepyMoon(56),
                        IllustrationFactory.createPillow(48)
                )
        );
        rootContent.getChildren().add(header);

        // Sleep Sanctuary Tip Card
        VBox tipCard = new VBox(4);
        tipCard.getStyleClass().addAll("card", "card-pastel-lavender");
        tipCard.setPadding(new Insets(14, 18, 14, 18));
        HBox tipRow = new HBox(14);
        tipRow.setAlignment(Pos.CENTER_LEFT);
        VBox textCol = new VBox(2);
        Label tipTitle = new Label("Restful Recovery Sanctuary");
        tipTitle.setStyle("-fx-font-weight: 800; -fx-text-fill: #5E4694; -fx-font-size: 13.5px;");
        Label tipDesc = new Label("Aim for 7-9 hours of consistent sleep in a cool, calming environment to maximize restorative REM cycles.");
        tipDesc.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #5E4694;");
        textCol.getChildren().addAll(tipTitle, tipDesc);
        HBox.setHgrow(textCol, Priority.ALWAYS);
        tipRow.getChildren().addAll(IllustrationFactory.createPillow(38), textCol);
        tipCard.getChildren().add(tipRow);
        rootContent.getChildren().add(tipCard);

        // Split: Form + 7-Day Chart
        HBox split = new HBox(18);
        VBox formCard = buildFormCard();
        formCard.setPrefWidth(390);

        VBox chartCard = new VBox(10);
        chartCard.getStyleClass().add("card");
        chartCard.setPadding(new Insets(18));
        HBox.setHgrow(chartCard, Priority.ALWAYS);
        Label chartTitle = new Label("Weekly Sleep Duration (Hours)");
        chartTitle.getStyleClass().add("text-h3");
        chartCard.getChildren().addAll(chartTitle, sleepChart);

        split.getChildren().addAll(formCard, chartCard);
        rootContent.getChildren().add(split);

        // Table Card
        VBox tableCard = buildTableCard();
        rootContent.getChildren().add(tableCard);

        setContent(rootContent);
        refresh();
    }

    private VBox buildFormCard() {
        VBox form = new VBox(12);
        form.getStyleClass().add("card");
        form.setPadding(new Insets(18));

        HBox formHeader = new HBox(10);
        formHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Record Sleep Session");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        formHeader.getChildren().addAll(title, IllustrationFactory.createAlarmClock(30));

        // Bedtime choices (e.g. 21:00 to 02:00)
        startHourCombo.getItems().addAll("21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00", "00:30", "01:00", "01:30", "02:00");
        startHourCombo.setValue("23:00");
        startHourCombo.setMaxWidth(Double.MAX_VALUE);

        // Wake up choices (e.g. 05:00 to 10:00)
        endHourCombo.getItems().addAll("05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00");
        endHourCombo.setValue("07:00");
        endHourCombo.setMaxWidth(Double.MAX_VALUE);

        qualityCombo.getItems().addAll(5, 4, 3, 2, 1);
        qualityCombo.setValue(5);
        qualityCombo.setMaxWidth(Double.MAX_VALUE);

        notesField.setPromptText("Sleep notes (e.g. Deep sleep, no waking up)");

        startHourCombo.valueProperty().addListener((o, ov, nv) -> updateDurationPreview());
        endHourCombo.valueProperty().addListener((o, ov, nv) -> updateDurationPreview());

        durationPreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #5E4694; -fx-font-size: 13px;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Log Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Bedtime:"), 0, 1);
        grid.add(startHourCombo, 1, 1);
        grid.add(new Label("Wake Up:"), 0, 2);
        grid.add(endHourCombo, 1, 2);
        grid.add(new Label("Quality (1-5 ★):"), 0, 3);
        grid.add(qualityCombo, 1, 3);
        grid.add(new Label("Notes:"), 0, 4);
        grid.add(notesField, 1, 4);

        Button saveBtn = new Button("Add Sleep Record");
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> handleSaveSleep());

        form.getChildren().addAll(formHeader, grid, durationPreviewLabel, saveBtn);
        return form;
    }

    private void updateDurationPreview() {
        int minutes = calculateMinutes();
        durationPreviewLabel.setText("Calculated Duration: " + HealthCalculations.formatDurationHoursMinutes(minutes));
    }

    private int calculateMinutes() {
        LocalTime start = LocalTime.parse(startHourCombo.getValue());
        LocalTime end = LocalTime.parse(endHourCombo.getValue());
        LocalDate base = LocalDate.now();
        LocalDateTime dtStart = LocalDateTime.of(base, start);
        LocalDateTime dtEnd = LocalDateTime.of(end.isBefore(start) ? base.plusDays(1) : base, end);
        return HealthCalculations.calculateSleepDurationMinutes(dtStart, dtEnd);
    }

    private void handleSaveSleep() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        LocalDate date = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();
        LocalTime start = LocalTime.parse(startHourCombo.getValue());
        LocalTime end = LocalTime.parse(endHourCombo.getValue());

        LocalDateTime dtStart = LocalDateTime.of(start.isAfter(end) ? date.minusDays(1) : date, start);
        LocalDateTime dtEnd = LocalDateTime.of(date, end);
        int durationMin = HealthCalculations.calculateSleepDurationMinutes(dtStart, dtEnd);

        SleepLog s = new SleepLog();
        s.setUserId(user.getId());
        s.setSleepStart(dtStart);
        s.setSleepEnd(dtEnd);
        s.setDurationMinutes(durationMin);
        s.setQualityRating(qualityCombo.getValue() != null ? qualityCombo.getValue() : 4);
        s.setSleepPhaseDeepMin((int)(durationMin * 0.25));
        s.setSleepPhaseLightMin((int)(durationMin * 0.75));
        s.setLogDate(date);
        s.setNotes(notesField.getText() != null ? notesField.getText().trim() : "Night Sleep");

        boolean ok = sleepDao.addSleepLog(s);
        if (ok) {
            notesField.clear();
            refresh();
        }
    }

    private VBox buildTableCard() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(18));

        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Sleep Log Records");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        tableHeader.getChildren().addAll(title, IllustrationFactory.createSleepyMoon(26));

        VBox emptyPlaceholder = new VBox(8);
        emptyPlaceholder.setAlignment(Pos.CENTER);
        Label emptyText = new Label("No sleep logs recorded yet. Sweet dreams tonight!");
        emptyText.setStyle("-fx-text-fill: #789088; -fx-font-size: 12px;");
        emptyPlaceholder.getChildren().addAll(IllustrationFactory.createEmptyState(54), emptyText);
        tableView.setPlaceholder(emptyPlaceholder);

        TableColumn<SleepLog, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLogDate().toString()));

        TableColumn<SleepLog, String> startCol = new TableColumn<>("Bedtime");
        startCol.setCellValueFactory(d -> new SimpleStringProperty(DateUtil.formatTime(d.getValue().getSleepStart().toLocalTime())));

        TableColumn<SleepLog, String> endCol = new TableColumn<>("Wake Up");
        endCol.setCellValueFactory(d -> new SimpleStringProperty(DateUtil.formatTime(d.getValue().getSleepEnd().toLocalTime())));

        TableColumn<SleepLog, String> durCol = new TableColumn<>("Duration");
        durCol.setCellValueFactory(d -> new SimpleStringProperty(HealthCalculations.formatDurationHoursMinutes(d.getValue().getDurationMinutes())));

        TableColumn<SleepLog, String> qualCol = new TableColumn<>("Quality");
        qualCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getQualityRating() + " / 5 ★"));

        TableColumn<SleepLog, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNotes() != null ? d.getValue().getNotes() : ""));

        TableColumn<SleepLog, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button delBtn = new Button("Delete");
            {
                delBtn.getStyleClass().add("btn-danger");
                delBtn.setOnAction(e -> {
                    SleepLog item = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this sleep log?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.YES) {
                        sleepDao.deleteSleepLog(item.getId(), authService.getCurrentUser().getId());
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

        tableView.getColumns().addAll(dateCol, startCol, endCol, durCol, qualCol, notesCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(250);

        box.getChildren().addAll(tableHeader, tableView);
        return box;
    }
}
