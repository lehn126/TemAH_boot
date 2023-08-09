package com.temah.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAuthResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhAuthResourceApplication.class, args);
    }

}
