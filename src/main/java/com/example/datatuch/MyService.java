package com.example.datatuch;

import java.io.IOException;
import java.sql.*;



public class MyService {
    private Connection connection;

    public MyService(Connection connection) {
        this.connection = connection;
    }
    public static void main(String[] args) {
    }
    public static void mostYear(Connection databaseConnection) throws IOException {
        try {
            String sqlQuery = "SELECT EXTRACT(MONTH FROM datasend) AS month, COUNT(*) AS textmess " +
                    "FROM telegramdata " +
                    "WHERE EXTRACT(YEAR FROM datasend) = 2023 " +
                    "GROUP BY EXTRACT(MONTH FROM datasend) " +
                    "ORDER BY month";

            try (PreparedStatement statement = databaseConnection.prepareStatement(sqlQuery);
                 ResultSet resultSet = statement.executeQuery()) {
                // Выводим заголовок таблицы
                System.out.printf("| %-10s | %-15s |\n", "Month", "Message Count");
                System.out.println("|------------|-----------------|");

                // Выводим результаты в консоль
                while (resultSet.next()) {
                    int month = resultSet.getInt("month");
                    int messageCount = resultSet.getInt("textmess");

                    // Выводим строку таблицы
                    System.out.printf("| %-10d | %-15d |\n", month, messageCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}