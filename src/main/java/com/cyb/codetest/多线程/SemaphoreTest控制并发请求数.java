package com.cyb.codetest.多线程;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 控制并发请求数
 *
 * @author cyb
 * @date 2021/1/18 2:31 下午
 */
public class SemaphoreTest控制并发请求数 {

    public static void main(String[] args) {
        //并发为2个
        Semaphore semaphore = new Semaphore(2);
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "获取权限");
                    Thread.sleep(random.nextInt(2000)); // 仿造处理时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(); //注意一定要finally中进行释放资源
                    System.out.println(Thread.currentThread().getName() + "释放");
                }
            }).start();
        }
    }
}
