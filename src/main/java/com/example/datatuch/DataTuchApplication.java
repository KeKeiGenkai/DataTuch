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
                System.out.println("2. *колличетво отправленных сообщений каждым пользователем*");
                System.out.println("3. Статистика");
                System.out.println("4. средняя длинна сообщения");
                System.out.println("5. количество мусора");
                System.out.println("6. Сброс данных");

                String choiceString = reader.readLine();
                int choice = Integer.parseInt(choiceString);

                switch (choice) {
                    case 1 -> AppConfig.data(databaseConnection);
                    case 2 -> MyService.textFromUser(databaseConnection);
                    case 3 -> MyService.mostYear(databaseConnection);
                    case 4 -> MyService.averageCharsPerMessage(databaseConnection);
                    case 5 -> MyService.messagesWithTextCounts(databaseConnection);
                    case 6 -> AppConfig.clearDatabase(databaseConnection);
                    default -> System.out.println("Некорректный выбор. Пожалуйста, выберите снова.");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        }
    }
}