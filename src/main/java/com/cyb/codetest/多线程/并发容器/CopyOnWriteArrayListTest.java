package com.cyb.codetest.多线程.并发容器;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 原理：并发版 ArrayList，底层结构也是数组，和 ArrayList 不同之处在于：当新增和删除元素时会创建一个新的数组，在新的数组中增加或者排除指定对象，
 * 最后用新增数组替换原来的数组。
 * - ReentrantLock lock = this.lock 添加删除操作 加锁保证了并发的安全
 * - 但是读取不会加锁，有可能读取到脏数据，适用于读多写少的情况
 *
 * @author cyb
 * @date 2021/1/12 2:04 下午
 */
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList();

        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.remove(1);
        copyOnWriteArrayList.get(1);
    }
}
