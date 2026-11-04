package com.healthos.controller;

import com.healthos.dao.UserProfileDao;
import com.healthos.dao.WaterDao;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.model.WaterLog;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import com.healthos.util.DateUtil;
import com.healthos.util.HealthCalculations;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * HealthOS Water & Hydration Tracker View.
 * Tracks daily water intake with quick-add preset buttons, custom entry,
 * visual water level completion bar, remaining goal indicators, and history logs.
 */
public class WaterView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final WaterDao waterDao = new WaterDao();
    private final UserProfileDao profileDao = new UserProfileDao();

    private final VBox rootContent = new VBox(22);
    private final TableView<WaterLog> tableView = new TableView<>();

    private final Label currentWaterLabel = new Label("0 ml");
    private final Label goalWaterLabel = new Label("Target: 2,500 ml");
    private final Label remainingWaterLabel = new Label("Remaining: 2,500 ml");
    private final ProgressBar progressBar = new ProgressBar(0);
    private final TextField customMlField = new TextField();

    public WaterView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        int userId = user.getId();

        UserProfile profile = profileDao.getProfile(userId);
        int targetGoal = profile != null ? profile.getDailyWaterGoalMl() : 2500;
        int currentTotal = waterDao.getTodayWaterTotalMl(userId, LocalDate.now());

        currentWaterLabel.setText(String.format("%,d", currentTotal) + " ml");
        goalWaterLabel.setText("Daily Target: " + String.format("%,d", targetGoal) + " ml");

        int remaining = Math.max(0, targetGoal - currentTotal);
        remainingWaterLabel.setText(remaining == 0 ? "Daily Hydration Goal Completed! 🎉" : "Remaining: " + String.format("%,d", remaining) + " ml");

        double progressPct = HealthCalculations.calculateGoalProgress(currentTotal, targetGoal);
        progressBar.setProgress(Math.min(1.0, progressPct / 100.0));

        List<WaterLog> logs = waterDao.getWaterLogs(userId, LocalDate.now());
        tableView.setItems(FXCollections.observableArrayList(logs));
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Hydration & Water Tracker 💧");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Stay replenished throughout your day to nourish alertness, metabolism, and radiant skin.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createWaterDrop(56),
                        IllustrationFactory.createWaterBottle(48)
                )
        );
        rootContent.getChildren().add(header);

        // Visual Water Tracker Card
        VBox waterCard = buildVisualTrackerCard();
        rootContent.getChildren().add(waterCard);

        // Quick Add Buttons Row
        HBox quickAddRow = buildQuickAddRow();
        rootContent.getChildren().add(quickAddRow);

        // Today's Hydration Table
        VBox tableCard = buildTableCard();
        rootContent.getChildren().add(tableCard);

        setContent(rootContent);
        refresh();
    }

    private VBox buildVisualTrackerCard() {
        VBox card = new VBox(14);
        card.getStyleClass().addAll("card", "card-pastel-blue");
        card.setPadding(new Insets(22));

        HBox topRow = new HBox(16);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Node bottleMascot = IllustrationFactory.createWaterBottle(56);

        VBox statsBox = new VBox(4);
        Label header = new Label("Today's Total Hydration");
        header.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #265B88;");
        currentWaterLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: #1C3F5E;");
        statsBox.getChildren().addAll(header, currentWaterLabel);
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        VBox rightStats = new VBox(4);
        rightStats.setAlignment(Pos.CENTER_RIGHT);
        goalWaterLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #265B88;");
        remainingWaterLabel.setStyle("-fx-font-size: 12.5px; -fx-font-weight: 600; -fx-text-fill: #3FA8EB;");
        rightStats.getChildren().addAll(goalWaterLabel, remainingWaterLabel);

        topRow.getChildren().addAll(bottleMascot, statsBox, rightStats);

        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(18);
        progressBar.setStyle("-fx-accent: #3FA8EB;");

        card.getChildren().addAll(topRow, progressBar);
        return card;
    }

    private HBox buildQuickAddRow() {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        Button btn200 = createQuickBtn("+200 ml", "Small Glass", 200);
        Button btn250 = createQuickBtn("+250 ml", "Standard Glass", 250);
        Button btn500 = createQuickBtn("+500 ml", "Water Bottle", 500);
        Button btn750 = createQuickBtn("+750 ml", "Sport Shaker", 750);

        HBox customBox = new HBox(8);
        customBox.setAlignment(Pos.CENTER_LEFT);
        customMlField.setPromptText("Custom ml");
        customMlField.setPrefWidth(110);
        Button customBtn = new Button("+ Add");
        customBtn.getStyleClass().add("btn-primary");
        customBtn.setOnAction(e -> {
            try {
                int ml = Integer.parseInt(customMlField.getText().trim());
                if (ml > 0) {
                    addWater(ml);
                    customMlField.clear();
                }
            } catch (Exception ignore) {}
        });
        customBox.getChildren().addAll(customMlField, customBtn);

        row.getChildren().addAll(btn200, btn250, btn500, btn750, customBox);
        return row;
    }

    private Button createQuickBtn(String title, String subtitle, int ml) {
        Button btn = new Button(title + "\n" + subtitle);
        btn.getStyleClass().add("btn-secondary");
        btn.setStyle("-fx-text-alignment: center; -fx-font-size: 12.5px; -fx-padding: 10 18;");
        btn.setOnAction(e -> addWater(ml));
        return btn;
    }

    private void addWater(int ml) {
        User user = authService.getCurrentUser();
        if (user == null) return;
        waterDao.addWaterLog(new WaterLog(0, user.getId(), ml, LocalDate.now(), LocalTime.now()));
        refresh();
    }

    private VBox buildTableCard() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(18));

        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Today's Hydration Entries");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        tableHeader.getChildren().addAll(title, IllustrationFactory.createWaterDrop(26));

        VBox emptyPlaceholder = new VBox(8);
        emptyPlaceholder.setAlignment(Pos.CENTER);
        Label emptyText = new Label("No water logged yet today. Take a refreshing sip!");
        emptyText.setStyle("-fx-text-fill: #789088; -fx-font-size: 12px;");
        emptyPlaceholder.getChildren().addAll(IllustrationFactory.createEmptyState(54), emptyText);
        tableView.setPlaceholder(emptyPlaceholder);

        TableColumn<WaterLog, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(d -> new SimpleStringProperty(DateUtil.formatTime(d.getValue().getLogTime())));

        TableColumn<WaterLog, String> amtCol = new TableColumn<>("Amount (ml)");
        amtCol.setCellValueFactory(d -> new SimpleStringProperty(String.format("%,d", d.getValue().getAmountMl()) + " ml"));

        TableColumn<WaterLog, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button delBtn = new Button("Delete");
            {
                delBtn.getStyleClass().add("btn-danger");
                delBtn.setOnAction(e -> {
                    WaterLog item = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this water entry?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.YES) {
                        waterDao.deleteWaterLog(item.getId(), authService.getCurrentUser().getId());
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

        tableView.getColumns().addAll(timeCol, amtCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(230);

        box.getChildren().addAll(tableHeader, tableView);
        return box;
    }
}
