package com.cyb.codetest.多线程.并发容器;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * FIFO线程安全队列，通过CAS来插入元素，删除元素
 *
 * @author cyb
 * @date 2021/1/12 2:12 下午
 */
@Slf4j
public class ConcurrentLinkedQueueTest {

    public static void main(String[] args) {
        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        concurrentLinkedQueue.add(1);
        concurrentLinkedQueue.add(2);
        concurrentLinkedQueue.add(3);
        System.out.println(concurrentLinkedQueue);
        concurrentLinkedQueue.remove(1);
        System.out.println(concurrentLinkedQueue);
    }
}
