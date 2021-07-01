package com.cyb.codetest.多线程.栅栏;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cyb
 * @date 2021/1/14 9:37 下午
 */
public class CyclicBarrierTest {
    public static void main(String[] args) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {

            System.out.println("count 计数结束, 在屏障");
            System.out.println("汇总线程：" + Thread.currentThread().getName() + " 任务合并。");

        });

        System.out.println("cyclicBarrier getParties：" + cyclicBarrier.getParties());

        List<String> list = new ArrayList<>();
        // 实现线程A
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("线程A：" + Thread.currentThread().getName() + "执行任务。");
                System.out.println("线程A：到达屏障点");
                cyclicBarrier.await();
                System.out.println("线程A：退出屏障点");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 实现线程B
        Thread threadB = new Thread(() -> {
            try {
                System.out.println("线程B：" + Thread.currentThread().getName() + "执行任务。");
                System.out.println("线程B：到达屏障点");
                cyclicBarrier.await();
                System.out.println("线程B：退出屏障点");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(threadA);
        executorService.execute(threadB);

//        executorService.shutdown();

        //重置计数
//        cyclicBarrier.reset();
//
//        System.out.println("继续下一轮reset");
//        Thread.sleep(2000);
//
//
//        executorService.execute(()->{
//            System.out.println("C执行");
//            try {
//                cyclicBarrier.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (BrokenBarrierException e) {
//                e.printStackTrace();
//            }
//            System.out.println("C被唤醒");
//
//        });
//        executorService.execute(()->{
//            System.out.println("D执行");
//            try {
//                cyclicBarrier.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (BrokenBarrierException e) {
//                e.printStackTrace();
//            }
//            System.out.println("D被唤醒");
//
//        });
//
//        executorService.shutdown();

    }
}
