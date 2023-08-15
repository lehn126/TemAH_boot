package com.temah.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhDemoJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhDemoJwtApplication.class, args);
    }

}
