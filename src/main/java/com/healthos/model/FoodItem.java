package com.healthos.model;

public class FoodItem {
    private int id;
    private String name;
    private String category;
    private String servingSize;
    private double calories;
    private double proteinG;
    private double carbsG;
    private double fatG;
    private double fiberG;
    private boolean isSystem;
    private Integer createdByUserId;

    public FoodItem() {}

    public FoodItem(int id, String name, String category, String servingSize, double calories, double proteinG, double carbsG, double fatG, double fiberG, boolean isSystem, Integer createdByUserId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.servingSize = servingSize;
        this.calories = calories;
        this.proteinG = proteinG;
        this.carbsG = carbsG;
        this.fatG = fatG;
        this.fiberG = fiberG;
        this.isSystem = isSystem;
        this.createdByUserId = createdByUserId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getServingSize() { return servingSize; }
    public void setServingSize(String servingSize) { this.servingSize = servingSize; }

    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }

    public double getProteinG() { return proteinG; }
    public void setProteinG(double proteinG) { this.proteinG = proteinG; }

    public double getCarbsG() { return carbsG; }
    public void setCarbsG(double carbsG) { this.carbsG = carbsG; }

    public double getFatG() { return fatG; }
    public void setFatG(double fatG) { this.fatG = fatG; }

    public double getFiberG() { return fiberG; }
    public void setFiberG(double fiberG) { this.fiberG = fiberG; }

    public boolean isSystem() { return isSystem; }
    public void setSystem(boolean system) { isSystem = system; }

    public Integer getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Integer createdByUserId) { this.createdByUserId = createdByUserId; }

    @Override
    public String toString() {
        return name + " (" + (int)calories + " kcal / " + servingSize + ")";
    }
}
