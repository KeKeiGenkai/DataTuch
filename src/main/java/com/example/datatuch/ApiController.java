package com.example.datatuch;


import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import static com.example.datatuch.DataTuchApplication.databaseConnection;


@ResponseBody
@RestController
public class ApiController {

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/getData")
    public ResponseEntity<Map<String, Object>> getData() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");

        try {
            Connection databaseConnection = DataTuchApplication.databaseConnection;
            Map<String, Object> data = new LinkedHashMap<>();
            // Получаем данные из всех методов и добавляем их в общую Map
            data.put("mostyear", MyService.mostYear(databaseConnection));
            data.put("textusers", MyService.textFromUser(databaseConnection));
            data.put("averageCharsPerMessage", MyService.averageCharsPerMessage(databaseConnection));
            data.put("messagesWithTextCounts", MyService.messagesWithTextCounts(databaseConnection));

            return ResponseEntity.ok().headers(headers).body(data);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(null);
        }
    }


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