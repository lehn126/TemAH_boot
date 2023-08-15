package com.temah.lam.scheduling;

import java.util.concurrent.ScheduledFuture;

/**
 * ScheduledFuture的包装类
 * ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果。
 */
public class ScheduledTask {
    volatile ScheduledFuture<?> future;
    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
