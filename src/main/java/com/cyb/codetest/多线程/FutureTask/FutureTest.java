package com.cyb.codetest.多线程.FutureTask;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 一步获取任务执行情况，每个任务只执行一次
 *
 * @author cyb
 * @date 2021/1/18 5:45 下午
 */
public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask(() -> {

            System.out.println("executes");
            return "success";
        });

        FutureTask<String> futureTask2 = new FutureTask(() -> {

            System.out.println("executes2");
            return "success2";
        });

        Thread thread = new Thread(futureTask);
        Thread thread2 = new Thread(futureTask);
        thread.start();
        Thread.sleep(1000);
        futureTask.cancel(true);
        thread2.start();
        Thread.sleep(1000);
        String futureTaskResult = futureTask.get();
        futureTask.isDone();
        System.out.println("futureTaskResult：" + futureTaskResult);

        String futureTaskResult2 = futureTask2.get();
        System.out.println("futureTaskResult2：" + futureTaskResult2);

//        if (!futureTask.isCancelled() && Objects.nonNull()) {
//            System.out.println(futureTask.get());
//        } else {
//            System.out.println("已经被取消了");
//        }
    }
}
