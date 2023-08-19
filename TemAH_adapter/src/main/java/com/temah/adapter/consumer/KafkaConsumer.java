package com.temah.adapter.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.adapter.mapper.AlarmMapper;
import com.temah.adapter.service.AlarmService;
import com.temah.common.alarm.dto.AlarmDto;
import com.temah.common.json.CustomJacksonObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * 将收到的字符串转换为标准格式的告警信息
     * @param msg 收到的字符串
     * @return 标准格式的告警信息
     */
    public List<AlarmDto> mapToAlarm(String msg) {
        List<AlarmDto> alarms = null;
        if (msg != null && !msg.isEmpty()) {
            alarms = new ArrayList<>();
            try {
                // 这里假定发来的是List类型的多个告警信息，真实情况可能为单个告警信息也可能是多个告警信息的列表
                List<Map<String, Object>> msgBody = alarmService.decodeAlarmMapList(msg);
                if (msgBody != null) {
                    for (Map<String, Object> map : msgBody) {
                        AlarmDto alarm = alarmMapper.mapTo(map);
                        if (alarm != null) {
                            alarms.add(alarm);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("解析告警出错. {}", msg, e);
            }
        }
        return alarms;
    }

    /**
     * 消费Kafka topic过来的告警信息
     * @param record message record
     * @param ack ack
     */
    @KafkaListener(topics = "${alarm.adapter.kafka.topic:topic_alarm}",
            groupId = "${alarm.adapter.kafka.group:group_alarm}")
    public void listenGroup(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String msg = record.value();
        logger.info("收到告警: {}", msg);
        List<AlarmDto> alarms = mapToAlarm(msg);
        try {
            if (alarms != null && !alarms.isEmpty()) {
                alarmService.pushAlarms(alarms);
            }
            ack.acknowledge();
        } catch (Exception e) {
            logger.error("推送告警出错. {}", alarms, e);
        }
    }
}
