package com.temah.adapter.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.adapter.mapper.AlarmMapper;
import com.temah.common.alarm.dispatcher.AlarmDispatcher;
import com.temah.common.http.RestTemplateUtils;
import com.temah.common.json.CustomJacksonObjectMapper;
import com.temah.common.kafka.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public KafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaProducer(kafkaTemplate);
    }

    @Bean
    public AlarmDispatcher alarmDispatcher(KafkaProducer kafkaProducer) {
        ObjectMapper objectMapper = new CustomJacksonObjectMapper();
        RestTemplateUtils restTemplateUtils = new RestTemplateUtils(new RestTemplate());
        return new AlarmDispatcher(objectMapper, restTemplateUtils, kafkaProducer);
    }

    @Bean
    public AlarmMapper alarmMapper() {
        return new AlarmMapper();
    }
}
