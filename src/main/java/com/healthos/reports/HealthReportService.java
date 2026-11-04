package com.healthos.reports;

import com.healthos.dao.ActivityDao;
import com.healthos.dao.NutritionDao;
import com.healthos.dao.ReportDao;
import com.healthos.dao.SleepDao;
import com.healthos.dao.VitalDao;
import com.healthos.dao.WaterDao;
import com.healthos.model.ActivityLog;
import com.healthos.model.Meal;
import com.healthos.model.ReportRecord;
import com.healthos.model.SleepLog;
import com.healthos.model.User;
import com.healthos.model.UserProfile;
import com.healthos.model.VitalLog;
import com.healthos.util.DateUtil;
import com.healthos.util.HealthCalculations;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * HealthOS PDF Report Generator.
 * Uses OpenPDF to produce clean, Apple Health-inspired wellness reports
 * with pastel accents and comprehensive health metrics.
 */
public class HealthReportService {

    private final ActivityDao activityDao = new ActivityDao();
    private final VitalDao vitalDao = new VitalDao();
    private final SleepDao sleepDao = new SleepDao();
    private final WaterDao waterDao = new WaterDao();
    private final NutritionDao nutritionDao = new NutritionDao();
    private final ReportDao reportDao = new ReportDao();

    // Pastel Theme Colors for OpenPDF
    private static final Color COLOR_PRIMARY = new Color(52, 67, 62);      // Dark slate text #34433E
    private static final Color COLOR_TEAL = new Color(120, 189, 177);      // Pastel Teal #78BDB1
    private static final Color COLOR_MINT = new Color(221, 243, 232);      // Soft Mint #DDF3E8
    private static final Color COLOR_PEACH = new Color(252, 232, 221);     // Pastel Peach #FCE8DD
    private static final Color COLOR_BG_LIGHT = new Color(248, 250, 248);  // Warm off-white #F8FAF8
    private static final Color COLOR_BORDER = new Color(227, 236, 231);

    // Fonts
    private static final Font FONT_TITLE = new Font(Font.HELVETICA, 20, Font.BOLD, COLOR_PRIMARY);
    private static final Font FONT_SUBTITLE = new Font(Font.HELVETICA, 11, Font.NORMAL, new Color(108, 125, 118));
    private static final Font FONT_SECTION = new Font(Font.HELVETICA, 13, Font.BOLD, COLOR_PRIMARY);
    private static final Font FONT_HEADER = new Font(Font.HELVETICA, 10, Font.BOLD, COLOR_PRIMARY);
    private static final Font FONT_BODY = new Font(Font.HELVETICA, 9.5f, Font.NORMAL, COLOR_PRIMARY);
    private static final Font FONT_MUTED = new Font(Font.HELVETICA, 8.5f, Font.ITALIC, new Color(120, 135, 130));

    public boolean generateReport(User user, UserProfile profile, LocalDate startDate, LocalDate endDate, File targetFile) {
        if (user == null || targetFile == null) return false;

        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        try (FileOutputStream out = new FileOutputStream(targetFile)) {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Header Banner
            addHeaderBanner(document, user, startDate, endDate);

            // 2. Profile Snapshot & Goals
            addProfileSection(document, profile);

            // 3. Activity Summary Table
            addActivitySection(document, user.getId(), startDate, endDate);

            // 4. Vitals & Biometrics Table
            addVitalsSection(document, user.getId());

            // 5. Sleep Duration & Quality
            addSleepSection(document, user.getId());

            // 6. Nutrition & Meals
            addNutritionSection(document, user.getId(), startDate, endDate);

            // 7. Disclaimer Notice
            addDisclaimer(document);

            document.close();

            // Record in Database
            ReportRecord record = new ReportRecord(0, user.getId(),
                    "HealthOS Wellness Report (" + startDate + " to " + endDate + ")",
                    startDate, endDate, targetFile.getAbsolutePath(), LocalDateTime.now());
            reportDao.addReportRecord(record);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addHeaderBanner(Document doc, User user, LocalDate start, LocalDate end) throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(COLOR_MINT);
        cell.setPadding(16);
        cell.setBorderColor(COLOR_TEAL);
        cell.setBorderWidth(1.5f);

        Paragraph title = new Paragraph("HealthOS • Personal Wellness Summary", FONT_TITLE);
        Paragraph subtitle = new Paragraph("Member: " + user.getFullName() + " (" + user.getEmail() + ")  |  Reporting Window: "
                + start.format(DateUtil.SHORT_DATE) + " – " + end.format(DateUtil.SHORT_DATE) + ", " + end.getYear(), FONT_SUBTITLE);
        cell.addElement(title);
        cell.addElement(subtitle);
        headerTable.addCell(cell);

        doc.add(headerTable);
        doc.add(new Paragraph(" "));
    }

    private void addProfileSection(Document doc, UserProfile p) throws Exception {
        if (p == null) return;
        Paragraph secTitle = new Paragraph("User Profile & Daily Target Baselines", FONT_SECTION);
        secTitle.setSpacingAfter(8);
        doc.add(secTitle);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{25, 25, 25, 25});

        addKeyValCell(table, "Age", p.getAge() + " yrs");
        addKeyValCell(table, "Gender", p.getGender());
        addKeyValCell(table, "Height", p.getHeightCm() + " cm");
        addKeyValCell(table, "Weight", p.getWeightKg() + " kg");

        addKeyValCell(table, "Daily Step Target", p.getDailyStepGoal() + " steps");
        addKeyValCell(table, "Hydration Target", p.getDailyWaterGoalMl() + " ml");
        addKeyValCell(table, "Daily Calorie Budget", p.getDailyCalorieGoal() + " kcal");
        addKeyValCell(table, "Sleep Target", HealthCalculations.formatDurationHoursMinutes(p.getDailySleepGoalMinutes()));

        doc.add(table);
        doc.add(new Paragraph(" "));
    }

