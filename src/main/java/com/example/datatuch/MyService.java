package com.example.datatuch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MyService {
    private Connection connection;

    public MyService(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
    }


    public static Map<String, Object> mostYear(Connection databaseConnection) {
        Map<String, Object> result = new LinkedHashMap<>();

        try {
            String sqlQuery = "SELECT EXTRACT(YEAR FROM datasend) AS year, COUNT(*) AS messageCount " +
                    "FROM telegramdata " +
                    "GROUP BY EXTRACT(YEAR FROM datasend) " +
                    "ORDER BY year";

            try (PreparedStatement statement = databaseConnection.prepareStatement(sqlQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                Map<String, Integer> yearMessageCountMap = new LinkedHashMap<>();
                while (resultSet.next()) {
                    int year = resultSet.getInt("year");
                    int messageCount = resultSet.getInt("messageCount");

                    yearMessageCountMap.put(String.valueOf(year), messageCount);
                }

                result.put("mostyear", yearMessageCountMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Integer> textFromUser(Connection databaseConnection) throws IOException {
        Map<String, Integer> result = new LinkedHashMap<>();

        try {
            String sql = "SELECT fromusers, COUNT(*) AS textmass FROM public.telegramdata GROUP BY fromusers";

            try (PreparedStatement statement = databaseConnection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String fromUsers = resultSet.getString("fromusers");
                    int textMass = resultSet.getInt("textmass");
                    result.put(fromUsers, textMass); // Добавляем имя пользователя и количество сообщений в Map
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, Object>> averageCharsPerMessage(Connection databaseConnection) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            String sql = "SELECT fromusers, AVG(LENGTH(textmass)) AS avg_chars_per_message " +
                    "FROM public.telegramdata " +
                    "GROUP BY fromusers";

            try (PreparedStatement statement = databaseConnection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                // Добавляем среднее количество символов на сообщение для каждого пользователя в список
                while (resultSet.next()) {
                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("fromUser", resultSet.getString("fromusers"));
                    user.put("averageCharsPerMessage", resultSet.getDouble("avg_chars_per_message"));
                    result.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map<String, Object>> messagesWithTextCounts(Connection databaseConnection) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            String sql = "SELECT fromusers, " +
                    "SUM(CASE WHEN textmass = 'voice_message' THEN 1 ELSE 0 END) AS messages_with_text_1, " +
                    "SUM(CASE WHEN textmass = 'video_message' THEN 1 ELSE 0 END) AS messages_with_text_2, " +
                    "SUM(CASE WHEN textmass = 'sticker' THEN 1 ELSE 0 END) AS messages_with_text_3 " +
                    "FROM public.telegramdata " +
                    "GROUP BY fromusers";

            try (PreparedStatement statement = databaseConnection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                // Добавляем количество сообщений каждого типа для каждого пользователя в список
                while (resultSet.next()) {
                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("fromUser", resultSet.getString("fromusers"));
                    user.put("voice_message", resultSet.getInt("messages_with_text_1"));
                    user.put("video_message", resultSet.getInt("messages_with_text_2"));
                    user.put("sticker", resultSet.getInt("messages_with_text_3"));
                    result.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}