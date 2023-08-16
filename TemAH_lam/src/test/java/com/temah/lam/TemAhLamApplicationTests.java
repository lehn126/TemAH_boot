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
                "remotePlatformStateTask", "getUsedMemory",
                "carl##123456##192.168.2.129##22##10");
        cronTaskRegistrar.addCronTask(scheduledTask, "0/5 * * * * ?");
        try {
            Thread.sleep(60000L);
        } catch (Exception ignored) {

        }
    }

}
