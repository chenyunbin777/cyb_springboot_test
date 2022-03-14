package com.cyb.codetest.优先级队列;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * 1 自然有序
 * 2 可以使用Comparator来构造自定义的有序
 * 3 非线程安全
 * @author cyb
 * @date 2022/3/11 下午4:23
 */
public class PriorityQueueTest {

    public static void main(String[] args) {
        //可以指定队列的大小,默认他是无界队列
//        PriorityQueue priorityQueue = new PriorityQueue(7);

        //优先队列自然排序示例
        Queue<Integer> integerPriorityQueue = new PriorityQueue<>(7);

        Random rand = new Random();

        for (int i = 0; i < 7; i++) {

            integerPriorityQueue.add(new Integer(rand.nextInt(100)));

        }

        for (int i = 0; i < 7; i++) {

            Integer in = integerPriorityQueue.poll();

            System.out.println("Processing Integer:" + in);

        }


        //优先队列使用示例
        Queue<Customer> customerPriorityQueue = new PriorityQueue<>(7, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                return (int) (c1.getId() - c2.getId());
            }
        });

        addDataToQueue(customerPriorityQueue);
        pollDataFromQueue(customerPriorityQueue);
    }


    //用于往队列增加数据的通用方法

    private static void addDataToQueue(Queue<Customer> customerPriorityQueue) {

        Random rand = new Random();

        for (int i = 0; i < 7; i++) {

            int id = rand.nextInt(100);

            customerPriorityQueue.add(new Customer(id, "Pankaj " + id));

        }

    }


    /**
     * 用于从队列取数据的通用方法
     */
    private static void pollDataFromQueue(Queue<Customer> customerPriorityQueue) {

        while (true) {

            Customer cust = customerPriorityQueue.poll();

            if (cust == null) break;

            System.out.println("Processing Customer with ID=" + cust.getId());

        }

    }
}
