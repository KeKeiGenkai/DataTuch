package com.example.datatuch;


import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();
    }

    public static void main(String[] args) {
        // Параметры подключения к базе данных
        String url = "jdbc:postgresql://localhost:5432/1613";
        String user = "postgres";
        String password = "1488";

        // Путь к вашему JSON файлу
        String jsonFilePath = "D:\\scheduler_start\\JavaTest\\DataTuch\\tgData\\result.json";
        try {
            // Загрузка JDBC драйвера
            Class.forName("org.postgresql.Driver");

            // Установка соединения с базой данных
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                // Чтение JSON файла
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode messagesArray = objectMapper.readTree(new File(jsonFilePath));

                // Перебор сообщений в массиве
                for (JsonNode messageNode : messagesArray) {
                    // Печать содержимого сообщения для отладки
                    System.out.println("Message Content: " + messageNode.toString());

                    // Получение данных из каждого сообщения
                    JsonNode dateNode = messageNode.get("date");
                    JsonNode fromNode = messageNode.get("from");
                    JsonNode textNode = messageNode.get("text");

                    // Проверка наличия узлов в JSON
                    if (dateNode != null && fromNode != null && textNode != null) {
                        String date = dateNode.asText();
                        String from = fromNode.asText();
                        String text = textNode.asText();

                        // SQL-запрос для вставки данных
                        String sql = "INSERT INTO telegramData (date, fromUsers, text) VALUES (?, ?, ?)";

                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setString(1, date);
                            preparedStatement.setString(2, from);
                            preparedStatement.setString(3, text);

                            // Выполнение SQL-запроса
                            preparedStatement.executeUpdate();
                        }
                    } else {
                        System.out.println("One or more required fields are missing in a message.");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}