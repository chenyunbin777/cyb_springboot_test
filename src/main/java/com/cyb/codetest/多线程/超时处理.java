package com.cyb.codetest.多线程;

import java.util.concurrent.*;

/**
 * @author cyb
 * @date 2021/1/18 2:38 下午
 */
public class 超时处理 {

    public static void main(String[] args) {
        int status = handleTimeout();
        System.out.println("status:" + status);
    }

    public static int handleTimeout() {

        ThreadPoolExecutor exec = new ThreadPoolExecutor(2, 2,
                60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        try {
            Future<String> future = exec.submit(() -> {
                //开始执行耗时操作
                Thread.sleep(1000 * 5);
                return "线程执行完成.";
            });
            String obj = future.get(1000 * 1, TimeUnit.MILLISECONDS); //任务处理超时时间设为 1 秒
            System.out.println("任务成功返回:" + obj);
        } catch (TimeoutException ex) {
            System.out.println("处理超时啦....");
            return 500;
        } catch (Exception e) {
            System.out.println("处理失败.");
            return 600;
        }
        // 关闭线程池
        exec.shutdown();

        return 200;
    }
}
