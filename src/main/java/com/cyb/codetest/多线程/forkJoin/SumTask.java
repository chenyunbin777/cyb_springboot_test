package com.cyb.codetest.多线程.forkJoin;

import java.util.concurrent.RecursiveTask;

/**
 * @auther cyb
 * @date 2024/1/26 14:59
 */
public class SumTask  extends RecursiveTask<Long> {
    private final int begin;
    private final int end;
    public SumTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }
    @Override
    protected Long compute() {
        long sum = 0;
        if (end - begin < 100) {
            for (int i = begin; i <= end; i++) {
                sum += i;
            }
        } else {
            // 拆分逻辑
            int middle = (end + begin) / 2;
            SumTask subtask1 = new SumTask(begin, middle);
            SumTask subtask2 = new SumTask(middle + 1, end);
            subtask1.fork();
            subtask2.fork();
            // 等到⼦任务做完
            long sum1 = subtask1.join();
            long sum2 = subtask2.join();
            sum = sum1 + sum2;
        }
        return sum;
    }
}
