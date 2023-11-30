package com.example.datatuch;

import org.springframework.stereotype.Service;

import java.sql.*;


@Service
public class MyService {
    public static void main(String[] args) {
    }
    public static void mostYear(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/1613", "postgres", "1488");
            String sqlQuery = "SELECT EXTRACT(MONTH FROM datasend) AS month, COUNT(*) AS textmess " +
                    "FROM telegramdata " +
                    "WHERE EXTRACT(YEAR FROM datasend) = 2022 " +
                    "GROUP BY EXTRACT(MONTH FROM datasend) " +
                    "ORDER BY month";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}