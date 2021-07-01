package com.cyb.codetest.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2021/1/14 11:11 下午
 */
public class ReetrantLockConditionTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        List<String> list = new ArrayList<>();
        // 实现线程A
        Thread threadA = new Thread(() -> {
            while (true) {
                lock.lock();
                list.add("a");
                System.out.println("threadA list.size:" + list.size());
                if (list.size() == 10) {
                    break;
                }
                if (list.size() % 2 == 1) {
                    try {
                        System.out.println("唤醒线程B,阻塞线程A");
                        conditionB.signal();
                        conditionA.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                lock.unlock();
            }
        });
        // 实现线程B
        Thread threadB = new Thread(() -> {
            while (true) {
                lock.lock();
                list.add("b");
                System.out.println("threadB list.size:" + list.size());
                if (list.size() == 10) {
                    break;
                }
                if (list.size() % 2 == 0) {
                    try {
                        System.out.println("唤醒线程A， 阻塞线程B");
                        conditionA.signal();
                        conditionB.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                lock.unlock();
            }
        });
        threadB.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.start();
    }

}
