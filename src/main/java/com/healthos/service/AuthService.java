package com.healthos.service;

import com.healthos.dao.UserDao;
import com.healthos.dao.UserProfileDao;
import com.healthos.model.User;
import com.healthos.model.UserProfile;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * HealthOS Authentication & User Session Service.
 * Manages user state, login, registration, and profile preferences.
 */
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private static AuthService instance;
    private final UserDao userDao = new UserDao();
    private final UserProfileDao profileDao = new UserProfileDao();

    private User currentUser;
    private UserProfile currentProfile;

    private AuthService() {}

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean login(String email, String password) {
        User user = userDao.authenticate(email, password);
        if (user != null) {
            this.currentUser = user;
            try {
                this.currentProfile = profileDao.getProfile(user.getId());
            } catch (Exception ex) {
                LOGGER.fine("Notice: Could not load user profile from DB: " + ex.getMessage());
            }
            if (this.currentProfile == null) {
                this.currentProfile = createDefaultProfile(user.getId());
            }
            return true;
        }
        return false;
    }

    public User register(String email, String password, String fullName) throws SQLException {
        User user = userDao.register(email, password, fullName);
        if (user != null) {
            this.currentUser = user;
            try {
                this.currentProfile = profileDao.getProfile(user.getId());
            } catch (Exception ignored) {}
            if (this.currentProfile == null) {
                this.currentProfile = createDefaultProfile(user.getId());
            }
        }
        return user;
    }

    private UserProfile createDefaultProfile(int userId) {
        UserProfile p = new UserProfile();
        p.setUserId(userId);
        p.setAge(22);
        p.setGender("FEMALE");
        p.setHeightCm(165.0);
        p.setWeightKg(58.5);
        p.setDailyStepGoal(10000);
        p.setDailyWaterGoalMl(2500);
        p.setDailyCalorieGoal(2000);
        p.setDailySleepGoalMinutes(480);
        p.setThemePreference("PASTEL_MINT");
        return p;
    }

    public void logout() {
        this.currentUser = null;
        this.currentProfile = null;
    }

    public boolean updateProfile(UserProfile profile) {
        boolean ok = profileDao.updateProfile(profile);
        if (ok) {
            this.currentProfile = profile;
        }
        return ok;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserProfile getCurrentProfile() {
        return currentProfile;
    }
}
