package com.example.datatuch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    public static void main(String[] args) {
    }
        public static void data(){
                try {
                    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/1613", "postgres", "1488");

                    String jsonFilePath = "tgData/result.json";

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

                            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                                preparedStatement.setTimestamp(1, timestamp);
                                preparedStatement.setString(2, from);
                                preparedStatement.setString(3, text);

                                preparedStatement.executeUpdate();
                            }
                        } else {
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