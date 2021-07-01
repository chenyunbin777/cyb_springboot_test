package com.cyb.codetest.多线程.阻塞队列四种;

import com.alibaba.fastjson.JSON;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author cyb
 * @date 2021/1/12 3:03 下午
 */
public class PriorityBlockingQueueTest {

    public static void main(String[] args) {

        //可以排序
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue(16, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        priorityBlockingQueue.add(1);

        priorityBlockingQueue.add(10);

        priorityBlockingQueue.add(7);

        System.out.println(" priorityBlockingQueue:" + JSON.toJSONString(priorityBlockingQueue));
    }
}
