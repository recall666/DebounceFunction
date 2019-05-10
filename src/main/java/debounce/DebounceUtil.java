package debounce;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 去抖函数工具类
 *
 * @author recall
 * @date 2019/5/9
 */
public class DebounceUtil {

    /**
     * 公用线程池
     */
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);

    /**
     * 去抖函数
     * 在去抖时间内 执行结果函数
     * 开始去抖时间与当前时间的差异小于最大去抖时间 则最终只会执行一次
     * <p>
     * 如果在去抖时间内 执行结果函数
     * 开始去抖时间与当前时间的差异大于等于去抖时间 则会立即执行一次 开始去抖时间则会重置
     *
     * @param business    业务
     * @param delayTime   去抖时间
     * @param delayUnit   去抖时间单位
     * @param maxTime     最大去抖时间（超过此事件会一定会执行一次业务）
     * @param maxTimeUnit 最大去抖时间单位
     * @param <T>         业务参数泛型
     * @return 去抖结果函数
     */
    public static <T> DebounceConsumer<T> debounce(BusinessConsumer<T> business, long delayTime, TimeUnit delayUnit, long maxTime, TimeUnit maxTimeUnit) {
        return new DebounceConsumer<>(scheduledExecutorService, business, delayTime, delayUnit, maxTime, maxTimeUnit);
    }



}
