package com.cyb.codetest.练手;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cyb
 * @date 2022/11/1 下午5:14
 */
public class TestJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(()->{

            System.out.println("Runnable1");

        });



        Thread thread2 = new Thread(()->{
            try {
                //内部调用Object.wait()来实现
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Runnable2");

        });

        Thread thread3 = new Thread(()->{
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Runnable3");

        });


//        Executors.callable()

//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        executorService.execute(thread1);
//        executorService.execute(thread2);


        thread1.start();
        thread2.start();
        thread3.start();
    }
}