    private void addActivitySection(Document doc, int userId, LocalDate start, LocalDate end) throws Exception {
        List<ActivityLog> logs = activityDao.getActivities(userId, start, end);
        Paragraph secTitle = new Paragraph("Activity & Exercise Logs (" + logs.size() + " recorded sessions)", FONT_SECTION);
        secTitle.setSpacingAfter(8);
        doc.add(secTitle);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20, 20, 15, 15, 15, 15});

        addTableHeader(table, "Date");
        addTableHeader(table, "Activity");
        addTableHeader(table, "Steps");
        addTableHeader(table, "Distance");
        addTableHeader(table, "Active Mins");
        addTableHeader(table, "Burned");

        int totalSteps = 0;
        double totalDist = 0;
        double totalCal = 0;

        for (ActivityLog a : logs) {
            table.addCell(createCell(a.getLogDate().toString()));
            table.addCell(createCell(a.getActivityType()));
            table.addCell(createCell(String.valueOf(a.getSteps())));
            table.addCell(createCell(a.getDistanceKm() + " km"));
            table.addCell(createCell(a.getActiveMinutes() + " m"));
            table.addCell(createCell((int)a.getCaloriesBurned() + " kcal"));

            totalSteps += a.getSteps();
            totalDist += a.getDistanceKm();
            totalCal += a.getCaloriesBurned();
        }

        if (logs.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("No activity sessions logged in this period.", FONT_MUTED));
            empty.setColspan(6);
            empty.setPadding(10);
            table.addCell(empty);
        }

        doc.add(table);

        Paragraph totalP = new Paragraph("Period Totals: " + totalSteps + " steps | "
                + Math.round(totalDist * 10.0) / 10.0 + " km traversed | " + (int)totalCal + " estimated active kcal burned", FONT_MUTED);
        totalP.setSpacingBefore(4);
        doc.add(totalP);
        doc.add(new Paragraph(" "));
    }

    private void addVitalsSection(Document doc, int userId) throws Exception {
        List<VitalLog> vitals = vitalDao.getVitals(userId, 5);
        Paragraph secTitle = new Paragraph("Latest Vitals & Biometric Logs", FONT_SECTION);
        secTitle.setSpacingAfter(8);
        doc.add(secTitle);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{24, 15, 16, 15, 15, 15});

        addTableHeader(table, "Timestamp");
        addTableHeader(table, "Heart Rate");
        addTableHeader(table, "Blood Pressure");
        addTableHeader(table, "SpO2");
        addTableHeader(table, "Weight");
        addTableHeader(table, "BMI");

        for (VitalLog v : vitals) {
            table.addCell(createCell(DateUtil.formatTimestamp(v.getLogTimestamp())));
            table.addCell(createCell(v.getHeartRateBpm() != null ? v.getHeartRateBpm() + " BPM" : "—"));
            table.addCell(createCell((v.getSystolicBp() != null && v.getDiastolicBp() != null) ? v.getSystolicBp() + "/" + v.getDiastolicBp() : "—"));
            table.addCell(createCell(v.getBloodOxygenSpo2() != null ? v.getBloodOxygenSpo2() + "%" : "—"));
            table.addCell(createCell(v.getWeightKg() != null ? v.getWeightKg() + " kg" : "—"));
            table.addCell(createCell(v.getBmi() != null ? String.valueOf(v.getBmi()) : "—"));
        }

        if (vitals.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("No biometric readings recorded yet.", FONT_MUTED));
            empty.setColspan(6);
            empty.setPadding(10);
            table.addCell(empty);
        }

        doc.add(table);
        doc.add(new Paragraph(" "));
    }

    private void addSleepSection(Document doc, int userId) throws Exception {
        List<SleepLog> sleepLogs = sleepDao.getSleepLogs(userId, 5);
        Paragraph secTitle = new Paragraph("Recent Sleep History & Sleep Quality", FONT_SECTION);
        secTitle.setSpacingAfter(8);
        doc.add(secTitle);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20, 25, 25, 15, 15});

        addTableHeader(table, "Date");
        addTableHeader(table, "Bedtime");
        addTableHeader(table, "Wake Up");
        addTableHeader(table, "Duration");
        addTableHeader(table, "Quality");

        for (SleepLog s : sleepLogs) {
            table.addCell(createCell(s.getLogDate().toString()));
            table.addCell(createCell(DateUtil.formatTimestamp(s.getSleepStart())));
            table.addCell(createCell(DateUtil.formatTimestamp(s.getSleepEnd())));
            table.addCell(createCell(HealthCalculations.formatDurationHoursMinutes(s.getDurationMinutes())));
            table.addCell(createCell(s.getQualityRating() + " / 5 ★"));
        }

        if (sleepLogs.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("No sleep logs recorded in this period.", FONT_MUTED));
            empty.setColspan(5);
            empty.setPadding(10);
            table.addCell(empty);
        }

        doc.add(table);
        doc.add(new Paragraph(" "));
    }

    private void addNutritionSection(Document doc, int userId, LocalDate start, LocalDate end) throws Exception {
        List<Meal> meals = nutritionDao.getMealsForDate(userId, end);
        Paragraph secTitle = new Paragraph("Nutrition & Meal Log (" + end.format(DateUtil.FRIENDLY_DATE) + ")", FONT_SECTION);
        secTitle.setSpacingAfter(8);
        doc.add(secTitle);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20, 20, 20, 20, 20});

        addTableHeader(table, "Meal");
        addTableHeader(table, "Calories");
        addTableHeader(table, "Protein");
        addTableHeader(table, "Carbs");
        addTableHeader(table, "Fats");

        double cals = 0, pro = 0, carbs = 0, fat = 0;
        for (Meal m : meals) {
            table.addCell(createCell(m.getMealType()));
            table.addCell(createCell((int)m.getTotalCalories() + " kcal"));
            table.addCell(createCell(m.getTotalProtein() + " g"));
            table.addCell(createCell(m.getTotalCarbs() + " g"));
            table.addCell(createCell(m.getTotalFat() + " g"));

            cals += m.getTotalCalories();
            pro += m.getTotalProtein();
            carbs += m.getTotalCarbs();
            fat += m.getTotalFat();
        }

        if (meals.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("No meals logged for this selected day.", FONT_MUTED));
            empty.setColspan(5);
            empty.setPadding(10);
            table.addCell(empty);
        }

        doc.add(table);

        Paragraph nutSummary = new Paragraph("Day Totals: " + (int)cals + " kcal | " + (int)pro + "g Protein | "
                + (int)carbs + "g Carbs | " + (int)fat + "g Fat", FONT_MUTED);
        nutSummary.setSpacingBefore(4);
        doc.add(nutSummary);
        doc.add(new Paragraph(" "));
    }

    private void addDisclaimer(Document doc) throws Exception {
        Paragraph p = new Paragraph("Notice: HealthOS is designed for personal wellness tracking and habit self-monitoring. "
                + "Measurements and calculated indicators are not intended to diagnose, treat, or replace professional medical guidance.", FONT_MUTED);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(18);
        doc.add(p);
    }

    private void addKeyValCell(PdfPTable table, String label, String value) {
        PdfPCell c = new PdfPCell();
        c.setBackgroundColor(COLOR_BG_LIGHT);
        c.setBorderColor(COLOR_BORDER);
        c.setPadding(6);
        c.addElement(new Paragraph(label, FONT_MUTED));
        c.addElement(new Paragraph(value, FONT_HEADER));
        table.addCell(c);
    }

    private void addTableHeader(PdfPTable table, String title) {
        PdfPCell c = new PdfPCell(new Phrase(title, FONT_HEADER));
        c.setBackgroundColor(COLOR_MINT);
        c.setBorderColor(COLOR_BORDER);
        c.setPadding(6);
        table.addCell(c);
    }

    private PdfPCell createCell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text, FONT_BODY));
        c.setBorderColor(COLOR_BORDER);
        c.setPadding(6);
        return c;
    }
}
