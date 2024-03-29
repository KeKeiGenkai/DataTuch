package com.example.datatuch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AppConfig {

    public static void main(String[] args) {
    }
    public static void data(MultipartFile file, Connection databaseConnection) throws IOException {
        try {
            createTable(databaseConnection);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(file.getInputStream());
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
                    Timestamp timestamp = Timestamp.valueOf(dateTime);

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
        System.out.println("fin");
    }

    public static void clearDatabase(Connection databaseConnection) {
        try {
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