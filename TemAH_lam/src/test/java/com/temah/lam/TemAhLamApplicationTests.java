package com.temah.lam;

import com.temah.lam.scheduling.CronTaskRegistrar;
import com.temah.lam.scheduling.SchedulingRunnable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TemAhLamApplicationTests {

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Test
    void contextLoads() {
        SchedulingRunnable scheduledTask = new SchedulingRunnable(
                "localPlatformStateTask", "getUsedMemory",
                "20");
        cronTaskRegistrar.addCronTask(scheduledTask, "0/10 * * * * ?");
        try {
            Thread.sleep(60000L);
        } catch (Exception ignored) {

        }
    }

}
