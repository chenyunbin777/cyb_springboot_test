package com.cyb.codetest.多线程;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2021/1/18 2:34 下午
 */
public class ReentrantLoackTest {


    public static void main(String[] args) throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();

        try {
            if (reentrantLock.tryLock(3000, TimeUnit.MILLISECONDS)) {
                System.out.println("获取锁成功");
                reentrantLock.unlock();
            } else {

            }
        } catch (Exception e) {
            reentrantLock.unlock();
        }

    }

}
