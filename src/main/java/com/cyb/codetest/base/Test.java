package com.cyb.codetest.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2021/1/4 10:07 上午
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(3 * 0.1 == 0.3);

        interrupt();
        Map<Object, Object> synchronizedMap = Collections.synchronizedMap(new HashMap<>());


        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();


        f();
    }
    public static void f(){
        f();
    }
    //状态锁
    private static Object lock;
    public static void notifyTest() throws InterruptedException {

        synchronized(lock){
            lock.wait();
        }

    }


    public static void interrupt(){

        Thread thread = new Thread(()->{
            System.out.println("run thread");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        thread.interrupt();
    }

    public void TryLock(){
        ReentrantLock reentrantLock = new ReentrantLock();
        if (reentrantLock.tryLock()) {
            try {
                // manipulate protected state
            } finally {
                reentrantLock.unlock();
            }
        } else {
            // perform alternative actions
        }
    }


}
