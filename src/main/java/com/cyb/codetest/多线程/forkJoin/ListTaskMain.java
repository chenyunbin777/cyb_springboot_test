package com.cyb.codetest.多线程.forkJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @auther cyb
 * @date 2024/1/30 16:08
 */
public class ListTaskMain {
    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }


        ListTask listTask = new ListTask(list);

        ForkJoinPool forkJoinPool = new ForkJoinPool(10);


        ForkJoinTask<Boolean> submitFuture = forkJoinPool.submit(listTask);


        if(submitFuture.isCompletedNormally()){
            System.out.println("打印执行的异常信息："+submitFuture.getException());
        }

        try {
            Boolean aBoolean = submitFuture.get();
            System.out.println("打印forkJoin执行结果："+aBoolean);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }



    }
}
