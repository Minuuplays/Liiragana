package com.langquest.db;

import com.langquest.model.CompletedLesson;
import com.langquest.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:langquest.db";

    public static void initialize() {
        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL
            );
            """;

        String createCompletedLessons = """
            CREATE TABLE IF NOT EXISTS completed_lessons (
                user_id INTEGER NOT NULL,
                lesson_title TEXT NOT NULL,
                score INTEGER NOT NULL,
                completed_at TEXT NOT NULL,
                PRIMARY KEY (user_id, lesson_title),
                FOREIGN KEY (user_id) REFERENCES users(id)
            );
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsers);
            stmt.execute(createCompletedLessons);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User createUser(String username) {
        String sql = "INSERT INTO users (username) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return new User(keys.getInt(1), username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username FROM users ORDER BY username";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static int getTotalXp(int userId) {
        String sql = "SELECT COALESCE(SUM(score), 0) * 10 AS total FROM completed_lessons WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void recordLessonAttempt(int userId, String lessonTitle, int score) {
        String selectSql = "SELECT score FROM completed_lessons WHERE user_id = ? AND lesson_title = ?";
        String insertSql = "INSERT INTO completed_lessons (user_id, lesson_title, score, completed_at) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE completed_lessons SET score = ?, completed_at = ? WHERE user_id = ? AND lesson_title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            Integer existingScore = null;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, userId);
                ps.setString(2, lessonTitle);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) existingScore = rs.getInt("score");
                }
            }

            if (existingScore == null) {
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, userId);
                    ps.setString(2, lessonTitle);
                    ps.setInt(3, score);
                    ps.setString(4, java.time.LocalDateTime.now().toString());
                    ps.executeUpdate();
                }
            } else if (score > existingScore) {
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, score);
                    ps.setString(2, java.time.LocalDateTime.now().toString());
                    ps.setInt(3, userId);
                    ps.setString(4, lessonTitle);
                    ps.executeUpdate();
                }
            }
            // else: not an improvement — no changes.

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<CompletedLesson> getCompletedLessons(int userId) {
        List<CompletedLesson> lessons = new ArrayList<>();
        String sql = "SELECT lesson_title, score, completed_at FROM completed_lessons WHERE user_id = ? ORDER BY completed_at DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lessons.add(new CompletedLesson(
                            rs.getString("lesson_title"),
                            rs.getInt("score"),
                            rs.getString("completed_at")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    public static void deleteCompletedLesson(int userId, String lessonTitle) {
        String sql = "DELETE FROM completed_lessons WHERE user_id = ? AND lesson_title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, lessonTitle);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}