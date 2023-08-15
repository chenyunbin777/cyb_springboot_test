package com.cyb.codetest.练手;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2022/11/1 下午5:14
 */
public class TestCondition {
    private static volatile int newPrintWho = 1; //下一个输出
    private static ReentrantLock lock = new ReentrantLock();

    private static final Condition conditionA = lock.newCondition();
    private static final Condition conditionB = lock.newCondition();
    private static final Condition conditionC = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            while (true) {
                lock.lock();

                try {


                    //死循环等待
                    while (newPrintWho != 1) {

                        conditionA.await();

                    }

                    System.out.println("Runnable1");
                    newPrintWho = 2;
                    conditionB.signal();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }


        });


        Thread thread2 = new Thread(() -> {

            while (true) {
                lock.lock();
                try {
                    while (newPrintWho != 2) {

                        conditionB.await();

                    }

                    System.out.println("Runnable2");
                    newPrintWho = 3;
                    conditionC.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        });

        Thread thread3 = new Thread(() -> {

            while (true) {
                lock.lock();

                try {
                    while (newPrintWho != 3) {

                        conditionC.await();

                    }

                    System.out.println("Runnable3");

                    newPrintWho = 1;
                    conditionA.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });


        thread1.start();
        thread2.start();
        thread3.start();
    }
}
