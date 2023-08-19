package com.temah.common.alarm.properties;

/**
 * 涉及告警处理的配置信息
 */
public class AlarmProperties {

    private AlarmProperties.AlarmConsumer consumer = new AlarmProperties.AlarmConsumer();

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
        private String address;
        private AlarmProperties.RestfulConsumer restful = new AlarmProperties.RestfulConsumer();
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
        private boolean enable = false;
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
        private boolean enable = false;
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
        private AlarmProperties.kafkaAdapter kafka = new AlarmProperties.kafkaAdapter();

        public AlarmProperties.kafkaAdapter getKafka() {
            return kafka;
        }

        public void setKafka(AlarmProperties.kafkaAdapter kafka) {
            this.kafka = kafka;
        }
    }

    public static class kafkaAdapter {
        private String topic = "topic_alarm";
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
