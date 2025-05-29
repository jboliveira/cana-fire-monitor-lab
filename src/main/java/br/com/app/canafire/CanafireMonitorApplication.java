package br.com.app.canafire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CanafireMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanafireMonitorApplication.class, args);
    }
}

