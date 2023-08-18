package com.temah.lam.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.common.http.RestTemplateUtils;
import com.temah.lam.dto.AlarmDto;
import com.temah.lam.producer.KafkaProducer;
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

    @Value("${alarm.consumer.restful.enable:false}")
    private boolean restfulPushEnable = false;

    @Value("${alarm.consumer.restful.uri:}")
    private String restfulPushUri = null;

    @Value("${alarm.consumer.kafka.enable:false}")
    private boolean kafkaPushEnable = false;

    @Value("${alarm.consumer.kafka.topic:topic_alarm}")
    private String kafkaPushTopic = null;

    private RestTemplateUtils restTemplateUtils;

    private KafkaProducer kafkaProducer;

    @Autowired
    public void setRestTemplateUtils(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    @Autowired
    public void setKafkaProducer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    private void pushAlarmByRestful(AlarmDto alarm) throws Exception {
        if (restfulPushUri != null && !restfulPushUri.isEmpty()) {
            String body = objectMapper.writeValueAsString(alarm);
            restTemplateUtils.processPost(restfulPushUri, null, RestTemplateUtils.buildStringBodyPostEntity(
                    MediaType.APPLICATION_JSON, null, body, null
            ), String.class);
        } else {
            logger.error("参数'alarm.consumer.restful.uri'未配置");
        }
    }

    private void pushAlarmByKafka(AlarmDto alarm) throws Exception {
        if (kafkaPushTopic != null && !kafkaPushTopic.isEmpty()) {
            String body = objectMapper.writeValueAsString(alarm);
            this.kafkaProducer.send(kafkaPushTopic, null, null, body);
        } else {
            logger.error("参数'alarm.consumer.kafka.topic'未配置");
        }
    }

    private void pushAlarm(AlarmDto alarm) {
        try {
            if (restfulPushEnable) {
                logger.debug("使用Restful API推送告警. {}", alarm);
                pushAlarmByRestful(alarm);
            } else if (kafkaPushEnable) {
                logger.debug("使用Kafka推送告警. {}", alarm);
                pushAlarmByKafka(alarm);
            }
        } catch (Exception e) {
            logger.error("推送告警出错. {}", alarm, e);
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
