package com.temah.adapter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.temah.common.alarm.dispatcher.AlarmDispatcher;
import com.temah.common.alarm.dto.AlarmDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(AlarmService.class);

    private final AlarmDispatcher alarmDispatcher;

    private JavaType alarmListType;

    private JavaType alarmMapListType;

    public AlarmService(AlarmDispatcher alarmDispatcher) {
        this.alarmDispatcher = alarmDispatcher;
    }

    /**
     * 获得解析Alarm列表使用的JavaType
     *
     * @return JavaType
     */
    public JavaType getAlarmListType() {
        if (this.alarmListType == null) {
            this.alarmListType = alarmDispatcher.getObjectMapper().getTypeFactory()
                    .constructCollectionType(List.class, AlarmDto.class);
        }
        return alarmListType;
    }

    /**
     * 获得解析Alarm Map列表使用的JavaType
     *
     * @return JavaType
     */
    public JavaType getAlarmMapListType() {
        if (this.alarmMapListType == null) {
            this.alarmMapListType = alarmDispatcher.getObjectMapper().getTypeFactory()
                    .constructCollectionType(List.class, Map.class);
        }
        return alarmMapListType;
    }

    /**
     * 将字符串解析为Alarm列表
     *
     * @param alarmListStr 字符串
     * @return Alarm列表
     * @throws JsonProcessingException exception
     */
    public List<AlarmDto> decodeAlarmList(String alarmListStr) throws JsonProcessingException {
        if (alarmListStr != null && !alarmListStr.isEmpty()) {
            return alarmDispatcher.getObjectMapper().readValue(alarmListStr, getAlarmListType());
        }
        return null;
    }

    /**
     * 将字符串解析为包含Alarm信息的Map列表
     *
     * @param alarmListStr 字符串
     * @return Alarm列表
     * @throws JsonProcessingException exception
     */
    public List<Map<String, Object>> decodeAlarmMapList(String alarmListStr) throws JsonProcessingException {
        if (alarmListStr != null && !alarmListStr.isEmpty()) {
            return alarmDispatcher.getObjectMapper().readValue(alarmListStr, getAlarmMapListType());
        }
        return null;
    }

    /**
     * 将告警信息列表推送到处理端点（AHFM当前只接受List格式的数据）
     * @param alarms Alarm列表
     */
    public void pushAlarms(List<AlarmDto> alarms) {
        try {
            alarmDispatcher.pushAlarms(alarms);
        } catch (Exception e) {
            logger.error("创建告警出错. {}", alarms, e);
        }
    }
}
