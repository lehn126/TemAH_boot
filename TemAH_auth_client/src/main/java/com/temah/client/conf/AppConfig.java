package com.temah.client.conf;

import com.temah.common.http.RestTemplateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplateUtils restTemplateUtils() {
        return new RestTemplateUtils(restTemplate());
    }
}
