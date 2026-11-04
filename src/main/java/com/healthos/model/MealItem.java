package com.healthos.model;

public class MealItem {
    private int id;
    private int mealId;
    private int foodItemId;
    private String foodName;
    private double quantity;
    private double calories;
    private double proteinG;
    private double carbsG;
    private double fatG;

    public MealItem() {}

    public MealItem(int id, int mealId, int foodItemId, String foodName, double quantity, double calories, double proteinG, double carbsG, double fatG) {
        this.id = id;
        this.mealId = mealId;
        this.foodItemId = foodItemId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.calories = calories;
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }

    public int getFoodItemId() { return foodItemId; }
    public void setFoodItemId(int foodItemId) { this.foodItemId = foodItemId; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }

    public double getProteinG() { return proteinG; }
    public void setProteinG(double proteinG) { this.proteinG = proteinG; }

    public double getCarbsG() { return carbsG; }
    public void setCarbsG(double carbsG) { this.carbsG = carbsG; }

    public double getFatG() { return fatG; }
    public void setFatG(double fatG) { this.fatG = fatG; }
}
