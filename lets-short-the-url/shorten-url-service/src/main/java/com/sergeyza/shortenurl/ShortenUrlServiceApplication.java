package com.sergeyza.shortenurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.sergeyza.shortenurl")
@EnableJpaRepositories(basePackages = "com.sergeyza.shortenurl.persistence.repository")
@EntityScan(basePackages = "com.sergeyza.shortenurl.persistence.entity")
public class ShortenUrlServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortenUrlServiceApplication.class, args);
    }
}
