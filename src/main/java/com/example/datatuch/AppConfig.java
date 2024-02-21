package com.example.datatuch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AppConfig {

    public static void main(String[] args) {
    }
        public static void data(Connection databaseConnection) throws IOException {


                try {

                    createDatabase(databaseConnection);
                    createTable(databaseConnection);

                    String jsonFilePath = DataTuchApplication.jsonFilePath;

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
                    JsonNode listNode = rootNode.get("messages");


                    for (JsonNode node : listNode) {
                        JsonNode dateNode = node.get("date");
                        JsonNode fromNode = node.get("from");
                        JsonNode textNode = node.get("text");
                        JsonNode gameTitleNode = node.get("game_title");
                        JsonNode mediaTypeNode = node.get("media_type");
                        JsonNode photoNode = node.get("photo");

                        if (dateNode != null && fromNode != null && textNode != null) {
                            String date = dateNode.asText();
                            String from = fromNode.asText();
                            String gameTitle = (gameTitleNode != null) ? gameTitleNode.asText() : null;
                            String mediaType = (mediaTypeNode != null) ? mediaTypeNode.asText() : null;
                            String photo = (photoNode != null) ? photoNode.asText() : null;
                            String text = (mediaType != null) ? mediaType : (gameTitle != null) ? gameTitle : (photo != null) ? photo : (textNode != null && !textNode.isArray()) ? textNode.asText() : "link or answer";

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(dateTime);



                            String sql = "INSERT INTO telegramdata (datasend, fromusers, textmass) VALUES (?, ?, ?)";

                                try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(sql)) {
                                preparedStatement.setTimestamp(1, timestamp);
                                preparedStatement.setString(2, from);
                                preparedStatement.setString(3, text);

                                preparedStatement.executeUpdate();
                            }
                        } else {
                            System.out.println("Некоторые узлы отсутствуют в сообщении.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    private static void createDatabase(Connection connection) throws SQLException {
        // Создание базы данных, если она ещё не существует
        Statement statement = connection.createStatement();
        // Заменяем IF NOT EXISTS на условие проверки существования базы данных
        ResultSet resultSet = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname='your_database_name'");
        if (!resultSet.next()) {
            statement.executeUpdate("CREATE DATABASE your_database_name");
        }
        resultSet.close();
        statement.close();
    }

    public static void clearDatabase(Connection databaseConnection) {
        try {
            // Выполняем SQL-запрос для удаления всех записей из таблицы
            /*String clearTableQuery = "DELETE FROM telegramdata";
            try (PreparedStatement statement = databaseConnection.prepareStatement(clearTableQuery)) {
                statement.executeUpdate();
            }*/

            // Опционально, если вы хотите очистить также и саму таблицу, раскомментируйте следующий код:

            String clearTableQuery = "TRUNCATE TABLE telegramdata";
            try (PreparedStatement statement = databaseConnection.prepareStatement(clearTableQuery)) {
                statement.executeUpdate();
            }


            System.out.println("База данных успешно очищена.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        // Создание таблицы, если она ещё не существует
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS telegramdata (" +
                "datasend TIMESTAMP," +
                "fromusers VARCHAR(255)," +
                "textmass TEXT" +
                ")");
        statement.close();
    }
    }