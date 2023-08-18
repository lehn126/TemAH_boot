package com.temah.adapter.conf;

import com.temah.adapter.mapper.AlarmMapper;
import com.temah.adapter.producer.KafkaProducer;
import com.temah.common.http.RestTemplateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Bean
    public KafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaProducer(kafkaTemplate);
    }

    @Bean
    public AlarmMapper alarmMapper() {
        return new AlarmMapper();
    }
}
