package com.cyb.codetest.多线程.Thread测试;

/**
 * @author cyb
 * @date 2022/9/11 上午10:52
 */
public class JoinTest {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(()->{

            System.out.println("执行线程1 ");

        });

        Thread thread2 = new Thread(()->{

            System.out.println("执行线程2 ");

        });


        Thread thread3 = new Thread(()->{

            System.out.println("执行线程2 ");

        });

        thread1.start();
        thread1.join();
        thread2.start();


    }
}
