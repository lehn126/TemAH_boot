package com.temah.lam.service;

import com.temah.common.alarm.dispatcher.AlarmDispatcher;
import com.temah.common.alarm.dto.AlarmDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(AlarmService.class);

    private final AlarmDispatcher alarmDispatcher;

    public AlarmService(AlarmDispatcher alarmDispatcher) {
        this.alarmDispatcher = alarmDispatcher;
    }

    private void pushAlarm(AlarmDto alarm) {
        try {
            // 处理端接收的是Alarm List
            alarmDispatcher.pushAlarms(Collections.singletonList(alarm));
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
