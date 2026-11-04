package com.healthos.controller;

import com.healthos.model.User;
import com.healthos.service.AuthService;
import com.healthos.ui.IllustrationFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

/**
 * HealthOS Login and Registration View.
 * Elegant pastel split-screen design with wellness illustration,
 * form validation, password toggle, and one-click demo credentials.
 */
public class LoginView extends StackPane {

    private final AuthService authService = AuthService.getInstance();
    private final Consumer<User> onLoginSuccess;

    private boolean isRegisterMode = false;
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final TextField plainPasswordField = new TextField();
    private final TextField nameField = new TextField();
    private final Label errorLabel = new Label();
    private final Button submitBtn = new Button();
    private final Button switchModeBtn = new Button();
    private final Label formTitle = new Label();
    private final Label formSubtitle = new Label();
    private boolean isPasswordVisible = false;

    public LoginView(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #F8FAF8;");
        setPadding(new Insets(30));

        HBox mainContainer = new HBox(40);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxSize(960, 620);
        mainContainer.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 24px; -fx-border-radius: 24px; -fx-border-color: #E6EFE9; -fx-border-width: 1px;");
        mainContainer.setEffect(new DropShadow(24, 0, 8, Color.rgb(52, 67, 62, 0.06)));

        // Left Banner: Pastel Wellness Illustration & Branding
        VBox leftBanner = buildLeftBanner();
        HBox.setHgrow(leftBanner, Priority.ALWAYS);

        // Right Panel: Clean Apple-Inspired Authentication Form
        VBox rightForm = buildRightForm();
        HBox.setHgrow(rightForm, Priority.ALWAYS);

        mainContainer.getChildren().addAll(leftBanner, rightForm);
        getChildren().add(mainContainer);
    }

    private VBox buildLeftBanner() {
        VBox banner = new VBox(20);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(40));
        banner.setStyle("-fx-background-color: #EDF9F2; -fx-background-radius: 24px 0 0 24px;");

        // Brand Logo
        HBox logoBox = (HBox) IllustrationFactory.createAppLogo(140, 42);
        logoBox.setAlignment(Pos.CENTER);

        // Cute Central Illustration
        VBox illContainer = new VBox(15);
        illContainer.setAlignment(Pos.CENTER);
        illContainer.getChildren().addAll(
                IllustrationFactory.createWellnessPlant(130),
                IllustrationFactory.createHeart(60)
        );

        Label quote = new Label("Your Daily Wellness Sanctuary");
        quote.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: #225945;");

        Label quoteSub = new Label("Nurture your daily movement, restful sleep, and mindfulness in one calming pastel ecosystem.");
        quoteSub.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #5E7A6E; -fx-text-alignment: center;");
        quoteSub.setWrapText(true);
        quoteSub.setMaxWidth(300);

