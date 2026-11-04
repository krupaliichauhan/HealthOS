package com.healthos.controller;

import com.healthos.dao.VitalDao;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.model.VitalLog;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * HealthOS Vitals & Biometrics Tracker View.
 * Records heart rate, blood pressure, SpO2, body temperature, weight, height,
 * and automatically calculates BMI with categoric health tracking badges.
 */
public class VitalsView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final VitalDao vitalDao = new VitalDao();

    private final VBox rootContent = new VBox(22);
    private final TableView<VitalLog> tableView = new TableView<>();

    private final TextField hrField = new TextField();
    private final TextField sysField = new TextField();
    private final TextField diaField = new TextField();
    private final TextField spo2Field = new TextField();
    private final TextField tempField = new TextField();
    private final TextField wtField = new TextField();
    private final TextField htField = new TextField();
    private final TextField notesField = new TextField();
    private final Label bmiCalcLabel = new Label("Calculated BMI: —");

    private final LineChart<String, Number> vitalsChart;

    public VitalsView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(50, 150, 10);
        yAxis.setLabel("Value (BPM / mmHg)");
        vitalsChart = new LineChart<>(xAxis, yAxis);
        vitalsChart.setPrefHeight(230);

        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        int userId = user.getId();

        List<VitalLog> logs = vitalDao.getVitals(userId, 30);
        tableView.setItems(FXCollections.observableArrayList(logs));

        // Pre-fill height and weight from profile if empty
        UserProfile p = authService.getCurrentProfile();
        if (p != null) {
            if (wtField.getText().isEmpty() && p.getWeightKg() > 0) wtField.setText(String.valueOf(p.getWeightKg()));
            if (htField.getText().isEmpty() && p.getHeightCm() > 0) htField.setText(String.valueOf(p.getHeightCm()));
            updateBmiPreview();
        }

        // Reload Chart
        vitalsChart.getData().clear();
        XYChart.Series<String, Number> hrSeries = new XYChart.Series<>();
        hrSeries.setName("Heart Rate (BPM)");

        XYChart.Series<String, Number> sysSeries = new XYChart.Series<>();
        sysSeries.setName("Systolic BP (mmHg)");

        for (int i = Math.min(logs.size() - 1, 6); i >= 0; i--) {
            VitalLog v = logs.get(i);
            String dateLabel = DateUtil.formatShort(v.getLogTimestamp().toLocalDate());
            if (v.getHeartRateBpm() != null) hrSeries.getData().add(new XYChart.Data<>(dateLabel, v.getHeartRateBpm()));
            if (v.getSystolicBp() != null) sysSeries.getData().add(new XYChart.Data<>(dateLabel, v.getSystolicBp()));
        }

        vitalsChart.getData().addAll(hrSeries, sysSeries);
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Vitals & Biometrics Tracker ❤️");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Monitor cardiovascular resting heart rate, blood pressure, blood oxygen, and Body Mass Index.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createHeart(56),
                        IllustrationFactory.createThermometer(48)
                )
        );
        rootContent.getChildren().add(header);

        // Disclaimer Notice Banner
        VBox disclaimer = new VBox(4);
        disclaimer.getStyleClass().addAll("card", "card-pastel-peach");
        disclaimer.setPadding(new Insets(12, 16, 12, 16));
        Label disTitle = new Label("Wellness Tracking Notice");
        disTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #8F4C2D; -fx-font-size: 12px;");
        Label disMsg = new Label("HealthOS is an MCA personal tracking ecosystem. These recordings are for wellness habit monitoring and not for medical diagnosis.");
        disMsg.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #5E463A;");
        disclaimer.getChildren().addAll(disTitle, disMsg);
        rootContent.getChildren().add(disclaimer);

        // Split: Form + Trend Chart
        HBox split = new HBox(18);
        VBox formCard = buildFormCard();
        formCard.setPrefWidth(400);

        VBox chartCard = new VBox(10);
        chartCard.getStyleClass().add("card");
        chartCard.setPadding(new Insets(18));
        HBox.setHgrow(chartCard, Priority.ALWAYS);
        Label chartTitle = new Label("Vitals Trends (Heart Rate & Blood Pressure)");
        chartTitle.getStyleClass().add("text-h3");
        chartCard.getChildren().addAll(chartTitle, vitalsChart);

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
        Label title = new Label("Record New Measurement");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        formHeader.getChildren().addAll(title, IllustrationFactory.createThermometer(30));

        hrField.setPromptText("BPM (e.g. 72)");
        sysField.setPromptText("Systolic (e.g. 118)");
        diaField.setPromptText("Diastolic (e.g. 76)");
        spo2Field.setPromptText("SpO2 % (e.g. 98.5)");
        tempField.setPromptText("Temp °C (e.g. 36.6)");
        wtField.setPromptText("Weight kg (e.g. 58.5)");
        htField.setPromptText("Height cm (e.g. 165)");
        notesField.setPromptText("Notes (e.g. Morning resting vitals)");

        wtField.textProperty().addListener((obs, oldV, newV) -> updateBmiPreview());
        htField.textProperty().addListener((obs, oldV, newV) -> updateBmiPreview());

        bmiCalcLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4A9C8F; -fx-font-size: 12.5px;");
        HBox bmiBox = new HBox(10);
        bmiBox.setAlignment(Pos.CENTER_LEFT);
        bmiBox.setStyle("-fx-background-color: #EDF9F2; -fx-padding: 8 12; -fx-background-radius: 12px;");
        bmiBox.getChildren().addAll(IllustrationFactory.createWeighingScale(32), bmiCalcLabel);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Heart Rate:"), 0, 0);
        grid.add(hrField, 1, 0);
        grid.add(new Label("Blood Pressure:"), 0, 1);
        HBox bpBox = new HBox(6, sysField, new Label("/"), diaField);
        bpBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(bpBox, 1, 1);
        grid.add(new Label("Oxygen (SpO2):"), 0, 2);
        grid.add(spo2Field, 1, 2);
        grid.add(new Label("Body Temp:"), 0, 3);
        grid.add(tempField, 1, 3);
        grid.add(new Label("Weight (kg):"), 0, 4);
        grid.add(wtField, 1, 4);
        grid.add(new Label("Height (cm):"), 0, 5);
        grid.add(htField, 1, 5);
        grid.add(new Label("Notes:"), 0, 6);
        grid.add(notesField, 1, 6);

        Button saveBtn = new Button("Record Vitals");
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> handleSaveVitals());

        form.getChildren().addAll(formHeader, grid, bmiBox, saveBtn);
        return form;
    }

    private void updateBmiPreview() {
        try {
            double wt = Double.parseDouble(wtField.getText().trim());
            double ht = Double.parseDouble(htField.getText().trim());
            double bmi = HealthCalculations.calculateBmi(wt, ht);
            String cat = HealthCalculations.getBmiCategory(bmi);
            bmiCalcLabel.setText("Calculated BMI: " + bmi + " (" + cat + ")");
        } catch (Exception e) {
            bmiCalcLabel.setText("Calculated BMI: —");
        }
    }

    private void handleSaveVitals() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        try {
            VitalLog v = new VitalLog();
            v.setUserId(user.getId());
            v.setLogTimestamp(LocalDateTime.now());

            if (!hrField.getText().isEmpty()) v.setHeartRateBpm(Integer.parseInt(hrField.getText().trim()));
            if (!sysField.getText().isEmpty()) v.setSystolicBp(Integer.parseInt(sysField.getText().trim()));
            if (!diaField.getText().isEmpty()) v.setDiastolicBp(Integer.parseInt(diaField.getText().trim()));
            if (!spo2Field.getText().isEmpty()) v.setBloodOxygenSpo2(Double.parseDouble(spo2Field.getText().trim()));
            if (!tempField.getText().isEmpty()) v.setTemperatureCelsius(Double.parseDouble(tempField.getText().trim()));

            if (!wtField.getText().isEmpty()) v.setWeightKg(Double.parseDouble(wtField.getText().trim()));
            if (!htField.getText().isEmpty()) v.setHeightCm(Double.parseDouble(htField.getText().trim()));

            if (v.getWeightKg() != null && v.getHeightCm() != null) {
                v.setBmi(HealthCalculations.calculateBmi(v.getWeightKg(), v.getHeightCm()));
            }

            v.setNotes(notesField.getText() != null ? notesField.getText().trim() : "Manual Vitals Entry");

            boolean ok = vitalDao.addVital(v);
            if (ok) {
                hrField.clear();
                sysField.clear();
                diaField.clear();
                spo2Field.clear();
                tempField.clear();
                notesField.clear();
                refresh();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid numeric values for vital measurements.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private VBox buildTableCard() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(18));

        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Historical Vital Logs");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        tableHeader.getChildren().addAll(title, IllustrationFactory.createHeart(26));

        VBox emptyPlaceholder = new VBox(8);
        emptyPlaceholder.setAlignment(Pos.CENTER);
        Label emptyText = new Label("No vital logs recorded yet. Take a measurement to get started!");
        emptyText.setStyle("-fx-text-fill: #789088; -fx-font-size: 12px;");
        emptyPlaceholder.getChildren().addAll(IllustrationFactory.createEmptyState(54), emptyText);
        tableView.setPlaceholder(emptyPlaceholder);

        TableColumn<VitalLog, String> dateCol = new TableColumn<>("Timestamp");
        dateCol.setCellValueFactory(d -> new SimpleStringProperty(DateUtil.formatTimestamp(d.getValue().getLogTimestamp())));

        TableColumn<VitalLog, String> hrCol = new TableColumn<>("Heart Rate");
        hrCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHeartRateBpm() != null ? d.getValue().getHeartRateBpm() + " BPM" : "—"));

        TableColumn<VitalLog, String> bpCol = new TableColumn<>("Blood Pressure");
        bpCol.setCellValueFactory(d -> new SimpleStringProperty((d.getValue().getSystolicBp() != null && d.getValue().getDiastolicBp() != null) ?
                d.getValue().getSystolicBp() + "/" + d.getValue().getDiastolicBp() + " mmHg" : "—"));

        TableColumn<VitalLog, String> spo2Col = new TableColumn<>("SpO2");
        spo2Col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBloodOxygenSpo2() != null ? d.getValue().getBloodOxygenSpo2() + "%" : "—"));

        TableColumn<VitalLog, String> wtCol = new TableColumn<>("Weight");
        wtCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getWeightKg() != null ? d.getValue().getWeightKg() + " kg" : "—"));

        TableColumn<VitalLog, String> bmiCol = new TableColumn<>("BMI");
        bmiCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getBmi() != null ? d.getValue().getBmi() + " (" + HealthCalculations.getBmiCategory(d.getValue().getBmi()) + ")" : "—"));

        TableColumn<VitalLog, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNotes() != null ? d.getValue().getNotes() : ""));

        TableColumn<VitalLog, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button delBtn = new Button("Delete");
            {
                delBtn.getStyleClass().add("btn-danger");
                delBtn.setOnAction(e -> {
                    VitalLog item = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this vitals log?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.YES) {
                        vitalDao.deleteVital(item.getId(), authService.getCurrentUser().getId());
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

        tableView.getColumns().addAll(dateCol, hrCol, bpCol, spo2Col, wtCol, bmiCol, notesCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(250);

        box.getChildren().addAll(tableHeader, tableView);
        return box;
    }
}
