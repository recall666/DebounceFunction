package debounce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务任务抽象类
 *
 * @param <T>
 */
public abstract class BusinessRunnable<T> implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 去抖函数
     */
    private final DebounceConsumer debounceConsumer;

    /**
     * 参数
     */
    private T params;

    /**
     * 是否已运行
     */
    private volatile boolean ran = false;

    /**
     * 是否忽略当前任务
     */
    private volatile boolean ignore = false;


    public BusinessRunnable(DebounceConsumer debounceConsumer, T params) {
        this.debounceConsumer = debounceConsumer;
        this.params = params;
    }

    @Override
    public void run() {
        synchronized (debounceConsumer) {
            long currentTimeMillis = System.currentTimeMillis();
            Long startTime = debounceConsumer.getStartTime();
            if (startTime == null) {
                startTime = currentTimeMillis;
                debounceConsumer.setStartTime(startTime);
            }

            // 如果当前任务需要忽略
            if (ignore) {
                long maxTime = debounceConsumer.getMaxTime();
                if (currentTimeMillis - startTime < maxTime) {
                    // 当去抖时间开始与当前时间的差 小于 最大限度时间 则忽略当前
                    // 反之 如果去抖时间与当前时间的差 大于最大限度时间 则执行业务逻辑
                    return;
                }
            }

            try {
                // 执行业务逻辑
                handler(params);
            } catch (Exception e) {
                logger.error("handler error", e);
            }

            // 去抖完毕 去抖时间置为空
            debounceConsumer.setStartTime(null);

            // 已运行
            setRan(true);
        }
    }

    /**
     * 业务方法
     *
     * @param params 参数
     */
    abstract void handler(T params);

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public DebounceConsumer getDebounceConsumer() {
        return debounceConsumer;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    public boolean isRan() {
        return ran;
    }

    public void setRan(boolean ran) {
        this.ran = ran;
    }
}