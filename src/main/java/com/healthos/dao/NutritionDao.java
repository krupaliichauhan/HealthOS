package com.healthos.dao;

import com.healthos.database.DatabaseConnection;
import com.healthos.model.FoodItem;
import com.healthos.model.Meal;
import com.healthos.model.MealItem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NutritionDao {

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT id, name, category, serving_size, calories, protein_g, carbs_g, fat_g, fiber_g, is_system, created_by_user_id " +
                "FROM food_items ORDER BY name ASC";
        try (Connection conn = db.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapFoodItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<FoodItem> searchFoodItems(String query) {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT id, name, category, serving_size, calories, protein_g, carbs_g, fat_g, fiber_g, is_system, created_by_user_id " +
                "FROM food_items WHERE LOWER(name) LIKE ? OR LOWER(category) LIKE ? ORDER BY name ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + query.toLowerCase().trim() + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapFoodItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Meal> getMealsForDate(int userId, LocalDate date) {
        List<Meal> list = new ArrayList<>();
        String sql = "SELECT id, user_id, meal_type, meal_date, meal_time, total_calories, total_protein, total_carbs, total_fat " +
                "FROM meals WHERE user_id = ? AND meal_date = ? ORDER BY meal_time ASC";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Meal meal = mapMeal(rs);
                    meal.setItems(getMealItems(conn, meal.getId()));
                    list.add(meal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<MealItem> getMealItems(Connection conn, int mealId) throws SQLException {
        List<MealItem> items = new ArrayList<>();
        String sql = "SELECT mi.id, mi.meal_id, mi.food_item_id, f.name as food_name, mi.quantity, mi.calories, mi.protein_g, mi.carbs_g, mi.fat_g " +
                "FROM meal_items mi JOIN food_items f ON mi.food_item_id = f.id WHERE mi.meal_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MealItem item = new MealItem();
                    item.setId(rs.getInt("id"));
                    item.setMealId(rs.getInt("meal_id"));
                    item.setFoodItemId(rs.getInt("food_item_id"));
                    item.setFoodName(rs.getString("food_name"));
                    item.setQuantity(rs.getDouble("quantity"));
                    item.setCalories(rs.getDouble("calories"));
                    item.setProteinG(rs.getDouble("protein_g"));
                    item.setCarbsG(rs.getDouble("carbs_g"));
                    item.setFatG(rs.getDouble("fat_g"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public boolean addMeal(Meal meal) {
        String sql = "INSERT INTO meals (user_id, meal_type, meal_date, meal_time, total_calories, total_protein, total_carbs, total_fat) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, meal.getUserId());
            ps.setString(2, meal.getMealType());
            ps.setDate(3, Date.valueOf(meal.getMealDate()));
            ps.setTime(4, Time.valueOf(meal.getMealTime()));
            ps.setDouble(5, meal.getTotalCalories());
            ps.setDouble(6, meal.getTotalProtein());
            ps.setDouble(7, meal.getTotalCarbs());
            ps.setDouble(8, meal.getTotalFat());
            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    int mealId = gk.getInt(1);
                    meal.setId(mealId);
                    if (meal.getItems() != null && !meal.getItems().isEmpty()) {
                        String itemSql = "INSERT INTO meal_items (meal_id, food_item_id, quantity, calories, protein_g, carbs_g, fat_g) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement ips = conn.prepareStatement(itemSql)) {
                            for (MealItem item : meal.getItems()) {
                                ips.setInt(1, mealId);
                                ips.setInt(2, item.getFoodItemId());
                                ips.setDouble(3, item.getQuantity());
                                ips.setDouble(4, item.getCalories());
                                ips.setDouble(5, item.getProteinG());
                                ips.setDouble(6, item.getCarbsG());
                                ips.setDouble(7, item.getFatG());
                                ips.addBatch();
                            }
                            ips.executeBatch();
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMeal(int mealId, int userId) {
        String sql = "DELETE FROM meals WHERE id = ? AND user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private FoodItem mapFoodItem(ResultSet rs) throws SQLException {
        FoodItem f = new FoodItem();
        f.setId(rs.getInt("id"));
        f.setName(rs.getString("name"));
        f.setCategory(rs.getString("category"));
        f.setServingSize(rs.getString("serving_size"));
        f.setCalories(rs.getDouble("calories"));
        f.setProteinG(rs.getDouble("protein_g"));
        f.setCarbsG(rs.getDouble("carbs_g"));
        f.setFatG(rs.getDouble("fat_g"));
        f.setFiberG(rs.getDouble("fiber_g"));
        f.setSystem(rs.getBoolean("is_system"));
        int uid = rs.getInt("created_by_user_id");
        if (!rs.wasNull()) f.setCreatedByUserId(uid);
        return f;
    }

    private Meal mapMeal(ResultSet rs) throws SQLException {
        Meal m = new Meal();
        m.setId(rs.getInt("id"));
        m.setUserId(rs.getInt("user_id"));
        m.setMealType(rs.getString("meal_type"));
        Date d = rs.getDate("meal_date");
        if (d != null) m.setMealDate(d.toLocalDate());
        Time t = rs.getTime("meal_time");
        if (t != null) m.setMealTime(t.toLocalTime());
        m.setTotalCalories(rs.getDouble("total_calories"));
        m.setTotalProtein(rs.getDouble("total_protein"));
        m.setTotalCarbs(rs.getDouble("total_carbs"));
        m.setTotalFat(rs.getDouble("total_fat"));
        return m;
    }
}
