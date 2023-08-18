package com.temah.adapter.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.adapter.dto.AlarmDto;
import com.temah.adapter.mapper.AlarmMapper;
import com.temah.adapter.service.AlarmService;
import com.temah.common.json.CustomJacksonObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final ObjectMapper objectMapper = new CustomJacksonObjectMapper();

    private AlarmMapper alarmMapper;

    private AlarmService alarmService;

    public KafkaConsumer(AlarmMapper alarmMapper, AlarmService alarmService) {
        this.alarmMapper = alarmMapper;
        this.alarmService = alarmService;
    }

    public AlarmDto mapToAlarm(String msg) {
        AlarmDto alarm = null;
        if (msg != null && !msg.isEmpty()) {
            try {
                Map<String, Object> msgBody = objectMapper.readValue(msg, Map.class);
                alarm = alarmMapper.mapTo(msgBody);
            } catch (Exception e) {
                logger.error("解析告警出错. {}", msg, e);
            }
        }
        return alarm;
    }

    @KafkaListener(topics = "${alarm.adapter.kafka.topic:topic_alarm}",
            groupId = "${alarm.adapter.kafka.group:group_alarm}")
    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String msg = record.value();
        logger.info("收到告警: {}", msg);
        AlarmDto alarm = mapToAlarm(msg);
        if (alarm != null) {
            try {
                alarmService.pushAlarm(alarm);
            } catch (Exception e) {
                logger.error("推送告警出错. {}", alarm, e);
            }
        }
        ack.acknowledge();
    }
}
