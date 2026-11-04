package com.healthos.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Meal {
    private int id;
    private int userId;
    private String mealType; // BREAKFAST, LUNCH, DINNER, SNACK
    private LocalDate mealDate;
    private LocalTime mealTime;
    private double totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFat;
    private List<MealItem> items = new ArrayList<>();

    public Meal() {
        this.mealDate = LocalDate.now();
        this.mealTime = LocalTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }

    public LocalTime getMealTime() { return mealTime; }
    public void setMealTime(LocalTime mealTime) { this.mealTime = mealTime; }

    public double getTotalCalories() { return totalCalories; }
    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }

    public double getTotalProtein() { return totalProtein; }
    public void setTotalProtein(double totalProtein) { this.totalProtein = totalProtein; }

    public double getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(double totalCarbs) { this.totalCarbs = totalCarbs; }

    public double getTotalFat() { return totalFat; }
    public void setTotalFat(double totalFat) { this.totalFat = totalFat; }

    public List<MealItem> getItems() { return items; }
    public void setItems(List<MealItem> items) { this.items = items; }
}
