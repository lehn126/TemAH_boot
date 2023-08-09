package com.temah.resource.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResourceController {

    Logger logger = LoggerFactory.getLogger(TestResourceController.class);

    @GetMapping("/hello")
    public String hello() {
        return "hello form resource server";
    }

}
