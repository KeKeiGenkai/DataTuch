package com.example.datatuch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataTuchApplication {

    private static Connection databaseConnection;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/1613", "postgres", "1488")) {
            databaseConnection = connection;
            runApplication();
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runApplication() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Выберите действие:");
                System.out.println("1. Парсинг TG даты");
                System.out.println("2. *больше нет*");
                System.out.println("3. Статистика");

                String choiceString = reader.readLine();
                int choice = Integer.parseInt(choiceString);

                switch (choice) {
                    case 1 -> AppConfig.data(databaseConnection);
                    case 2 -> System.out.println("lol");
                    case 3 -> MyService.mostYear(databaseConnection);
                    default -> System.out.println("Некорректный выбор. Пожалуйста, выберите снова.");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        }
    }
}