package com.azure.eventmanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableMongoRepositories
public class EventManagerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EventManagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application Started");
    }
}
