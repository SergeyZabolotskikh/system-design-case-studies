package com.sergeyza.shortenurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sergeyza.shortenurl")
public class ShortenUrlServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortenUrlServiceApplication.class, args);
    }
}