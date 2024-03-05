package com.example.datatuch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.datatuch.DataTuchApplication.databaseConnection;

@SpringBootApplication
@RestController
public class MainApplication {

    public static void main(String[] args) {
        // Запуск приложения Spring Boot
        SpringApplication.run(MainApplication.class, args);

        // Подключение к базе данных
        DataTuchApplication.connectToDatabase();
    }
}