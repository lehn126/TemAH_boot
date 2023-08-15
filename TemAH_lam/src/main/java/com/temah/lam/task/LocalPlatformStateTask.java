package com.temah.lam.task;

import com.temah.lam.collector.LocalPlatformStateCollector;
import com.temah.lam.exec.ExecUtils;
import com.temah.lam.service.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;


@Component("localPlatformStateTask")
public class LocalPlatformStateTask {

    private static final Logger logger = LoggerFactory.getLogger(LocalPlatformStateTask.class);

    AlarmService alarmService;

    @Autowired
    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    public void getUsedCpu(String params) {
        double ratio = LocalPlatformStateCollector.getUsedCpu();
        LocalDateTime time = LocalDateTime.now();
        logger.info("当前CPU占用率{}: {}%", time, ratio);
        if (params != null && !params.isEmpty()) {
            Double limit = Double.valueOf(params);
            if (limit.compareTo(ratio) < 0) {
                logger.info("当前CPU占用: {}, 超过限制: {}, 触发报警", ratio, limit);
                String localHost = ExecUtils.getLocalHost();
                Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
                alarmService.createCpuOverLimitAlarm(localHost!=null?localHost:"localhost", date, ratio, limit);
            }
        }
    }

    public void getUsedMemory(String params) {
        double ratio = LocalPlatformStateCollector.getUsedMemory();
        LocalDateTime time = LocalDateTime.now();
        logger.info("当前内存占用率{}: {}%", time, ratio);
        if (params != null && !params.isEmpty()) {
            Double limit = Double.valueOf(params);
            if (limit.compareTo(ratio) < 0) {
                logger.info("当前内存占用: {}, 超过限制: {}, 触发报警", ratio, limit);
                String localHost = ExecUtils.getLocalHost();
                Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
                alarmService.createMemoryOverLimitAlarm(localHost!=null?localHost:"localhost", date, ratio, limit);
            }
        }
    }
}
