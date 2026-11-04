package com.healthos;

import com.healthos.dao.ActivityDao;
import com.healthos.dao.UserDao;
import com.healthos.database.DatabaseConnection;
import com.healthos.model.ActivityLog;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.reports.HealthReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HealthOS Database & Reporting Smoke Test")
class DatabaseSmokeTest {

    @Test
    @DisplayName("Should initialize database, authenticate seed user, log activity, and generate PDF report")
    void testEndToEndWorkflow() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        assertNotNull(db);
        assertNotNull(db.getStatusMessage());

        UserDao userDao = new UserDao();
        // Authenticate seed user fairy@healthos.app with fairy123
        User fairy = userDao.authenticate("fairy@healthos.app", "fairy123");
        assertNotNull(fairy, "Seed user fairy@healthos.app should authenticate successfully");
        assertEquals("Fairy Soni", fairy.getFullName());

        // Test Activity Logging
        ActivityDao activityDao = new ActivityDao();
        ActivityLog testLog = new ActivityLog(0, fairy.getId(), "YOGA", 5000, 3.2, 45, 180.0,
                LocalDate.now(), LocalTime.now(), 1, "Automated Test Session");
        boolean logAdded = activityDao.addActivity(testLog);
        assertTrue(logAdded, "Activity record should be inserted successfully");

        List<ActivityLog> activities = activityDao.getRecentActivities(fairy.getId(), 5);
        assertFalse(activities.isEmpty(), "Should retrieve recorded activities");

        // Test PDF Report Generation
        HealthReportService reportService = new HealthReportService();
        File tempPdf = new File(System.getProperty("java.io.tmpdir"), "HealthOS_Smoke_Test_Report.pdf");
        UserProfile profile = new UserProfile();
        profile.setUserId(fairy.getId());
        profile.setAge(22);
        profile.setHeightCm(165.0);
        profile.setWeightKg(58.5);

        boolean pdfGenerated = reportService.generateReport(fairy, profile, LocalDate.now().minusDays(7), LocalDate.now(), tempPdf);
        assertTrue(pdfGenerated, "PDF report should generate without error");
        assertTrue(tempPdf.exists(), "Generated PDF file should exist on disk");
        assertTrue(tempPdf.length() > 1000, "Generated PDF file should have non-trivial size");

        // Clean up temp file
        tempPdf.delete();
    }
}
