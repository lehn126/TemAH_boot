package com.temah.adapter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temah.adapter.dto.AlarmDto;
import com.temah.common.http.RestTemplateUtils;
import com.temah.common.json.CustomJacksonObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

    private static final Logger logger = LoggerFactory.getLogger(AlarmService.class);

    private final ObjectMapper objectMapper = new CustomJacksonObjectMapper();

    @Value("${alarm.consumer.restful.enable:false}")
    private boolean restfulPushEnable = false;

    @Value("${alarm.consumer.restful.uri:}")
    private String restfulPushUri = null;

    private RestTemplateUtils restTemplateUtils;

    @Autowired
    public void setRestTemplateUtils(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
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

    public void pushAlarm(AlarmDto alarm) {
        try {
            if (restfulPushEnable) {
                logger.debug("使用Restful API创建告警. {}", alarm);
                pushAlarmByRestful(alarm);
            }
        } catch (Exception e) {
            logger.error("创建告警出错. {}", alarm, e);
        }
    }
}
