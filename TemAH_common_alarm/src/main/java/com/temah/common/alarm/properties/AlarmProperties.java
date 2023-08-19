package com.temah.common.alarm.properties;

/**
 * 涉及告警处理的配置信息
 */
public class AlarmProperties {

    /**
     * 告警消费者配置
     */
    private AlarmProperties.AlarmConsumer consumer = new AlarmProperties.AlarmConsumer();

    /**
     * Adapter配置
     */
    private AlarmProperties.AlarmAdapter adapter = new AlarmProperties.AlarmAdapter();

    public AlarmProperties.AlarmConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(AlarmProperties.AlarmConsumer consumer) {
        this.consumer = consumer;
    }

    public AlarmProperties.AlarmAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(AlarmProperties.AlarmAdapter adapter) {
        this.adapter = adapter;
    }

    public static class AlarmConsumer {
        /**
         * 最终告警消费者的地址
         */
        private String address;
        /**
         * 使用Restful API发送告警到消费者时使用
         */
        private AlarmProperties.RestfulConsumer restful = new AlarmProperties.RestfulConsumer();
        /**
         * 发送告警到Kafka时使用
         */
        private AlarmProperties.kafkaConsumer kafka = new AlarmProperties.kafkaConsumer();

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public AlarmProperties.RestfulConsumer getRestful() {
            return restful;
        }

        public void setRestful(AlarmProperties.RestfulConsumer restful) {
            this.restful = restful;
        }

        public AlarmProperties.kafkaConsumer getKafka() {
            return kafka;
        }

        public void setKafka(AlarmProperties.kafkaConsumer kafka) {
            this.kafka = kafka;
        }
    }

    public static class RestfulConsumer {
        /**
         * 是否使用Restful API发送告警到消费者
         */
        private boolean enable = false;
        /**
         * 告警消费者使用的Restful API地址
         */
        private String uri;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class kafkaConsumer {
        /**
         * 是否将告警发送到Kafka
         */
        private boolean enable = false;
        /**
         * 发送告警到Kafka使用的topic
         */
        private String topic = "topic_alarm";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    public static class AlarmAdapter {
        /**
         * Adapter使用Kafka接收告警时使用
         */
        private AlarmProperties.kafkaAdapter kafka = new AlarmProperties.kafkaAdapter();

        public AlarmProperties.kafkaAdapter getKafka() {
            return kafka;
        }

        public void setKafka(AlarmProperties.kafkaAdapter kafka) {
            this.kafka = kafka;
        }
    }

    public static class kafkaAdapter {
        /**
         * Adapter从Kafka接收告警使用的topic
         */
        private String topic = "topic_alarm";
        /**
         * Adapter从Kafka接收消息使用的group
         */
        private String group = "group_alarm";

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }
}
