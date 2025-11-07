package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalTime;

@SpringBootApplication
@RestController
public class DemoApplication {

    @GetMapping("/")
    public String hello() {
        return "Hello from Java Spring Boot! Time: " + LocalTime.now();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
