package com.cyb.codetest.多线程;

/**
 * @author cyb
 * @date 2021/1/4 4:31 下午
 */
public class SynchronizedTest {

    public static void main(String[] args) {
        //锁是类对象
        synchronized (SynchronizedTest.class) {

        }
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        synchronizedTest.fBlock();
        synchronizedTest.f2();

    }

    public void fBlock() {
        //this就是调用这个方法的对象
        System.out.println(this);
        synchronized (this) {

        }
    }

    /**
     * 锁住类对象，
     */
    public static synchronized void StaticBlock() {
        //this就是调用这个方法的对象
        System.out.println("StaticBlock");
    }


    public synchronized void f2() {
        //this就是调用这个方法的对象
        System.out.println(this);
        synchronized (this) {

        }
    }


}
