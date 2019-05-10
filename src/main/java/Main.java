import debounce.BusinessConsumer;
import debounce.DebounceConsumer;
import debounce.DebounceUtil;

import java.util.concurrent.TimeUnit;


/**
 * 去抖函数
 *
 * @author recall
 * @date 2019/5/10
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        DebounceConsumer<Object> debounce = DebounceUtil.debounce(new BusinessConsumer<Object>() {
                                                                      @Override
                                                                      public void accept(Object o) {
                                                                          System.out.println(System.currentTimeMillis());
                                                                      }
                                                                  },
                1, TimeUnit.SECONDS,
                2, TimeUnit.SECONDS
        );

        Runnable testRunnable = () -> {
            long count = 0;
            while (true) {
                debounce.accept(null);
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("*");
            }
        };

        new Thread(testRunnable).start();
        new Thread(testRunnable).start();
        new Thread(testRunnable).start();
        new Thread(testRunnable).start();
    }

}
