package com.temah.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAuthClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhAuthClientApplication.class, args);
    }

}
