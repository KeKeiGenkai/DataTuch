package com.example.datatuch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataTuchApplication {

    static Connection databaseConnection;


    public static void main(String[] args) throws IOException {
        connectToDatabase();
    }

    public static void connectToDatabase() {
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


        } catch (SQLException e) {
            System.err.println("Ошибка при создании базы данных: " + e.getMessage());
            e.printStackTrace();}

        try {
            // Подключение к базе данных
            databaseConnection = DriverManager.getConnection(url + dbName, dbUser, dbPassword);
            System.out.println("Успешное подключение к базе данных.");
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cleanup(Connection databaseConnection) {
        try {
            if (DataTuchApplication.databaseConnection != null && !DataTuchApplication.databaseConnection.isClosed()) {
                DataTuchApplication.databaseConnection.close();
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