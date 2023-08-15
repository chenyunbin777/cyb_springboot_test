package com.cyb.codetest.多线程.阻塞队列四种;

import com.alibaba.fastjson.JSON;

import java.util.Random;
import java.util.concurrent.*;

/**
 *
 * 阻塞的有界队列
 * @author cyb
 * @date 2021/1/12 2:58 下午
 */
public class ArrayBlockingQueueTest {


    static Random random = new Random(47);


    public static void main(String args[]) throws InterruptedException {

        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(16);

        PriorityBlockingQueue<PriorityEntity> queue = new PriorityBlockingQueue<PriorityEntity>();
        ExecutorService executor = Executors.newCachedThreadPool();


        //如果已经满了会抛出异常
        arrayBlockingQueue.add(1);
        //可以在不超过队列容量的情况下立即执行此操作，
        //如果此队列成功，则返回{@code true}；如果此队列失败，则返回}@code false}
        //这个方法比add会更好
        arrayBlockingQueue.offer(1);

        //当队列满了的时候会等待队列不满的时候插入到队尾。
        arrayBlockingQueue.put(1);


        for (int i = 0; i < 10; i++) {
            queue.put(new PriorityEntity(random.nextInt(10), i));
        }

        for (int i = 0; i < 10; i++) {
            //如果队列是null 会抛出异常
            PriorityEntity remove = queue.remove();
            //如果队列是null 返回null
            arrayBlockingQueue.poll();

            //获取队列头部，如果是空，等待队列中可以获取到数据
            arrayBlockingQueue.take();

            System.out.println("remove:" + remove.priority);
        }
        System.out.println("queue:" + JSON.toJSONString(queue));


//        executor.execute(new Runnable() {
//            public void run() {
//                int i = 0;
//                while (true) {
//                    PriorityEntity priorityEntity = new PriorityEntity(random.nextInt(10),i++);
//                    queue.put(priorityEntity);
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
//                    } catch (InterruptedException e) {
//                        System.out.println(e);
//                    }
//                }
//            }
//        });
//
//
//        executor.execute(new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        System.out.println("take-- " + queue.take() + " left:-- [" + queue.toString() + "]");
//                        try {
//                            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
//                        } catch (InterruptedException e) {
//                            System.out.println(e);
//                        }
//                    } catch (InterruptedException e) {
//                        System.out.println(e);
//                    }
//                }
//            }
//        });


//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            System.out.println(e);
//        }

    }

    static class PriorityEntity implements Comparable<PriorityEntity> {
        private static int count = 0;
        private int id = count++;
        private int priority;
        private int index = 0;


        public PriorityEntity(int _priority, int _index) {
            System.out.println("_priority : " + _priority);
            this.priority = _priority;
            this.index = _index;
        }


        public String toString() {
            return id + "# [index=" + index + " priority=" + priority + "]";
        }


        //数字小,优先级高
        public int compareTo(PriorityEntity o) {
            return this.priority > o.priority ? 1 : this.priority < o.priority ? -1 : 0;
        }
    }
}