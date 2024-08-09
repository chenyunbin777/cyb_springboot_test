package com.cyb.codetest.多线程.forkJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * 使用ForkJoinPool自定义线程数量
 *
 * @auther cyb
 * @date 2024/1/25 16:04
 */
public class ForkJoinTest2 {

    public static void main(String[] args) throws InterruptedException {

        int cpuCores = Runtime.getRuntime().availableProcessors();
        System.out.println(cpuCores);
        ForkJoinPool forkJoinPool = new ForkJoinPool(1);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        forkJoinPool.submit(() -> {
            list.parallelStream().forEach((number) -> {
                try {
                    String currentThreadName = Thread.currentThread().getName();
                    Thread c = Thread.currentThread();

//                    Thread.sleep(5000);
                    //todo 以下程序可能会同一时间执行，因为，如此下来并发可能会非常高

                    System.out.println(currentThreadName + "===> "
                            + c.getClass().getName() + ":" + c.getName() + ":" + c.getId());

                } catch (Exception e) { }
            });
        });

        Thread.sleep(Integer.MAX_VALUE);
    }
}
