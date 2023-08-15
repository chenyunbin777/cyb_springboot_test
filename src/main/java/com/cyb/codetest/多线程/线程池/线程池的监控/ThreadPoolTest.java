package com.cyb.codetest.多线程.线程池.线程池的监控;

import com.cyb.codetest.多线程.线程池.RejectExceptionHandlerTest;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author cyb
 * @date 2022/9/22 下午5:27
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new RejectExceptionHandlerTest(),"cyb");


        for (;;) {
            Future<String> success = threadPoolMonitor.submit(() -> {

                System.out.println("执行线程池监控开始");
                System.out.println("开始执行程序逻辑");

                return "执行线程池监控success";

            });

            threadPoolMonitor.execute((()->{
                System.out.println("execute execute");


            }));

            Thread.sleep(50);
            String successreult = success.get();
            System.out.println("successreult:"+successreult);

            //将任务队列中正在等待的所有任务转移到一个 List 中并返回，我们可以根据返回的任务 List 来进行一些补救的操作，例如记录在案并在后期重试。
            List<Runnable> runnables = threadPoolMonitor.shutdownNow();

            //这个方法可以检测线程池是否真正“终结”了，这不仅代表线程池已关闭，同时代表线程池中的所有任务都已经都执行完毕了。
            boolean terminated = threadPoolMonitor.isTerminated();
            System.out.println("terminated:"+terminated);

            //仅仅表示线程池是否关闭
            boolean shutdown = threadPoolMonitor.isShutdown();
            System.out.println("shutdown:"+shutdown);


        }


    }
}
