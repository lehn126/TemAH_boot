package com.temah.common.alarm.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.common.alarm.properties.AlarmProperties;
import com.temah.common.http.RestTemplateUtils;
import com.temah.common.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class AlarmDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(AlarmDispatcher.class);

    private AlarmProperties alarmProperties;

    private ObjectMapper objectMapper;

    private RestTemplateUtils restTemplateUtils;

    private final KafkaProducer kafkaProducer;

    public AlarmDispatcher(ObjectMapper objectMapper, RestTemplateUtils restTemplateUtils,
                           KafkaProducer kafkaProducer) {
        this.objectMapper = objectMapper;
        this.restTemplateUtils = restTemplateUtils;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * 注入Alarm配置信息
     *
     * @param alarmProperties alarm配置信息
     */
    @Autowired
    public void setAlarmProperties(AlarmProperties alarmProperties) {
        this.alarmProperties = alarmProperties;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RestTemplateUtils getRestTemplateUtils() {
        return restTemplateUtils;
    }

    public void setRestTemplateUtils(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    public KafkaProducer getKafkaProducer() {
        return kafkaProducer;
    }

    public void setKafkaProducer(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    /**
     * 使用Restful API发送告警到配置的端点
     *
     * @param alarms 告警信息
     * @throws Exception exception
     */
    private void pushAlarmsByRestful(Object alarms) throws Exception {
        String restfulPushUri = alarmProperties.getConsumer().getRestful().getUri();
        if (restfulPushUri != null && !restfulPushUri.isEmpty()) {
            String body = objectMapper.writeValueAsString(alarms);
            restTemplateUtils.processPost(restfulPushUri, null, RestTemplateUtils.buildStringBodyPostEntity(
                    MediaType.APPLICATION_JSON, null, body, null
            ), String.class);
        } else {
            logger.error("参数'alarm.consumer.restful.uri'未配置");
        }
    }

    /**
     * 发送告警到配置的Kafka topic
     *
     * @param alarms 告警信息
     * @throws Exception exception
     */
    private void pushAlarmsByKafka(Object alarms) throws Exception {
        String kafkaPushTopic = alarmProperties.getConsumer().getKafka().getTopic();
        if (kafkaPushTopic != null && !kafkaPushTopic.isEmpty()) {
            String body = objectMapper.writeValueAsString(alarms);
            this.kafkaProducer.send(kafkaPushTopic, null, null, body);
        } else {
            logger.error("参数'alarm.consumer.kafka.topic'未配置");
        }
    }

    /**
     * 根据配置文件中的配置发送告警到对应的端点, 这里需要根据处理端需要的数据类型传入对应类型的告警信息
     *
     * @param alarms 告警信息
     * @throws Exception exception
     */
    public void pushAlarms(Object alarms) throws Exception {
        if (alarmProperties.getConsumer().getKafka().isEnable()) {
            logger.debug("使用Kafka推送告警. {}", alarms);
            pushAlarmsByKafka(alarms);
        } else if (alarmProperties.getConsumer().getRestful().isEnable()) {
            logger.debug("使用Restful API推送告警. {}", alarms);
            pushAlarmsByRestful(alarms);
        }
    }
}
