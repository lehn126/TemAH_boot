package com.temah.common.kafka;

import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送消息到指定topic
     * @param topic topic name (notnull)
     * @param partition specified partition (nullable)
     * @param key specified message key (nullable)
     * @param data message data
     */
    public void send(String topic, Integer partition, String key, String data) {
        if (key != null) {
            if (partition != null) {
                kafkaTemplate.send(topic, partition, key, data);
            } else {
                kafkaTemplate.send(topic, key, data);
            }
        } else {
            kafkaTemplate.send(topic, data);
        }
    }
}
