package com.healthos.controller;

import com.healthos.dao.NutritionDao;
import com.healthos.dao.UserProfileDao;
import com.healthos.model.FoodItem;
import com.healthos.model.Meal;
import com.healthos.model.MealItem;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * HealthOS Nutrition & Meal Tracker View.
 * Search wholesome foods, log breakfast/lunch/dinner/snacks,
 * calculate calories and macronutrients (protein, carbs, fat),
 * and track daily nutrition budgets.
 */
public class NutritionView extends ScrollPane {

    private final AuthService authService = AuthService.getInstance();
    private final NutritionDao nutritionDao = new NutritionDao();
    private final UserProfileDao profileDao = new UserProfileDao();

    private final VBox rootContent = new VBox(22);
    private final TableView<Meal> mealTableView = new TableView<>();

    private final ComboBox<String> mealTypeCombo = new ComboBox<>();
    private final ComboBox<FoodItem> foodItemCombo = new ComboBox<>();
    private final TextField searchField = new TextField();
    private final TextField qtyField = new TextField("1.0");
    private final DatePicker datePicker = new DatePicker(LocalDate.now());

    private final Label caloriesLabel = new Label("0 / 2,000 kcal");
    private final Label proteinLabel = new Label("0 g");
    private final Label carbsLabel = new Label("0 g");
    private final Label fatLabel = new Label("0 g");

    private final ProgressBar calBar = new ProgressBar(0);
    private final ProgressBar proBar = new ProgressBar(0);
    private final ProgressBar carbsBar = new ProgressBar(0);
    private final ProgressBar fatBar = new ProgressBar(0);

    public NutritionView() {
        setFitToWidth(true);
        setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        buildUI();
    }

    public void refresh() {
        User user = authService.getCurrentUser();
        if (user == null) return;
        int userId = user.getId();

        LocalDate selectedDate = datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now();
        List<Meal> meals = nutritionDao.getMealsForDate(userId, selectedDate);
        mealTableView.setItems(FXCollections.observableArrayList(meals));

        // Calculate totals
        double totCal = 0, totPro = 0, totCarb = 0, totFat = 0;
        for (Meal m : meals) {
            totCal += m.getTotalCalories();
            totPro += m.getTotalProtein();
            totCarb += m.getTotalCarbs();
            totFat += m.getTotalFat();
        }

        UserProfile profile = profileDao.getProfile(userId);
        int goalCal = profile != null ? profile.getDailyCalorieGoal() : 2000;

        caloriesLabel.setText((int)totCal + " / " + goalCal + " kcal (" + (int)HealthCalculations.calculateGoalProgress(totCal, goalCal) + "%)");
        proteinLabel.setText((int)totPro + " g (Target: 70g)");
        carbsLabel.setText((int)totCarb + " g (Target: 220g)");
        fatLabel.setText((int)totFat + " g (Target: 55g)");

        calBar.setProgress(Math.min(1.0, totCal / goalCal));
        proBar.setProgress(Math.min(1.0, totPro / 70.0));
        carbsBar.setProgress(Math.min(1.0, totCarb / 220.0));
        fatBar.setProgress(Math.min(1.0, totFat / 55.0));

        // Reload food item dropdown
        List<FoodItem> items = nutritionDao.getAllFoodItems();
        foodItemCombo.setItems(FXCollections.observableArrayList(items));
        if (!items.isEmpty() && foodItemCombo.getValue() == null) {
            foodItemCombo.setValue(items.get(0));
        }
    }

