package com.cyb.codetest.多线程.forkJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * 由于所有使用并行流parallerStream的地方都是使用同一个Fork-Join线程池(内部使用的fork-join线程池是整个JVM进程全局唯一的线程池)，而线程池线程数仅为cpu的核心数。
 * @auther cyb
 * @date 2024/1/25 16:04
 */
public class ForkJoinTest {

    public static void main(String[] args) throws InterruptedException {
        final List<Integer> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(1);
        }
        for (int i = 1; i <= 50; i++) {
            new Thread("test-" + i) {
                String currentThreadName = this.getName();
                @Override
                public void run() {
                    list.parallelStream()
                            .forEach(number -> {
                                Thread c = Thread.currentThread();
                                System.out.println(currentThreadName + "===> "
                                        + c.getClass().getName() + ":" + c.getName() + ":" + c.getId());
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }.start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
