package com.example.datatuch;

import com.example.datatuch.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Scanner;


@SpringBootApplication
public class DataTuchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataTuchApplication.class, args);
        String url = "jdbc:postgresql://localhost:5432/1613";
        String user = "postgres";
        String pass = "1488";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Login:");
            String login = scanner.nextLine();

            System.out.print("Password:");
            String password = scanner.nextLine();

            System.out.print("Name:");
            String name = scanner.nextLine();

            System.out.print("You age:");
            int age = scanner.nextInt();
            scanner.nextLine();

            System.out.print("You country:");
            String country = scanner.nextLine();

            System.out.print("Phone number:");
            long phonenum = scanner.nextLong();


            String sql = "INSERT INTO \"users\" (login, password, phoneNum, age, country, name) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setLong(3, phonenum);
                preparedStatement.setInt(4, age);
                preparedStatement.setString(5, country);
                preparedStatement.setString(6, name);

                int rowsAffected = preparedStatement.executeUpdate();

                System.out.println("Rows affected: " + rowsAffected);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (scanner != null) {
                    scanner.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AppConfig.data();
    }
}