package com.cyb.codetest.Thread;

import java.util.concurrent.*;

/**
 * @author cyb
 * @date 2021/1/4 5:08 下午
 */
public class ThreadPoolTest {


    public static void main(String[] args) {
        //1 创建一个可重用固定个数的线程池，以共享的无界队列方式来运行这些线程。
        //keepALiveTime = 0
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2,
                60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        //核心线程可以根据keepAliveTime来超时回收
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        executorService.execute(() -> {
            System.out.println("cyb");
        });


        // 2 创建一个可缓存线程池
        //可缓存线程池，先查看池中有没有以前建立的线程，如果有，就直接使用。如果没有，就建一个新的线程加入池中，缓存型池子通常用于执行一些生存期很短的异步型任务
        //缓存 keepALiveTime = 60s，corePoolSize = 0，maximumPoolSize = Integer.MAX_VALUE
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        // 3 创建一个定长线程池，支持定时及周期性任务执行
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(7);

        // 4 创建一个单线程化的线程池
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        //延迟1秒后每3秒执行一次
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("延迟1秒后每3秒执行一次");
            }
        }, 1, 3, TimeUnit.SECONDS);

        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                System.out.println("延迟1秒后每3秒执行一次");
            }
        }, 1, 3, TimeUnit.SECONDS);

    }
}
