package com.temah.lam.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.common.http.RestTemplateUtils;
import com.temah.common.web.RestResult;
import com.temah.lam.dto.AlarmDto;
import com.temah.lam.utils.CustomJacksonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(AlarmService.class);

    private final ObjectMapper objectMapper = new CustomJacksonObjectMapper();

    @Value("${lam.push.restful.enable:false}")
    private boolean restfulPushEnable = false;

    @Value("${lam.push.restful.uri:}")
    private String restfulPushUri = null;

    private void pushAlarmByRestful(String body) {
        if (restfulPushUri != null && !restfulPushUri.isEmpty()) {
            try {
                RestTemplateUtils.processPost(restfulPushUri, null, RestTemplateUtils.buildStringBodyPostEntity(
                        MediaType.APPLICATION_JSON, null, body, null
                ), String.class);
            } catch (Exception e) {
                logger.error("创建告警出错. body={}", body, e);
            }
        } else {
            logger.error("参数'lam.push.restful.uri'未配置");
        }
    }

    public void createCpuOverLimitAlarm(String host, Date date, double ratio, double limit) {

        AlarmDto alarmDto = AlarmDto.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("CPU Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        try {
            String body = objectMapper.writeValueAsString(alarmDto);
            if (restfulPushEnable) {
                logger.info("创建主机CPU超限告警. {}", body);
                pushAlarmByRestful(body);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMemoryOverLimitAlarm(String host, Date date, double ratio, double limit) {
        AlarmDto alarmDto = AlarmDto.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("Memory Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        try {
            String body = objectMapper.writeValueAsString(alarmDto);
                if (restfulPushEnable) {
                logger.info("创建主机内存占用超限告警. {}", body);
                pushAlarmByRestful(body);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
