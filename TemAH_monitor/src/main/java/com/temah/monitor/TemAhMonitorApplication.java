package com.temah.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemAhMonitorApplication.class, args);
    }

}
