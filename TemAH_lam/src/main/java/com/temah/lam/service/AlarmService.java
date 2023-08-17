package com.temah.lam.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.common.http.RestTemplateUtils;
import com.temah.lam.dto.AlarmDto;
import com.temah.lam.utils.CustomJacksonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${lam.push.feign.enable:false}")
    private boolean feignPushEnable = false;

    private RestTemplateUtils restTemplateUtils;

    @Autowired
    public void setRestTemplateUtils(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    private void pushAlarmByRestful(AlarmDto alarm) throws Exception {
        if (restfulPushUri != null && !restfulPushUri.isEmpty()) {
            String body = objectMapper.writeValueAsString(alarm);
            restTemplateUtils.processPost(restfulPushUri, null, RestTemplateUtils.buildStringBodyPostEntity(
                    MediaType.APPLICATION_JSON, null, body, null
            ), String.class);
        } else {
            logger.error("参数'lam.push.restful.uri'未配置");
        }
    }

    private void pushAlarm(AlarmDto alarm) {
        try {
            if (restfulPushEnable) {
                logger.debug("使用Restful API创建告警. {}", alarm);
                pushAlarmByRestful(alarm);
            }
        } catch (Exception e) {
            logger.error("创建告警出错. {}", alarm, e);
        }
    }

    public void createCpuOverLimitAlarm(String host, Date date, double ratio, double limit) {

        AlarmDto alarmDto = AlarmDto.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("CPU Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        logger.info("创建主机CPU超限告警. {}", alarmDto);
        pushAlarm(alarmDto);
    }

    public void createMemoryOverLimitAlarm(String host, Date date, double ratio, double limit) {
        AlarmDto alarmDto = AlarmDto.builder()
                .setEventTime(date)
                .setManagedObject(String.format("OSI_SYSTEM '%s'", host))
                .setAlarmType("EquipmentAlarm")
                .setAdditionalText(String.format("Memory Over limit;host=%s;ratio=%.2f;limit=%.2f;", host, ratio, limit))
                .build();

        logger.info("创建主机内存占用超限告警. {}", alarmDto);
        pushAlarm(alarmDto);
    }
}
