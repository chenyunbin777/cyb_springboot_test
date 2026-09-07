package com.cyb.codetest.多线程.线程池;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * 当任务数超过核心线程数的时候，如果和让任务不进入队列中
 * @author cyb
 * @date 2024/9/18 上午11:11
 */
public class NoQueueThreadPoolTest {


    public static void main(String[] args) {
        String str ="111.111.22.11";
        String[] strings = str.split("\\.");

        System.out.println(JSON.toJSONString(strings));
        System.out.println(111);

        StringUtils.isNotEmpty(str);


        int length = str.length();

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2,
                60L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),///ˈsɪŋkrənəs kjuː/
                //SynchronousQueue是BlockingQueue的一种，所以SynchronousQueue是线程安全的。SynchronousQueue和其他的BlockingQueue不同的是SynchronousQueue的capacity是0。
                // 即SynchronousQueue不存储任何元素。
                //也就是说SynchronousQueue的每一次insert操作，必须等待其他线性的remove操作。而每一个remove操作也必须等待其他线程的insert操作。
                //这种特性可以让我们想起了Exchanger。和Exchanger不同的是，使用SynchronousQueue可以在两个线程中传递同一个对象。一个线程放对象，另外一个线程取对象。
                Executors.defaultThreadFactory(),
                new RejectExceptionHandlerTest()); //自定义线程拒绝策略\
    }
}
