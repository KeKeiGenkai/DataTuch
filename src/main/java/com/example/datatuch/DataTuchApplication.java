package com.example.datatuch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@SpringBootApplication
public class DataTuchApplication {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Парсинг TG даты");
            System.out.println("2. Поля регистрации");
            System.out.println("3. Статистика");

            String choiceString = reader.readLine();
            int choice = Integer.parseInt(choiceString);

            switch (choice) {
                case 1:
                    AppConfig.data();
                    break;
                case 2:
                    reg.regist();
                    break;
                case 3:
                    MyService.mostYear();
                    break;
                default:
                    System.out.println("Некорректный выбор. Пожалуйста, выберите снова.");
            }
        }
    }
    }