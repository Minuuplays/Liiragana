package com.langquest.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.langquest.model.CompletedLesson;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:langquest.db";

    public static void initialize() {
        String createUserStats = """
            CREATE TABLE IF NOT EXISTS user_stats (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                total_xp INTEGER NOT NULL DEFAULT 0
            );
            """;

        String createCompletedLessons = """
            CREATE TABLE IF NOT EXISTS completed_lessons (
                lesson_title TEXT PRIMARY KEY,
                score INTEGER NOT NULL,
                completed_at TEXT NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUserStats);
            stmt.execute(createCompletedLessons);
            stmt.execute("INSERT OR IGNORE INTO user_stats (id, total_xp) VALUES (1, 0)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTotalXp() {
        String sql = "SELECT COALESCE(SUM(score), 0) * 10 AS total FROM completed_lessons";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void recordLessonAttempt(String lessonTitle, int score) {
        String selectSql = "SELECT score FROM completed_lessons WHERE lesson_title = ?";
        String insertSql = "INSERT INTO completed_lessons (lesson_title, score, completed_at) VALUES (?, ?, ?)";
        String updateSql = "UPDATE completed_lessons SET score = ?, completed_at = ? WHERE lesson_title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            Integer existingScore = null;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, lessonTitle);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) existingScore = rs.getInt("score");
                }
            }

            if (existingScore == null) {
                // First attempt at this lesson — record it, award full XP.
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, lessonTitle);
                    ps.setInt(2, score);
                    ps.setString(3, java.time.LocalDateTime.now().toString());
                    ps.executeUpdate();
                }

            } else if (score > existingScore) {
                // Beat the previous best — update the record, award only the improvement.
                int xpDelta = (score - existingScore) * 10;

                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, score);
                    ps.setString(2, java.time.LocalDateTime.now().toString());
                    ps.setString(3, lessonTitle);
                    ps.executeUpdate();
                }
            }
            // else: not an improvement — no changes at all, exactly as you asked for.

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCompletedLessonTitles() {
        List<String> titles = new ArrayList<>();
        String sql = "SELECT lesson_title FROM completed_lessons";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) titles.add(rs.getString("lesson_title"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    public static int getCompletedLessonCount() {
        return getCompletedLessonTitles().size();
    }

    public static List<CompletedLesson> getCompletedLessons() {
        List<CompletedLesson> lessons = new ArrayList<>();
        String sql = "SELECT lesson_title, score, completed_at FROM completed_lessons ORDER BY completed_at DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lessons.add(new CompletedLesson(
                        rs.getString("lesson_title"),
                        rs.getInt("score"),
                        rs.getString("completed_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public static void deleteCompletedLesson(String lessonTitle) {
        String sql = "DELETE FROM completed_lessons WHERE lesson_title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lessonTitle);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}