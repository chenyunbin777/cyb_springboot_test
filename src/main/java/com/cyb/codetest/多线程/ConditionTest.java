package com.cyb.codetest.多线程;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2021/3/29 11:57 上午
 */
public class ConditionTest {
    private static volatile int newPrintWho = 1; //下一个输出

    private static ReentrantLock lock = new ReentrantLock();

    private static final Condition conditionA = lock.newCondition();
    private static final Condition conditionB = lock.newCondition();
    private static final Condition conditionC = lock.newCondition();
    private static final int TIMES = 5; //循环次数

    static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread threadA = new Thread() {
            @Override
            public void run() {

                while (true) {

                    lock.lock();
                    if (count == 60) {
                        return;
                    }
                    try {
                        for (int i = 0; i < TIMES; i++) {


                            while (newPrintWho != 1) {
                                conditionA.await(); //阻塞
                            }
                            System.out.println("线程A：" + count);
                            count++;

                        }
                        newPrintWho = 2;
                        conditionB.signalAll(); //唤醒ThreadB
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        lock.unlock();
                    }
                }


            }
        };

        Thread threadB = new Thread() {
            @Override
            public void run() {
                while (true) {

                    lock.lock();
                    if (count == 60) {
                        return;
                    }
                    try {
                        for (int i = 0; i < TIMES; i++) {


                            while (newPrintWho != 2) {
                                conditionB.await(); //阻塞
                            }
                            System.out.println("线程B：" + count);
                            count++;
                        }
                        newPrintWho = 3;
                        conditionC.signalAll(); //唤醒ThreadB
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        lock.unlock();
                    }
                }


            }

        };
        Thread threadC = new Thread() {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    if (count == 60) {
                        return;
                    }
                    try {
                        for (int i = 0; i < TIMES; i++) {


                            while (newPrintWho != 3) {
                                conditionC.await(); //阻塞
                            }
                            System.out.println("线程C：" + count);
                            count++;
                        }
                        newPrintWho = 1;
                        conditionA.signalAll(); //唤醒ThreadB
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        lock.unlock();
                    }
                }

            }
        };


        threadA.start();
        threadB.start();
        threadC.start();
    }

}
