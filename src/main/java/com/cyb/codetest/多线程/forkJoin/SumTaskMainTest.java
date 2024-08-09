package com.cyb.codetest.多线程.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @auther cyb
 * @date 2024/1/26 15:53
 */
public class SumTaskMainTest {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //计算1～10000的累加和
        SumTask sumTask = new SumTask(1,10000);

        ForkJoinTask<Long> submitFuture = forkJoinPool.submit(sumTask);

        //Returns true if this task threw an exception or was cancelled.
        if(submitFuture.isCompletedAbnormally()){
            System.out.println("打印执行的异常信息："+submitFuture.getException());
        }

        try {
            Long aLong = submitFuture.get();
            System.out.println("打印forkJoin执行结果："+aLong);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
