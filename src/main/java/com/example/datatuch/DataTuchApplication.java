package com.example.datatuch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.datatuch.AppConfig.data;

public class DataTuchApplication {

    static Connection databaseConnection;

    static String jsonFilePath;

    public static void main(String[] args) throws IOException {
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
                data(dbConnection); // Передача пути к файлу JSON в метод data
            } catch (SQLException e) {
                System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при создании базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void addFile(String jsonFilePath) {
        DataTuchApplication.jsonFilePath = jsonFilePath;

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