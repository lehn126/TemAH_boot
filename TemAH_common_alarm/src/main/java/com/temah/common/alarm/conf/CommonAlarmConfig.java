package com.temah.common.alarm.conf;

import com.temah.common.alarm.properties.AlarmProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonAlarmConfig {

    @Bean
    @ConfigurationProperties(prefix = "alarm")
    public AlarmProperties alarmProperties() {
        return new AlarmProperties();
    }
}
