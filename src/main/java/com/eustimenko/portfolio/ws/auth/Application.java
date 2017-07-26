package com.eustimenko.portfolio.ws.auth;

import com.eustimenko.portfolio.ws.auth.api.configuration.WebSocketConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({WebSocketConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
