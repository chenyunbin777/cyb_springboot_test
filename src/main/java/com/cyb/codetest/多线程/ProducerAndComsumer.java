package com.cyb.codetest.多线程;

import java.util.LinkedList;

/**
 * @author cyb
 * @date 2021/1/4 3:02 下午
 */
public class ProducerAndComsumer {

    //仓库最大容量
    private static final int MAX_SIZE = 100;
    //仓库存储的载体
    private static LinkedList list = new LinkedList();

    //生产产品
    public static void produce(int num) {
        //同步
        synchronized (list) {
            //仓库剩余的容量不足以存放即将要生产的数量，暂停生产
            while (list.size() + num > MAX_SIZE) {
                System.out.println("【要生产的产品数量】:" + num + "\t【库存量】:"
                        + list.size() + "\t暂时不能执行生产任务!");

                try {
                    //条件不满足，生产阻塞
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < num; i++) {
                list.add(new Object());
            }

            System.out.println("【已经生产产品数】:" + num + "\t【现仓储量为】:" + list.size());

            list.notifyAll();
        }
    }

    //消费产品
    public static void consume(int num) {
        synchronized (list) {

            //不满足消费条件
            while (num > list.size()) {
                System.out.println("【要消费的产品数量】:" + num + "\t【库存量】:"
                        + list.size() + "\t暂时不能执行生产任务!");

                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //消费条件满足，开始消费
            for (int i = 0; i < num; i++) {
                list.remove();
            }

            System.out.println("【已经消费产品数】:" + num + "\t【现仓储量为】:" + list.size());

            list.notifyAll();
        }
    }


    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; ; i++) {
                produce(10);
            }

        });
        thread.start();
        Thread thread2 = new Thread(() -> {
            for (int i = 0; ; i++) {
                consume(10);
            }

        });
        thread2.start();
        ;
    }
}
