package com.temah.lam.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.lam.alarm.Alarm;
import com.temah.lam.utils.CustomJacksonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(AlarmService.class);

    private final ObjectMapper objectMapper = new CustomJacksonObjectMapper();

    public void createCpuOverLimitAlarm(String host, Date date, double ratio, double limit) {

        Alarm alarm = Alarm.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("CPU Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        try {
            String body = objectMapper.writeValueAsString(alarm);
            logger.info("创建主机CPU超限告警. {}", body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMemoryOverLimitAlarm(String host, Date date, double ratio, double limit) {
        Alarm alarm = Alarm.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("Memory Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        try {
            String body = objectMapper.writeValueAsString(alarm);
            logger.info("创建主机内存占用超限告警. {}", body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
