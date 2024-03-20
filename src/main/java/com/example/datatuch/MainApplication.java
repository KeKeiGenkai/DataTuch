package com.example.datatuch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class MainApplication {

    public static void main(String[] args) {
        // Запуск Spring Boot
        SpringApplication.run(MainApplication.class, args);

        // Подключение к бд
        DataTuchApplication.connectToDatabase();
    }
}