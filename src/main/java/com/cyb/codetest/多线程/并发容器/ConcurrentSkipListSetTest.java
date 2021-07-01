package com.cyb.codetest.多线程.并发容器;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author cyb
 * @date 2021/1/12 2:38 下午
 */
public class ConcurrentSkipListSetTest {

    public static void main(String[] args) {
        ConcurrentSkipListSet concurrentSkipListSet = new ConcurrentSkipListSet();

        concurrentSkipListSet.add(1);
    }
}
