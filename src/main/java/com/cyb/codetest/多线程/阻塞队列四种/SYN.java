package com.cyb.codetest.多线程.阻塞队列四种;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author cyb
 * @date 2021/1/12 3:25 下午
 */
public class SYN {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        SynchronousQueue<Object> synchronousQueue = new SynchronousQueue<>();

        Runnable producer = () -> {
            Object object = new Object();
            try {
                synchronousQueue.put(object);
                System.out.println("put：" + object);
            } catch (InterruptedException ex) {
//                log.error(ex.getMessage(),ex);
            }
//            log.info("produced {}",object);
        };

        Runnable consumer = () -> {
            try {
                Object object = synchronousQueue.take();
                System.out.println("take：" + object);
//                log.info("consumed {}",object);
            } catch (InterruptedException ex) {
//                log.error(ex.getMessage(),ex);
            }
        };

        executor.submit(producer);
        executor.submit(consumer);

        executor.awaitTermination(50000, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }
}