    private void buildUI() {
        rootContent.setPadding(new Insets(28, 32, 40, 32));

        // Header
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label title = new Label("Nutrition & Meal Tracker 🥑");
        title.getStyleClass().add("text-h1");
        Label subtitle = new Label("Fuel your body mindfully with nourishing foods and balanced macronutrient distributions.");
        subtitle.getStyleClass().add("text-muted");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        header.getChildren().addAll(titleBox,
                new HBox(10,
                        IllustrationFactory.createNutrition(54),
                        IllustrationFactory.createStrawberry(44),
                        IllustrationFactory.createCarrot(44)
                )
        );
        rootContent.getChildren().add(header);

        // Daily Macro-nutrient Summary Card
        VBox macroCard = buildMacroSummaryCard();
        rootContent.getChildren().add(macroCard);

        // Meal Entry Form
        VBox formCard = buildMealFormCard();
        rootContent.getChildren().add(formCard);

        // Meals Table Card
        VBox tableCard = buildMealsTableCard();
        rootContent.getChildren().add(tableCard);

        setContent(rootContent);
        refresh();
    }

    private VBox buildMacroSummaryCard() {
        VBox card = new VBox(14);
        card.getStyleClass().addAll("card", "card-pastel-mint");
        card.setPadding(new Insets(20));

        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_LEFT);
        Label cardTitle = new Label("Today's Macronutrient Targets");
        cardTitle.setStyle("-fx-font-weight: 800; -fx-text-fill: #225945; -fx-font-size: 15px;");
        HBox.setHgrow(cardTitle, Priority.ALWAYS);
        top.getChildren().addAll(cardTitle, datePicker);
        datePicker.setOnAction(e -> refresh());

        HBox bars = new HBox(18);
        bars.setAlignment(Pos.CENTER);

        VBox cBox = createMacroBarBox("Calories", caloriesLabel, calBar, "progress-bar-peach", IllustrationFactory.createFlameCalorie(22));
        VBox pBox = createMacroBarBox("Protein", proteinLabel, proBar, "progress-bar-mint", IllustrationFactory.createNutrition(22));
        VBox cbBox = createMacroBarBox("Carbohydrates", carbsLabel, carbsBar, "progress-bar-lavender", IllustrationFactory.createStrawberry(22));
        VBox fBox = createMacroBarBox("Healthy Fats", fatLabel, fatBar, "progress-bar-peach", IllustrationFactory.createCarrot(22));

        bars.getChildren().addAll(cBox, pBox, cbBox, fBox);
        for (javafx.scene.Node n : bars.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
        }

