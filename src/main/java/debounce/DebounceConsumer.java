package debounce;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 去抖函数结果
 *
 * @param <T>
 */
public class DebounceConsumer<T> implements Consumer<T> {

    /**
     * 定时任务线程池
     */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 去抖时间
     * 毫秒
     */
    private long delayTime;

    /**
     * 最大去抖时间
     * 超过此时间则立即执行一次业务
     * 毫秒
     */
    private long maxTime;

    /**
     * 去抖开始时间
     * = null 时 则代表此次去抖已结束或者未开始
     */
    private volatile Long startTime;

    /**
     * 当前业务任务
     */
    private BusinessRunnable<T> currentRunnable = null;

    /**
     * 前一个业务任务
     */
    private BusinessRunnable<T> prevRunnable = null;

    /**
     * 业务函数
     */
    private BusinessConsumer<T> businessConsumer;

    /**
     * 锁
     */
    private Lock lock = new ReentrantLock();


    public DebounceConsumer(ScheduledExecutorService scheduledExecutorService, BusinessConsumer<T> businessConsumer, long delayTime, TimeUnit delayUnit, long maxTime, TimeUnit maxTimeUnit) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.businessConsumer = businessConsumer;
        this.delayTime = TimeUnit.MILLISECONDS.convert(delayTime, delayUnit);
        this.maxTime = TimeUnit.MILLISECONDS.convert(maxTime, maxTimeUnit);
    }

    @Override
    public void accept(T t) {
        try {
            lock.lock();
            // 如果前一个任务还未执行 则取消任务
            if (prevRunnable != null) {
                prevRunnable.setIgnore(true);
            }
            // 创建一个Runnable
            currentRunnable = new BusinessRunnable<T>(this, t) {
                @Override
                void handler(T params) {
                    businessConsumer.accept(params);
                }
            };
            // 把当前任务添加进定时任务
            scheduledExecutorService.schedule(currentRunnable, delayTime, TimeUnit.MILLISECONDS);
            // 当前任务变为前一个任务
            prevRunnable = currentRunnable;
        } finally {
            lock.unlock();
        }
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public BusinessRunnable<T> getCurrentRunnable() {
        return currentRunnable;
    }

    public void setCurrentRunnable(BusinessRunnable<T> currentRunnable) {
        this.currentRunnable = currentRunnable;
    }

    public BusinessRunnable<T> getPrevRunnable() {
        return prevRunnable;
    }

    public void setPrevRunnable(BusinessRunnable<T> prevRunnable) {
        this.prevRunnable = prevRunnable;
    }

    public BusinessConsumer<T> getBusinessConsumer() {
        return businessConsumer;
    }

    public void setBusinessConsumer(BusinessConsumer<T> businessConsumer) {
        this.businessConsumer = businessConsumer;
    }
}
