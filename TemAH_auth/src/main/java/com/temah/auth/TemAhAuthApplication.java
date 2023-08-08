package com.temah.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhAuthApplication.class, args);
    }

}
