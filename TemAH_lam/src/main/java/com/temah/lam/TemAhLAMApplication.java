package com.temah.lam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhLAMApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhLAMApplication.class, args);
    }

}
