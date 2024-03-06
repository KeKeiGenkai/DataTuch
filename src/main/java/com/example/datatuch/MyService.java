package com.example.datatuch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class MyService {
    private Connection connection;

    public MyService(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
    }

    public static void mostYear(Connection databaseConnection) throws IOException {
        try {
            String sqlQuery = "SELECT EXTRACT(YEAR FROM datasend) AS year, COUNT(*) AS messageCount " +
                    "FROM telegramdata " +
                    "GROUP BY EXTRACT(YEAR FROM datasend) " +
                    "ORDER BY year";

            try (PreparedStatement statement = databaseConnection.prepareStatement(sqlQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                // Выводим заголовок таблицы
                System.out.printf("| %-10s | %-15s |\n", "Year", "Message Count");
                System.out.println("|------------|-----------------|");

                // Выводим результаты в консоль
                while (resultSet.next()) {
                    int year = resultSet.getInt("year");
                    int messageCount = resultSet.getInt("messageCount");

                    // Выводим строку таблицы
                    System.out.printf("| %-10d | %-15d |\n", year, messageCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void textFromUser(Connection databaseConnection) throws IOException {
        try {


            // Напишите SQL-запрос
            String sql = "SELECT fromusers, COUNT(*) as textmass FROM public.telegramdata GROUP BY fromusers";

            // Выполните запрос и получите результаты
            try (PreparedStatement statement = databaseConnection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                // Выведите заголовок таблицы
                System.out.printf("| %-20s | %-15s |\n", "From User", "Message Count");
                System.out.println("|----------------------|-----------------|");

                // Выведите результаты в консоль
                while (resultSet.next()) {
                    String fromUsers = resultSet.getString("fromusers");
                    int textMass = resultSet.getInt("textmass");

                    // Выведите строку таблицы
                    System.out.printf("| %-20s | %-15d |\n", fromUsers, textMass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void averageCharsPerMessage(Connection databaseConnection) throws IOException {
        try {
            // Напишите SQL-запрос
            String sql = "SELECT fromusers, AVG(LENGTH(textmass)) AS avg_chars_per_message " +
                    "FROM public.telegramdata " +
                    "GROUP BY fromusers";

            // Выполните запрос и получите результаты
            try (PreparedStatement statement = databaseConnection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                // Выведите заголовок таблицы
                System.out.printf("| %-20s | %-25s |\n", "From User", "Average Chars per Message");
                System.out.println("|----------------------|---------------------------|");

                // Выведите результаты в консоль
                while (resultSet.next()) {
                    String fromUsers = resultSet.getString("fromusers");
                    double avgCharsPerMessage = resultSet.getDouble("avg_chars_per_message");

                    // Выведите строку таблицы
                    System.out.printf("| %-20s | %-25.2f |\n", fromUsers, avgCharsPerMessage);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void messagesWithTextCounts(Connection databaseConnection) throws IOException {
        try {
            // Напишите SQL-запрос
            String sql = "SELECT fromusers, " +
                    "SUM(CASE WHEN textmass = 'voice_message' THEN 1 ELSE 0 END) AS messages_with_text_1, " +
                    "SUM(CASE WHEN textmass = 'video_message' THEN 1 ELSE 0 END) AS messages_with_text_2, " +
                    "SUM(CASE WHEN textmass = 'sticker' THEN 1 ELSE 0 END) AS messages_with_text_3 " +
                    "FROM public.telegramdata " +
                    "GROUP BY fromusers";

            // Выполните запрос и получите результаты
            try (PreparedStatement statement = databaseConnection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                // Выведите заголовок таблицы
                System.out.printf("| %-20s | %-25s | %-25s | %-25s |\n", "From User", "voice_message", "video_message", "sticker");
                System.out.println("|----------------------|---------------------------|---------------------------|---------------------------|");

                // Выведите результаты в консоль
                while (resultSet.next()) {
                    String fromUsers = resultSet.getString("fromusers");
                    int messagesWithText1 = resultSet.getInt("messages_with_text_1");
                    int messagesWithText2 = resultSet.getInt("messages_with_text_2");
                    int messagesWithText3 = resultSet.getInt("messages_with_text_3");

                    // Выведите строку таблицы
                    System.out.printf("| %-20s | %-25d | %-25d | %-25d |\n", fromUsers, messagesWithText1, messagesWithText2, messagesWithText3);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}