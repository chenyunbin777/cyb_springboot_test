package com.cyb.codetest.多线程.线程池.线程池的监控;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author cyb
 * @date 2022/9/22 下午5:20
 */
public class ThreadPoolMonitor2 extends ThreadPoolExecutor {
    public ThreadPoolMonitor2(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 父类ThreadPoolExecutor并没有对beforeExecute进行实现，是一个空方法交给子类去进行扩展
     * 会在任务r执行之前执行
     *
     * 任务执行之前，记录任务开始时间
     *
     */
    @Override
    public void beforeExecute(Thread t, Runnable r) {


    }

    /**
     * 任务执行之后，计算任务结束时间
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {

    }
}