        banner.getChildren().addAll(logoBox, illContainer, quote, quoteSub);
        return banner;
    }

    private VBox buildRightForm() {
        VBox form = new VBox(16);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(45, 55, 45, 40));

        formTitle.setText("Welcome Back ✨");
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: 800; -fx-text-fill: #34433E;");

        formSubtitle.setText("Sign in to your HealthOS personal dashboard");
        formSubtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #6C7D76;");

        errorLabel.setStyle("-fx-text-fill: #D94857; -fx-font-size: 12px; -fx-font-weight: 600;");
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);

        // Name Field (only visible in Register mode)
        VBox nameBox = new VBox(6);
        Label nameLbl = new Label("Full Name");
        nameLbl.setStyle("-fx-font-weight: 600; -fx-text-fill: #34433E; -fx-font-size: 12px;");
        nameField.setPromptText("e.g. Fairy Soni");
        nameField.setPrefHeight(40);
        nameBox.getChildren().addAll(nameLbl, nameField);
        nameBox.setVisible(false);
        nameBox.managedProperty().bind(nameBox.visibleProperty());

        // Email Field
        VBox emailBox = new VBox(6);
        Label emailLbl = new Label("Email Address");
        emailLbl.setStyle("-fx-font-weight: 600; -fx-text-fill: #34433E; -fx-font-size: 12px;");
        emailField.setPromptText("fairy@healthos.app");
        emailField.setPrefHeight(40);
        emailBox.getChildren().addAll(emailLbl, emailField);

        // Password Field with Eye Toggle
        VBox passBox = new VBox(6);
        Label passLbl = new Label("Password");
        passLbl.setStyle("-fx-font-weight: 600; -fx-text-fill: #34433E; -fx-font-size: 12px;");

        StackPane passStack = new StackPane();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(40);
        plainPasswordField.setPromptText("Enter your password");
        plainPasswordField.setPrefHeight(40);
        plainPasswordField.setVisible(false);
        plainPasswordField.managedProperty().bind(plainPasswordField.visibleProperty());

        // Synchronize password text
        passwordField.textProperty().bindBidirectional(plainPasswordField.textProperty());
        emailField.setOnAction(e -> handleAuthSubmit());
        passwordField.setOnAction(e -> handleAuthSubmit());
        plainPasswordField.setOnAction(e -> handleAuthSubmit());

        Button eyeToggle = new Button("👁");
        eyeToggle.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 10 0 0;");
        eyeToggle.setOnAction(e -> {
            isPasswordVisible = !isPasswordVisible;
            plainPasswordField.setVisible(isPasswordVisible);
            passwordField.setVisible(!isPasswordVisible);
            eyeToggle.setText(isPasswordVisible ? "🙈" : "👁");
        });
        StackPane.setAlignment(eyeToggle, Pos.CENTER_RIGHT);

        passStack.getChildren().addAll(passwordField, plainPasswordField, eyeToggle);
        passBox.getChildren().addAll(passLbl, passStack);

        // Submit Button
        submitBtn.setText("Sign In to HealthOS");
        submitBtn.getStyleClass().add("btn-primary");
        submitBtn.setPrefHeight(42);
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setOnAction(e -> handleAuthSubmit());

        // Toggle Sign Up / Sign In
        HBox toggleBox = new HBox(6);
        toggleBox.setAlignment(Pos.CENTER);
        Label toggleLbl = new Label("Don't have an account?");
        toggleLbl.setStyle("-fx-text-fill: #6C7D76; -fx-font-size: 12.5px;");

        switchModeBtn.setText("Create One");
        switchModeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4A9C8F; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;");
        switchModeBtn.setOnAction(e -> {
            isRegisterMode = !isRegisterMode;
            nameBox.setVisible(isRegisterMode);
            formTitle.setText(isRegisterMode ? "Create Account 🌸" : "Welcome Back ✨");
            formSubtitle.setText(isRegisterMode ? "Begin your personalized wellness journey" : "Sign in to your HealthOS personal dashboard");
            submitBtn.setText(isRegisterMode ? "Create Account" : "Sign In to HealthOS");
            toggleLbl.setText(isRegisterMode ? "Already have an account?" : "Don't have an account?");
            switchModeBtn.setText(isRegisterMode ? "Sign In" : "Create One");
            errorLabel.setVisible(false);
        });
        toggleBox.getChildren().addAll(toggleLbl, switchModeBtn);

        // One-Click Demo Viva Credentials helper
        VBox demoCard = new VBox(8);
        demoCard.setAlignment(Pos.CENTER);
        demoCard.setPadding(new Insets(12, 16, 12, 16));
        demoCard.setStyle("-fx-background-color: #F0F9F5; -fx-background-radius: 12px; -fx-border-color: #D1EADE; -fx-border-radius: 12px; -fx-border-width: 1px;");

        Label demoTitle = new Label("Demo Credentials (Seed Account)");
        demoTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: 800; -fx-text-fill: #225945;");

        Label demoInfo = new Label("Email: fairy@healthos.app  |  Password: fairy123");
        demoInfo.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #34433E; -fx-font-weight: 600;");

        Button fillDemoBtn = new Button("✨ One-Click Fill: Fairy Soni");
        fillDemoBtn.getStyleClass().add("btn-mint");
        fillDemoBtn.setOnAction(e -> {
            emailField.setText("fairy@healthos.app");
            passwordField.setText("fairy123");
            plainPasswordField.setText("fairy123");
            errorLabel.setVisible(false);
        });
        demoCard.getChildren().addAll(demoTitle, demoInfo, fillDemoBtn);

        form.getChildren().addAll(
                formTitle, formSubtitle, errorLabel,
                nameBox, emailBox, passBox,
                submitBtn, toggleBox, demoCard
        );

        return form;
    }

    private void handleAuthSubmit() {
        errorLabel.setVisible(false);
        String email = emailField.getText() != null ? emailField.getText().trim() : "";
        String pass = isPasswordVisible ?
                (plainPasswordField.getText() != null ? plainPasswordField.getText().trim() : "") :
                (passwordField.getText() != null ? passwordField.getText().trim() : "");

        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please enter both email and password.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (isRegisterMode) {
            String name = nameField.getText() != null ? nameField.getText().trim() : "";
            if (name.isEmpty()) {
                showError("Please enter your full name.");
                return;
            }
            if (pass.length() < 6) {
                showError("Password must be at least 6 characters.");
                return;
            }
            try {
                User user = authService.register(email, pass, name);
                if (user != null) {
                    onLoginSuccess.accept(user);
                } else {
                    showError("Registration failed. Please try again.");
                }
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        } else {
            boolean success = authService.login(email, pass);
            if (success) {
                onLoginSuccess.accept(authService.getCurrentUser());
            } else {
                showError("Invalid email or password. (Hint: Click Quick Fill Demo button below)");
            }
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}
