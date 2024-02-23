package com.example.datatuch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DataTuchApplication {

    private static Connection databaseConnection;

    static String jsonFilePath;

    public static void main(String[] args) {
        // Параметры подключения к PostgreSQL

        String url = "jdbc:postgresql://localhost:5432/";
        String user = "postgres";
        String password = "1488";

        // Параметры новой базы данных
        String dbName = "tgdata";
        String dbUser = "postgres";
        String dbPassword = "1488";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Создание базы данных
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE " + dbName);
            }

            System.out.println("База данных успешно создана.");

            // Подключение к созданной базе данных
            String dbUrl = url + dbName;
            try (Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                System.out.println("Успешное подключение к базе данных.");

                // Здесь вызывайте методы для выполнения вашей логики приложения
                // Например: runApplication(dbConnection);
            } catch (SQLException e) {
                System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при создании базы данных: " + e.getMessage());
            e.printStackTrace();
        }
        runApplication();
    }

    private static void runApplication() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean exit = false;
            while (!exit) {
                System.out.println("Выберите действие:");
                System.out.println("1. Парсинг TG даты");
                System.out.println("2. *колличетво отправленных сообщений каждым пользователем*");
                System.out.println("3. Статистика");
                System.out.println("4. средняя длинна сообщения");
                System.out.println("5. количество мусора");
                System.out.println("6. Сброс данных");
                System.out.println("7. Путь к json");
                System.out.println("8. Выход");
                String choiceString = reader.readLine();
                if (choiceString != null && !choiceString.isEmpty()) {
                    int choice = Integer.parseInt(choiceString);

                    switch (choice) {
                        case 1 -> AppConfig.data(databaseConnection);
                        case 2 -> MyService.textFromUser(databaseConnection);
                        case 3 -> MyService.mostYear(databaseConnection);
                        case 4 -> MyService.averageCharsPerMessage(databaseConnection);
                        case 5 -> MyService.messagesWithTextCounts(databaseConnection);
                        case 6 -> AppConfig.clearDatabase(databaseConnection);
                        case 7 -> addFile();
                        case 8 -> {
                            cleanup();
                            exit = true; // Устанавливаем флаг для выхода из цикла
                        }
                        default -> System.out.println("Некорректный выбор. Пожалуйста, выберите снова.");
                    }
                } else {
                    System.out.println("Строка с выбором пустая или null. Пожалуйста, введите корректное значение.");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addFile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите путь к файлу:");
        jsonFilePath = sc.nextLine();
    }

    public static void cleanup() {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }

            // Удаляем базу данных tgdata
            String url = "jdbc:postgresql://localhost:5432/";
            String user = "postgres";
            String password = "1488";
            String dbName = "tgdata";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
                System.out.println("База данных " + dbName + " успешно удалена.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении базы данных: " + e.getMessage());
            e.printStackTrace();
        }finally {
            // Завершаем выполнение программы
            System.exit(0);
        }
    }
}