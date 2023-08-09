package com.temah.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhAuthServerApplication.class, args);
    }

}
