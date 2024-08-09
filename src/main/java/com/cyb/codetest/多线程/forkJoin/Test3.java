package com.cyb.codetest.多线程.forkJoin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @auther cyb
 * @date 2024/7/12 18:31
 */
public class Test3 {


    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(1);
        List<String> inertInventoryList = new ArrayList<>();
        try {
            Object finish = pool.submit(() -> {
                inertInventoryList.parallelStream().forEach(livePlanProductImportBO -> {
                    System.out.println("执行任务");

                });

                System.out.println("finish");

                return "a";

            }).get();

            System.out.println(finish);


        } catch (Exception e) {
            System.out.println("全量数据插入e :"+e);
        } finally {
            System.out.println("finally shutdown");
            pool.shutdown();
        }
    }
}
