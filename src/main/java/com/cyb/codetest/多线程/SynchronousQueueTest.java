package com.cyb.codetest.多线程;

import java.util.concurrent.SynchronousQueue;

/**
 * put线程执行queue.put(1) 后就被阻塞了，只有take线程进行了消费，put线程才可以返回。
 * 可以认为这是一种线程与线程间一对一传递消息的模型。
 *
 * @author cyb
 * @date 2021/1/4 5:42 下午
 */
public class SynchronousQueueTest {


    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();

        Thread putThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("put thread start");
                try {
                    queue.put(1);
                } catch (InterruptedException e) {
                }
                System.out.println("put thread end");
            }
        });

        Thread takeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("take thread start");
                try {
                    System.out.println("take from putThread: " + queue.take());
                } catch (InterruptedException e) {
                }
                System.out.println("take thread end");
            }
        });

        putThread.start();
        Thread.sleep(1000);
        takeThread.start();
    }
}
