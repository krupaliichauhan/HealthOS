package com.healthos.controller;

import com.healthos.importer.HealthDataImporter;
import com.healthos.model.User;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * HealthOS Data Import & Interoperability View.
 * Parses and validates CSV activity logs and JSON vitals,
 * attributing records to verified data sources.
 */
public class ImporterView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final HealthDataImporter importer = new HealthDataImporter();

    private final VBox rootContent = new VBox(22);
    private final ListView<String> logListView = new ListView<>();
    private final Label resultSummaryLabel = new Label("Ready to import health datasets.");

    public ImporterView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        buildUI();
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Data Import & Interoperability 📥");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Batch import activity, steps, and vitals from standardized CSV or JSON formats.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createDatabaseServer(52),
                        IllustrationFactory.createEmptyState(48)
                )
        );
        rootContent.getChildren().add(header);

        // Technical Clarification Banner
        VBox noticeBox = new VBox(4);
        noticeBox.getStyleClass().addAll("card", "card-pastel-mint");
        noticeBox.setPadding(new Insets(12, 16, 12, 16));
        Label nTitle = new Label("Standard Interoperability Specification");
        nTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #225945; -fx-font-size: 12px;");
        Label nMsg = new Label("HealthOS Desktop ingests records via open CSV and JSON specifications. Direct wireless mobile sync requires companion bridge exports.");
        nMsg.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #34433E;");
        noticeBox.getChildren().addAll(nTitle, nMsg);
        rootContent.getChildren().add(noticeBox);

        // Action Cards (CSV + JSON)
        HBox cardsRow = new HBox(18);
        VBox csvCard = buildCsvCard();
        VBox jsonCard = buildJsonCard();
        HBox.setHgrow(csvCard, Priority.ALWAYS);
        HBox.setHgrow(jsonCard, Priority.ALWAYS);
        cardsRow.getChildren().addAll(csvCard, jsonCard);
        rootContent.getChildren().add(cardsRow);

        // Validation Logs Card
        VBox logsCard = new VBox(10);
        logsCard.getStyleClass().add("card");
        logsCard.setPadding(new Insets(18));
        Label logTitle = new Label("Import Validation & Ingestion Logs");
        logTitle.getStyleClass().add("text-h3");

        resultSummaryLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4A9C8F;");
        logListView.setPrefHeight(200);

        VBox emptyLogs = new VBox(6);
        emptyLogs.setAlignment(Pos.CENTER);
        Label emptyLogsLbl = new Label("No files imported yet. Select CSV or JSON above.");
        emptyLogsLbl.setStyle("-fx-text-fill: #789088; -fx-font-size: 11.5px;");
        emptyLogs.getChildren().addAll(IllustrationFactory.createEmptyState(44), emptyLogsLbl);
        logListView.setPlaceholder(emptyLogs);

        logsCard.getChildren().addAll(logTitle, resultSummaryLabel, logListView);
        rootContent.getChildren().add(logsCard);

        setContent(rootContent);
    }

    private VBox buildCsvCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(18));

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Activity Logs CSV Ingestion");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        titleRow.getChildren().addAll(title, IllustrationFactory.createSneaker(28));

        Label desc = new Label("Supported columns: activity_type, steps, distance_km, active_minutes, calories_burned, log_date, log_time, notes");
        desc.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #6C7D76;");
        desc.setWrapText(true);

        HBox btnRow = new HBox(10);
        Button importBtn = new Button("📂 Select CSV & Import");
        importBtn.getStyleClass().add("btn-primary");
        importBtn.setOnAction(e -> handleCsvImport());

        Button sampleBtn = new Button("Download Sample CSV");
        sampleBtn.getStyleClass().add("btn-secondary");
        sampleBtn.setOnAction(e -> exportSampleFile("/com/healthos/samples/sample_activities.csv", "sample_activities.csv"));

        btnRow.getChildren().addAll(importBtn, sampleBtn);
        card.getChildren().addAll(titleRow, desc, btnRow);
        return card;
    }

    private VBox buildJsonCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(18));

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Vitals & Biometrics JSON Ingestion");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        titleRow.getChildren().addAll(title, IllustrationFactory.createHeart(28));

        Label desc = new Label("Supported JSON schema fields: heartRateBpm, systolicBp, diastolicBp, bloodOxygenSpo2, weightKg, heightCm, bmi, notes");
        desc.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #6C7D76;");
        desc.setWrapText(true);

        HBox btnRow = new HBox(10);
        Button importBtn = new Button("📂 Select JSON & Import");
        importBtn.getStyleClass().add("btn-primary");
        importBtn.setOnAction(e -> handleJsonImport());

        Button sampleBtn = new Button("Download Sample JSON");
        sampleBtn.getStyleClass().add("btn-secondary");
        sampleBtn.setOnAction(e -> exportSampleFile("/com/healthos/samples/sample_vitals.json", "sample_vitals.json"));

        btnRow.getChildren().addAll(importBtn, sampleBtn);
        card.getChildren().addAll(titleRow, desc, btnRow);
        return card;
    }

    private void handleCsvImport() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        FileChooser fc = new FileChooser();
        fc.setTitle("Select Activity CSV File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        File f = fc.showOpenDialog(getScene().getWindow());

        if (f != null) {
            HealthDataImporter.ImportResult res = importer.importActivityCsv(f, user.getId());
            updateLogView(res, "CSV");
        }
    }

    private void handleJsonImport() {
        User user = authService.getCurrentUser();
        if (user == null) return;

        FileChooser fc = new FileChooser();
        fc.setTitle("Select Vitals JSON File");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        File f = fc.showOpenDialog(getScene().getWindow());

        if (f != null) {
            HealthDataImporter.ImportResult res = importer.importVitalsJson(f, user.getId());
            updateLogView(res, "JSON");
        }
    }

    private void updateLogView(HealthDataImporter.ImportResult res, String type) {
        logListView.getItems().clear();
        logListView.getItems().addAll(res.getLogs());
        resultSummaryLabel.setText(type + " Ingestion Result: " + res.getSuccessCount() + " records successfully saved, " + res.getErrorCount() + " issues.");
    }

    private void exportSampleFile(String resourcePath, String defaultName) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Sample Template");
        fc.setInitialFileName(defaultName);
        File dest = fc.showSaveDialog(getScene().getWindow());
        if (dest != null) {
            try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in != null) {
                    Files.copy(in, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sample file saved to: " + dest.getAbsolutePath(), ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
