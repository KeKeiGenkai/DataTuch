package com.example.datatuch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

    @PostMapping("/action/{actionName}")
    public ResponseEntity<String> handleAction(@PathVariable String actionName) {
        String result = "";

        switch(actionName) {

            case "averageCharsPerMessage":
                try {
                    MyService.averageCharsPerMessage(databaseConnection);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "Ошибка при выполнении запроса: " + e.getMessage();
                }
                break;
            case "messagesWithTextCounts":
                try {
                    MyService.messagesWithTextCounts(databaseConnection);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "Ошибка при выполнении запроса: " + e.getMessage();
                }
                break;
            case "clearDatabase":
                AppConfig.clearDatabase(databaseConnection);
                result = "База данных успешно очищена.";
                break;
            case "cleanup":
                DataTuchApplication.cleanup(databaseConnection);
                result = "Программа завершена.";
                break;
            default:
                return ResponseEntity.badRequest().body("Неверный запрос.");
        }


        return ResponseEntity.ok(result);
    }
}