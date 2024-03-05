package com.example.datatuch;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}