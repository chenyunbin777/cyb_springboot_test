package com.cyb.codetest.多线程.FutureTask;

import java.util.concurrent.*;

/**
 * 异步执行返回结果
 *
 * @author cyb
 * @date 2021/1/18 6:13 下午
 */
public class FutureTaskMy implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "执行任务";
    }

    public static void main(String[] args) throws InterruptedException {
        FutureTaskMy futureTaskMy = new FutureTaskMy();

        FutureTask<String> FutureTask = new FutureTask(futureTaskMy);
        Thread thread = new Thread(FutureTask);
        thread.start();
        //控制执行时间在1s
        String result = null;
        try {
            result = FutureTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}


