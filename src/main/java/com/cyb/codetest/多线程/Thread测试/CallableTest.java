package com.cyb.codetest.多线程.Thread测试;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author cyb
 * @date 2022/9/11 上午10:56
 */
public class CallableTest implements Callable {


    @Override
    public Object call() throws Exception {
        Thread.sleep(3000);
        System.out.println("calld方法执行了");
        return "call方法返回值";
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(new CallableTest());
//        futureTask.run();
        futureTask.run();
        Object obj = futureTask.get();
        System.out.println(obj);

        System.out.println("isDone:"+futureTask.isDone());


    }
}
