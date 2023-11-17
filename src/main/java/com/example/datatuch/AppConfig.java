package com.example.datatuch;


import org.postgresql.jdbc.PgConnection;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.example.datatuch.AppConfig;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    private static Connection connection;

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/1613", "postgres", "1488");

            String jsonFilePath = "tgData/1111.json";

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
            JsonNode listNode = rootNode.get("messages");

            for (JsonNode node : listNode) {
                JsonNode dateNode = node.get("date");
                JsonNode fromNode = node.get("from");
                JsonNode textNode = node.get("text");

                if (dateNode != null && fromNode != null && textNode != null) {
                    String date = dateNode.asText();
                    String from = fromNode.asText();
                    String text = textNode.asText();

                    String sql = "INSERT INTO telegramdata (datasend, fromusers, textmass) VALUES (?, ?, ?)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, date);
                        preparedStatement.setString(2, from);
                        preparedStatement.setString(3, text);

                        preparedStatement.executeUpdate();
                    }
                } else {
                    // Обработка случая, когда один из узлов отсутствует
                    System.out.println("Некоторые узлы отсутствуют в сообщении.");
                }
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}