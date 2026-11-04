package com.healthos.controller;

import com.healthos.dao.ReportDao;
import com.healthos.model.ReportRecord;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.reports.HealthReportService;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import com.healthos.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * HealthOS PDF Health Reports View.
 * Generates comprehensive OpenPDF reports containing user profiles,
 * activity logs, vitals, sleep, nutrition, and rule-based insights.
 */
public class ReportsView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final HealthReportService reportService = new HealthReportService();
    private final ReportDao reportDao = new ReportDao();

    private final VBox rootContent = new VBox(22);
    private final DatePicker startDatePicker = new DatePicker(LocalDate.now().minusDays(14));
    private final DatePicker endDatePicker = new DatePicker(LocalDate.now());
    private final TableView<ReportRecord> tableView = new TableView<>();
    private final Label statusLabel = new Label();

    public ReportsView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        List<ReportRecord> history = reportDao.getReports(user.getId(), 20);
        tableView.setItems(FXCollections.observableArrayList(history));
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Health & Wellness Reports 📄");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Export beautiful, printable PDF summaries of your health metrics and weekly progress.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createTrophy(54),
                        IllustrationFactory.createDocumentHeart(48)
                )
        );
        rootContent.getChildren().add(header);

        // Generator Card
        VBox genCard = buildGeneratorCard();
        rootContent.getChildren().add(genCard);

        // History Table Card
        VBox tableCard = buildHistoryTableCard();
        rootContent.getChildren().add(tableCard);

        setContent(rootContent);
        refresh();
    }

    private VBox buildGeneratorCard() {
        VBox card = new VBox(14);
        card.getStyleClass().addAll("card", "card-pastel-mint");
        card.setPadding(new Insets(22));

        HBox cardHeader = new HBox(10);
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Generate Custom Wellness PDF Report");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: 800; -fx-text-fill: #225945;");
        HBox.setHgrow(title, Priority.ALWAYS);
        cardHeader.getChildren().addAll(title, IllustrationFactory.createDocumentHeart(32));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(10);

        grid.add(new Label("Start Date:"), 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(new Label("End Date:"), 2, 0);
        grid.add(endDatePicker, 3, 0);

        Button exportBtn = new Button("📄 Export & Save PDF Report");
        exportBtn.getStyleClass().add("btn-primary");
        exportBtn.setOnAction(e -> handleExportPdf());

        statusLabel.setStyle("-fx-font-size: 12.5px; -fx-font-weight: 600; -fx-text-fill: #225945;");
        statusLabel.setVisible(false);

        HBox btnRow = new HBox(14, exportBtn, statusLabel);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(cardHeader, grid, btnRow);
        return card;
    }

    private void handleExportPdf() {
        User user = authService.getCurrentUser();
        UserProfile profile = authService.getCurrentProfile();
        if (user == null) return;

        LocalDate start = startDatePicker.getValue() != null ? startDatePicker.getValue() : LocalDate.now().minusDays(14);
        LocalDate end = endDatePicker.getValue() != null ? endDatePicker.getValue() : LocalDate.now();

        if (start.isAfter(end)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Start date cannot be after end date.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save HealthOS Report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Document (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("HealthOS_Wellness_Report_" + start + "_to_" + end + ".pdf");

        File file = fileChooser.showSaveDialog(getScene().getWindow());
        if (file != null) {
            boolean success = reportService.generateReport(user, profile, start, end, file);
            if (success) {
                statusLabel.setText("✓ Report saved to: " + file.getName());
                statusLabel.setVisible(true);
                refresh();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "PDF report generated successfully! Open file now?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(btn -> {
                    if (btn == ButtonType.YES) {
                        try {
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(file);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } else {
                Alert err = new Alert(Alert.AlertType.ERROR, "Failed to generate PDF report.", ButtonType.OK);
                err.showAndWait();
            }
        }
    }

    private VBox buildHistoryTableCard() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(18));

        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Generated Report History");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        tableHeader.getChildren().addAll(title, IllustrationFactory.createTrophy(26));

        VBox emptyPlaceholder = new VBox(8);
        emptyPlaceholder.setAlignment(Pos.CENTER);
        Label emptyText = new Label("No reports generated yet. Click 'Export & Save' above!");
        emptyText.setStyle("-fx-text-fill: #789088; -fx-font-size: 12px;");
        emptyPlaceholder.getChildren().addAll(IllustrationFactory.createEmptyState(54), emptyText);
        tableView.setPlaceholder(emptyPlaceholder);

        TableColumn<ReportRecord, String> titleCol = new TableColumn<>("Report Title");
        titleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getReportTitle()));

        TableColumn<ReportRecord, String> periodCol = new TableColumn<>("Reporting Window");
        periodCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPeriodStart() + " to " + d.getValue().getPeriodEnd()));

        TableColumn<ReportRecord, String> genCol = new TableColumn<>("Generated Timestamp");
        genCol.setCellValueFactory(d -> new SimpleStringProperty(DateUtil.formatTimestamp(d.getValue().getGeneratedAt())));

        TableColumn<ReportRecord, String> pathCol = new TableColumn<>("File Path");
        pathCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFilePath()));

        TableColumn<ReportRecord, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button openBtn = new Button("Open");
            {
                openBtn.getStyleClass().add("btn-secondary");
                openBtn.setOnAction(e -> {
                    ReportRecord item = getTableView().getItems().get(getIndex());
                    File f = new File(item.getFilePath());
                    if (f.exists() && Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(f);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Alert a = new Alert(Alert.AlertType.WARNING, "File no longer exists at: " + item.getFilePath(), ButtonType.OK);
                        a.showAndWait();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : openBtn);
            }
        });

        tableView.getColumns().addAll(titleCol, periodCol, genCol, pathCol, actionCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(230);

        box.getChildren().addAll(tableHeader, tableView);
        return box;
    }
}
