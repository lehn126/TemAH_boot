package com.temah.lam.scheduling;

import com.temah.lam.model.ScheduleSetting;
import com.temah.lam.service.ScheduleSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务预热
 * spring boot项目启动完成后，加载数据库里状态为正常的定时任务
 */
@Component
public class SysJobRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SysJobRunner.class);

    private CronTaskRegistrar cronTaskRegistrar;

    private ScheduleSettingService settingService;

    @Autowired
    public void setCronTaskRegistrar(CronTaskRegistrar cronTaskRegistrar) {
        this.cronTaskRegistrar = cronTaskRegistrar;
    }

    @Autowired
    public void setSettingService(ScheduleSettingService settingService) {
        this.settingService = settingService;
    }

    @Override
    public void run(String... args) {
        // 初始加载数据库里状态为正常的定时任务
        List<ScheduleSetting> jobList = settingService.selectByJobStatus(1);

        if (jobList != null && !jobList.isEmpty()) {
            for (ScheduleSetting job : jobList) {
                SchedulingRunnable task = new SchedulingRunnable(job.getBeanName(), job.getMethodName(), job.getMethodParams());
                cronTaskRegistrar.addCronTask(task, job.getCronExpression());
            }
            logger.info("定时任务已加载完毕...");
        }
    }
}
