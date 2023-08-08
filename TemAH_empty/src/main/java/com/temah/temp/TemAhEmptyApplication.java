package com.temah.temp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhEmptyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhEmptyApplication.class, args);
    }

}
