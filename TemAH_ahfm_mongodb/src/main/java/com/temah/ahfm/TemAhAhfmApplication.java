package com.temah.ahfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAhfmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhAhfmApplication.class, args);
    }

}
