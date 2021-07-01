package com.cyb.codetest.多线程;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cyb
 * @date 2021/1/18 2:05 下午
 */
public class CASTest {

    public static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();

        int expect = atomicInteger.get();

        boolean result = atomicInteger.compareAndSet(expect, expect + 1);
        System.out.println(result);

        //可以解决ABA的问题
        //数据引用和版本号, 来个数据都对上才可以修改. 每个数据对应一个版本号
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(1, 1);


        boolean b = atomicStampedReference.compareAndSet(1, 2, 1, 2);
        System.out.println("b:" + b);
    }
}
