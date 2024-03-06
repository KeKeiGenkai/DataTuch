package com.example.datatuch;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.datatuch.DataTuchApplication.databaseConnection;

@RestController
class ApiController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Обработка загруженного файла
            AppConfig.data(file, DataTuchApplication.databaseConnection);
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при обработке файла: " + e.getMessage();
        }
        return "Файл успешно загружен и обработан.";
    }

    @PostMapping("/textFromUser")
    public String handleTextFromUserRequest() {
        try {
            MyService.textFromUser(databaseConnection);
            return "Запрос успешно обработан";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при обработке запроса: " + e.getMessage();
        }
    }

    @PostMapping("/mostYear")
    public String handleMostYearRequest() {
        try {
            MyService.mostYear(databaseConnection);
            return "Запрос успешно обработан";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при обработке запроса: " + e.getMessage();
        }
    }

    @PostMapping("/averageCharsPerMessage")
    public String handleAverageCharsPerMessageRequest() {
        try {
            MyService.averageCharsPerMessage(databaseConnection);
            return "Запрос успешно обработан";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при обработке запроса: " + e.getMessage();
        }
    }

    @PostMapping("/messagesWithTextCounts")
    public String handleMessagesWithTextCountsRequest() {
        try {
            MyService.messagesWithTextCounts(databaseConnection);
            return "Запрос успешно обработан";
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при обработке запроса: " + e.getMessage();
        }
    }

    @PostMapping("/clearDatabase")
    public String handleClearDatabaseRequest() {
        try {
            AppConfig.clearDatabase(databaseConnection);
            return "База данных успешно очищена.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при очистке базы данных: " + e.getMessage();
        }
    }

    @PostMapping("/cleanup")
    public String handleCleanupRequest() {
        try {
            DataTuchApplication.cleanup(databaseConnection);
            return "Программа успешно завершена.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при завершении программы: " + e.getMessage();
        }
    }
}