        card.getChildren().addAll(top, bars);
        return card;
    }

    private VBox createMacroBarBox(String title, Label valLbl, ProgressBar bar, String barStyle, javafx.scene.Node mascot) {
        VBox box = new VBox(6);
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #6C7D76;");
        valLbl.setStyle("-fx-font-size: 13.5px; -fx-font-weight: bold; -fx-text-fill: #34433E;");
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.getStyleClass().addAll("progress-bar", barStyle);

        HBox titleRow = new HBox(6, mascot, t);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().addAll(titleRow, valLbl, bar);
        return box;
    }

    private VBox buildMealFormCard() {
        VBox form = new VBox(12);
        form.getStyleClass().add("card");
        form.setPadding(new Insets(18));

        HBox formHeader = new HBox(10);
        formHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Add Food Item to Meal");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        formHeader.getChildren().addAll(title, IllustrationFactory.createCarrot(30));

        mealTypeCombo.getItems().addAll("BREAKFAST", "LUNCH", "DINNER", "SNACK");
        mealTypeCombo.setValue("BREAKFAST");

        searchField.setPromptText("Search food (e.g. Avocado, Salmon)...");
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null || newV.trim().isEmpty()) {
                foodItemCombo.setItems(FXCollections.observableArrayList(nutritionDao.getAllFoodItems()));
            } else {
                foodItemCombo.setItems(FXCollections.observableArrayList(nutritionDao.searchFoodItems(newV)));
            }
            if (!foodItemCombo.getItems().isEmpty()) foodItemCombo.setValue(foodItemCombo.getItems().get(0));
        });

        foodItemCombo.setMaxWidth(Double.MAX_VALUE);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        grid.add(new Label("Meal Category:"), 0, 0);
        grid.add(mealTypeCombo, 1, 0);
        grid.add(new Label("Filter Food:"), 0, 1);
        grid.add(searchField, 1, 1);
        grid.add(new Label("Select Food Item:"), 0, 2);
        grid.add(foodItemCombo, 1, 2);
        grid.add(new Label("Servings / Quantity:"), 0, 3);
        grid.add(qtyField, 1, 3);

        Button addBtn = new Button("Record Meal");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> handleAddMeal());

        form.getChildren().addAll(formHeader, grid, addBtn);
        return form;
    }

    private void handleAddMeal() {
        User user = authService.getCurrentUser();
        FoodItem item = foodItemCombo.getValue();
        if (user == null || item == null) return;

        try {
            double qty = Double.parseDouble(qtyField.getText().trim());
            if (qty <= 0) qty = 1.0;

            Meal meal = new Meal();
            meal.setUserId(user.getId());
            meal.setMealType(mealTypeCombo.getValue());
            meal.setMealDate(datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now());
            meal.setMealTime(LocalTime.now());

            double cal = item.getCalories() * qty;
            double pro = item.getProteinG() * qty;
            double carbs = item.getCarbsG() * qty;
            double fat = item.getFatG() * qty;

            meal.setTotalCalories(cal);
            meal.setTotalProtein(pro);
            meal.setTotalCarbs(carbs);
            meal.setTotalFat(fat);

            MealItem mi = new MealItem(0, 0, item.getId(), item.getName(), qty, cal, pro, carbs, fat);
            meal.setItems(Collections.singletonList(mi));

            boolean ok = nutritionDao.addMeal(meal);
            if (ok) {
                refresh();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid numeric serving quantity.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private VBox buildMealsTableCard() {
        VBox box = new VBox(10);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(18));

        HBox tableHeader = new HBox(10);
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Logged Meals for Selected Date");
        title.getStyleClass().add("text-h3");
        HBox.setHgrow(title, Priority.ALWAYS);
        tableHeader.getChildren().addAll(title, IllustrationFactory.createStrawberry(26));

        VBox emptyPlaceholder = new VBox(8);
        emptyPlaceholder.setAlignment(Pos.CENTER);
        Label emptyText = new Label("No meals logged for this date. Fuel up with delicious nutrition!");
        emptyText.setStyle("-fx-text-fill: #789088; -fx-font-size: 12px;");
        emptyPlaceholder.getChildren().addAll(IllustrationFactory.createEmptyState(54), emptyText);
        mealTableView.setPlaceholder(emptyPlaceholder);

        TableColumn<Meal, String> typeCol = new TableColumn<>("Meal");
        typeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMealType()));

        TableColumn<Meal, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(d -> new SimpleStringProperty(DateUtil.formatTime(d.getValue().getMealTime())));

        TableColumn<Meal, String> calCol = new TableColumn<>("Calories");
        calCol.setCellValueFactory(d -> new SimpleStringProperty((int)d.getValue().getTotalCalories() + " kcal"));

        TableColumn<Meal, String> proCol = new TableColumn<>("Protein");
        proCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTotalProtein() + " g"));

        TableColumn<Meal, String> carbCol = new TableColumn<>("Carbs");
        carbCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTotalCarbs() + " g"));

        TableColumn<Meal, String> fatCol = new TableColumn<>("Fats");
        fatCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTotalFat() + " g"));

        TableColumn<Meal, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button delBtn = new Button("Delete");
            {
                delBtn.getStyleClass().add("btn-danger");
                delBtn.setOnAction(e -> {
                    Meal item = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete this meal?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.YES) {
                        nutritionDao.deleteMeal(item.getId(), authService.getCurrentUser().getId());
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

        mealTableView.getColumns().addAll(typeCol, timeCol, calCol, proCol, carbCol, fatCol, actionCol);
        mealTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mealTableView.setPrefHeight(230);

        box.getChildren().addAll(tableHeader, mealTableView);
        return box;
    }
}
