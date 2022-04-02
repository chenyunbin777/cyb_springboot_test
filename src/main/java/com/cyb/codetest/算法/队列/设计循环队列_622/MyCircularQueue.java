package com.cyb.codetest.算法.队列.设计循环队列_622;

import com.alibaba.fastjson.JSON;

/**
 * 你的实现应该支持如下操作：
 * <p>
 * MyCircularQueue(k): 构造器，设置队列长度为 k 。
 * Front: 从队首获取元素。如果队列为空，返回 -1 。
 * Rear: 获取队尾元素。如果队列为空，返回 -1 。
 * enQueue(value): 向循环队列插入一个元素。如果成功插入则返回真。
 * deQueue(): 从循环队列中删除一个元素。如果成功删除则返回真。
 * isEmpty(): 检查循环队列是否为空。
 * isFull(): 检查循环队列是否已满。
 * <p>
 * <p>
 * <p>
 * 所有的值都在 0 至 1000 的范围内；
 * 操作数将在 1 至 1000 的范围内；
 * 请不要使用内置的队列库。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/design-circular-queue
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author cyb
 * @date 2022/4/2 下午3:44
 */
public class MyCircularQueue {

    int[] queue;
    int head = 0;
    int tail = 0; //位置是下一个为空也就是0的位置， 除非tail追上了head节点 测试就是满队列状态
    boolean isFull = false;

    public MyCircularQueue(int k) {
        queue = new int[k];
    }

    /**
     * 向循环队列插入一个元素。如果成功插入则返回真
     * 队尾入队
     * 1 是否已满
     * 如果满了返回false
     *
     * @param value
     * @return
     */
    public boolean enQueue(int value) {

        if (isFull()) {

            return false;
        }


        queue[tail] = value;

        //小于最后一个节点就可以++
        if (tail < queue.length - 1) {
            tail++;
        }
        //如果等于就直接到0
        else if (tail == queue.length - 1) {
            tail = 0;
        }

        if (tail == head) {
            isFull = true;
        }

        //头尾不相邻 tail向后移动一位
//        if (!isHeadNearTail()) {
//            if (tail < queue.length - 1) {
//                tail++;
//            }else if(tail==queue.length - 1){
//                tail = 0;
//            }
//
//        }
        //头尾相邻不移动

        return true;
    }

    /**
     * 从循环队列中删除一个元素。如果成功删除则返回真。
     * FIFO队头出队
     *
     * @return
     */
    public boolean deQueue() {
        if (isEmpty()) {
            return false;
        }

        queue[head] = 0;
        //小于最后一个节点就可以++
        if (head < queue.length - 1) {
            head++;
        }
        //如果等于就直接到0
        else if (head == queue.length - 1) {
            head = 0;
        }

        if (isFull) {
            isFull = false;
        }

        return true;
    }

    /**
     * 从队首获取元素。如果队列为空，返回 -1
     *
     * @return
     */
    public int Front() {
        if (isEmpty()) {
            return -1;
        }
        return queue[head];
    }

    /**
     * 获取队尾元素。如果队列为空，返回 -1
     *
     * @return
     */
    public int Rear() {

        //如果是空就直接返回-1，下边判断tail位置的时候就不用了
        if (isEmpty()) {
            return -1;
        }

        //如果在首位需要返回环形最后一位
        if (tail == 0) {
            return queue[queue.length - 1];
        }

        System.out.println(tail);
        return queue[tail - 1];

    }

    /**
     * 检查循环队列是否为空。
     *
     * @return
     */
    public boolean isEmpty() {

        //不是满队列并且头尾相等
        if (!isFull && head == tail) {
            return true;
        }

        return false;
    }

    /**
     * 队列满的条件就是
     * 1 tail与head相邻：tail在最后一个位置，head在第一个位置
     * 2 或者tail在head的前面：tail head同时大于0
     * 3 并且 queue[tail] 必须有数据
     *
     * @return
     */
    public boolean isFull() {

        return isFull;

//        if (((tail == queue.length - 1 && head == 0) || (head - tail == 1)) && queue[tail] != 0) {
//            return true;
//        }
//
//        return false;
    }


    /**
     * 判断头尾是否相邻
     *
     * @return
     */
    public boolean isHeadNearTail() {

        if ((tail == queue.length - 1 && head == 0)
                || (head - tail == 1)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        MyCircularQueue circularQueue = new MyCircularQueue(3); // 设置长度为 3
        boolean enQueue1 = circularQueue.enQueue(1); // 返回 true
        boolean enQueue2 = circularQueue.enQueue(2); // 返回 true
        System.out.println("head:" + circularQueue.head);
        System.out.println("tail:" + circularQueue.tail);

        boolean enQueue3 = circularQueue.enQueue(3);// 返回 true
        System.out.println("enQueue1:" + enQueue1);
        System.out.println("enQueue2:" + enQueue2);
        System.out.println("enQueue3:" + enQueue3);

        boolean enQueue4 = circularQueue.enQueue(4);// 返回 false，队列已满
        System.out.println("enQueue4:" + enQueue4);
        System.out.println("tail:" + circularQueue.tail);
        int rear3 = circularQueue.Rear();// 返回 3
        System.out.println("rear3:" + rear3);

        boolean full = circularQueue.isFull();// 返回 true
        System.out.println("full:" + full);

        boolean deQueue = circularQueue.deQueue();// 返回 true
        System.out.println("deQueue:" + deQueue);
        System.out.println("queue:" + JSON.toJSONString(circularQueue.queue));

        //这里有问题 [0,2,3] 入队4应该变成[4,2,3]
        System.out.println("head1:" + circularQueue.head);
        System.out.println("tail1:" + circularQueue.tail);
        boolean enQueue44 = circularQueue.enQueue(4);// 返回 true
        System.out.println("enQueue44:" + enQueue44);
        System.out.println("queue:" + JSON.toJSONString(circularQueue.queue));
        System.out.println("head:" + circularQueue.head);
        System.out.println("tail:" + circularQueue.tail);


        int rear4 = circularQueue.Rear();// 返回 4
        System.out.println("rear4:" + rear4);


    }

}
