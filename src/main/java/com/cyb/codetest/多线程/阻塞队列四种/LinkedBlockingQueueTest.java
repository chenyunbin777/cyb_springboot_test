package com.cyb.codetest.多线程.阻塞队列四种;

import com.alibaba.fastjson.JSON;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * FIFO无界队列
 *
 * @author cyb
 * @date 2021/1/12 2:44 下午
 */
public class LinkedBlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        //如果不设置大小的话就是Integer.MAX_VALUE
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(3);

        //如果队列已经满了就抛出异常
//        linkedBlockingQueue.add(1);
//        linkedBlockingQueue.add(2);
        //队列已经满了就不进行插入
//        linkedBlockingQueue.offer(2);
        System.out.println("linkedBlockingQueue1:" + JSON.toJSONString(linkedBlockingQueue));
        //获取队列元素，出队元素，如果没有元素会阻塞等待
//        linkedBlockingQueue.take();

        //获取队列元素，出队元素，如果没有元素返回null
//        Object poll = linkedBlockingQueue.poll();
//        System.out.println("poll:"+poll);
        //返回队头，不出队元素
//        Object peek = linkedBlockingQueue.peek();
//        System.out.println("peek:"+peek);

        //空队列会报错
        linkedBlockingQueue.remove();
        System.out.println("linkedBlockingQueue2:" + JSON.toJSONString(linkedBlockingQueue));
    }
}
