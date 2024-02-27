package com.example.datatuch;

import jakarta.servlet.ServletException;

import java.io.*;

import static com.example.datatuch.AppConfig.data;
import static com.example.datatuch.DataTuchApplication.*;
import static spark.Spark.*;
import javax.servlet.http.HttpServletRequest;

public class Main {

    private static String uploadedFilePath;

    public static void main(String[] args) {
        // Установка порта для сервера
        port(8080);

        // Маршрут для обработки HTTP-запросов от HTML-страницы
        post("/upload", (request, response) -> {
            try (InputStream inputStream = request.raw().getPart("file").getInputStream()) {
                // Сохраняем загруженный файл на сервере
                uploadedFilePath = saveUploadedFile(inputStream);
                return "Файл успешно загружен";
            } catch (IOException e) {
                e.printStackTrace();
                response.status(500);
                return "Ошибка загрузки файла";
            }
        });

        // Маршрут для обработки HTTP-запросов от HTML-страницы
        post("/runCase/:caseNumber", (request, response) -> {
            // Получаем номер кейса из запроса
            int caseNumber = Integer.parseInt(request.params(":caseNumber"));
            // В зависимости от номера кейса, вызываем соответствующий метод
            switch (caseNumber) {
                case 1:
                    if (DataTuchApplication.jsonFilePath == null || DataTuchApplication.jsonFilePath.isEmpty()) {
                        return "Путь к JSON файлу не указан";
                    } else {
                        data(databaseConnection);
                        return "Данные успешно обработаны";
                    }
                case 2:
                    MyService.textFromUser(databaseConnection);
                    return "Операция выполнена успешно";
                case 3:
                    MyService.mostYear(databaseConnection);
                    return "Операция выполнена успешно";
                case 4:
                    MyService.averageCharsPerMessage(databaseConnection);
                    return "Операция выполнена успешно";
                case 5:
                    MyService.messagesWithTextCounts(databaseConnection);
                    return "Операция выполнена успешно";
                case 6:
                    AppConfig.clearDatabase(databaseConnection);
                    return "Операция выполнена успешно";
                case 7:
                    try (InputStream inputStream = request.raw().getPart("file").getInputStream()) {
                        String uploadedFilePath = saveUploadedFile(inputStream);
                        // Делайте что-то с uploadedFilePath, если нужно
                        return "Файл успешно загружен";
                    } catch (IOException e) {
                        e.printStackTrace();
                        response.status(500);
                        return "Ошибка загрузки файла";
                    }
                case 8:
                    cleanup();
                    break;
                default:
                    System.out.println("Некорректный номер кейса. Пожалуйста, выберите снова.");
                    return "Некорректный номер кейса";
            }

            // Возвращаем успешный ответ
            return "Кейс выполнен успешно";
        });
    }
    private static String saveUploadedFile(InputStream inputStream) throws IOException {
        // Указываете путь для сохранения файла на сервере
        String uploadDirectory = "/путь_к_папке_для_загрузок";
        String fileName = "uploaded_file.json"; // Имя для сохраняемого файла

        // Создаем объект File для директории загрузок
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // Создаем директорию, если она не существует
        }

        // Создаем объект File для сохраняемого файла
        File file = new File(uploadDir, fileName);

        // Копируем содержимое InputStream в FileOutputStream для сохранения файла
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        String jsonFilePath;
        // Обновляем jsonFilePath, чтобы указать на сохраненный файл
        jsonFilePath = file.getAbsolutePath();

        return jsonFilePath;
    }

}