package com.example.datatuch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

public class AppConfig {
    @Bean
    public MyService myService() {return new MyService();
    }

}
