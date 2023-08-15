package com.temah.lam.task;

import com.temah.lam.collector.RemotePlatformStateCollector;
import com.temah.lam.service.AlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component("remotePlatformStateTask")
public class RemotePlatformStateTask {

    private static final Logger logger = LoggerFactory.getLogger(RemotePlatformStateTask.class);

    AlarmService alarmService;

    @Autowired
    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    private static class ParamsInfo {
        private String user;
        private String passwd;
        private String host;
        private int port;
        private String limit;

        public String getUser() {
            return user;
        }

        public String getPasswd() {
            return passwd;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getLimit() {
            return limit;
        }

        public static ParamsInfo buildFromParams(String params) {
            if (params != null && params.indexOf("##") > 0) {
                ParamsInfo info = new ParamsInfo();
                String[] array = params.split("##");
                info.user = array[0];
                info.passwd = array[1];
                info.host = array[2];
                String portStr = array[3];
                if (portStr != null && !portStr.isEmpty()) {
                    info.port = Integer.parseInt(portStr);
                } else {
                    info.port = 22;
                }
                info.limit = array[4];
                return info;
            }
            return null;
        }
    }

    public void getUsedCpu(String params) {
        ParamsInfo info = ParamsInfo.buildFromParams(params);
        if (info != null) {
            double ratio = RemotePlatformStateCollector.getUsedCpuWithCMD(
                    info.getUser(), info.getPasswd(), info.getHost(), info.getPort());
            LocalDateTime time = LocalDateTime.now();
            logger.info("远程主机CPU占用率{}: [{}:{}] {}%", time, info.getHost(), info.getPort(), ratio);
            if (info.getLimit() != null && !info.getLimit().isEmpty()) {
                Double limit = Double.valueOf(info.getLimit());
                if (limit.compareTo(ratio) < 0) {
                    logger.info("远程主机CPU占用: {}, 超过限制: {}, 触发报警", ratio, limit);
                    Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
                    alarmService.createCpuOverLimitAlarm(info.host, date, ratio, limit);
                }
            }
        }
    }

    public void getUsedMemory(String params) {
        ParamsInfo info = ParamsInfo.buildFromParams(params);
        if (info != null) {
            double ratio = RemotePlatformStateCollector.getUsedMemoryWithCMD(
                    info.getUser(), info.getPasswd(), info.getHost(), info.getPort());
            LocalDateTime time = LocalDateTime.now();
            logger.info("远程主机内存占用率{}: [{}:{}] {}%", time, info.getHost(), info.getPort(), ratio);
            if (info.getLimit() != null && !info.getLimit().isEmpty()) {
                Double limit = Double.valueOf(info.getLimit());
                if (limit.compareTo(ratio) < 0) {
                    logger.info("远程主机内存占用: {}, 超过限制: {}, 触发报警", ratio, limit);
                    Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
                    alarmService.createMemoryOverLimitAlarm(info.host, date, ratio, limit);
                }
            }
        }
    }
}
