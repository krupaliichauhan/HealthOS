package com.healthos;

import com.healthos.controller.LoginView;
import com.healthos.controller.MainLayoutView;
import com.healthos.database.DatabaseConnection;
import com.healthos.service.AuthService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * HealthOS Desktop Application Entry Point.
 * Personal Health and Wellness Tracking Ecosystem.
 */
public class HealthOSApp extends Application {

    private final AuthService authService = AuthService.getInstance();
    private Stage primaryStage;
    private StackPane rootContainer;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.rootContainer = new StackPane();

        // Initialize Database in background/early
        DatabaseConnection.getInstance();

        Scene scene = new Scene(rootContainer, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/com/healthos/styles/main.css").toExternalForm());

        stage.setTitle("HealthOS • Personal Health & Wellness Ecosystem");
        stage.setMinWidth(1040);
        stage.setMinHeight(680);
        stage.setScene(scene);

        // Attempt window icon loading if available
        try (InputStream iconStream = getClass().getResourceAsStream("/com/healthos/illustrations/heart_cute.svg")) {
            // JavaFX handles stage display smoothly
        } catch (Exception ignore) {}

        showLoginScreen();
        stage.show();
    }

    public void showLoginScreen() {
        LoginView loginView = new LoginView(user -> showMainApplication());
        rootContainer.getChildren().setAll(loginView);
    }

    public void showMainApplication() {
        MainLayoutView mainLayout = new MainLayoutView(v -> {
            authService.logout();
            showLoginScreen();
        });
        rootContainer.getChildren().setAll(mainLayout);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
