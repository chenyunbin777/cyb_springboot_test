package com.cyb.codetest.base;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2021/1/4 10:07 上午
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(3 * 0.1 == 0.3);


        ReentrantLock reentrantLock = new ReentrantLock();

        reentrantLock.lock();
        try {

        } catch (Exception e) {

        } finally {
            reentrantLock.unlock();
        }
//        interrupt();
        Map<Object, Object> synchronizedMap = Collections.synchronizedMap(new HashMap<>());

        HashMap hashMap = new HashMap();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

        TreeMap<Integer, Integer> treeMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        treeMap.put(1, 1);
        treeMap.put(3, 1);
        treeMap.put(2, 1);
        System.out.println(treeMap);

        List list = new ArrayList<>();
        f();
    }

    public static void f() {
        f();
    }

    //状态锁
    private static Object lock;

    public static void notifyTest() throws InterruptedException {

        synchronized (lock) {
            lock.wait();
        }

    }


    public static void interrupt() {

        Thread thread = new Thread(() -> {
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

    public void TryLock() {
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
