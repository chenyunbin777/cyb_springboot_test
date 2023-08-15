package com.cyb.codetest;


import java.nio.MappedByteBuffer;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) throws InterruptedException {
//        ConcurrentHashMap
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);
        arrayBlockingQueue.add(1);
        System.out.println();

        List<String> arrayList = new ArrayList();
        System.out.println("arrayList.size():" + arrayList.size());


        HashMap hashmap = new HashMap();
        LinkedList linkedList = new LinkedList();
        Set set = new HashSet();

        String str = "123";
        String intern = str.intern();

        testString();
        AtomicInteger AtomicInteger = new AtomicInteger(10);
        AtomicInteger.compareAndSet(1, 2);
        AtomicInteger.get();
//        HashMap头插 尾插  静态条件  数据竞争 if else  i++
//        锁：监视器 持有锁线程  阻塞队列：等待锁线程 只有一个条件队列：
//        用户态 内核态 的切换
//        元数据指针 指向方法区

        //锁的优化 1.6 优化后的锁
        //锁消除
        //锁合并

        //偏向锁升级为轻量级锁会在：安全点执行STW，生产环境不要使用偏向锁
//        for (int i = 0; i < 10; i++) {
//            synchronized (Test.class){
//                System.out.println("ssss");
//            }
//        }

        //自旋锁：多线程竞争激烈场景，要不进入阻塞队列，要不自旋获取锁，不进行上下文切换
        //自适应自旋锁：

        //锁性能：synchronized：内置锁，监视器，非公平
        //使用方式，线程的：公平，中断()，死锁
        //AQS

        //JMM  java内存模型是什么？， 为什么会有指令重排序？ 指令重排序遵循什么规则？ happen-before规则
        //1 vailtile：内存语义是什么？
        //2 CAS:compare and sweep，
        //1 2 实现了我们的并发包
        //

        //ThreadLocal：动态地址法解决hash冲突问题

        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.unlock();
        reentrantLock.lockInterruptibly();
        reentrantLock.tryLock(1, TimeUnit.SECONDS);

        Thread thread = new Thread(() -> {
            System.out.println("xxxxx");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("yyyy");
        });

        thread.start();


        /**
         * 三个方法，都是Thread中定义的方法，其中interrupted()是Thread中的静态方法：
         * interrupt() : 给调用线程加入一个中断点，但是不会结束此线程。
         * isInterrupted()：返回此线程的中断状态，但是不会移除此线程中断状态。
         * interrupted()：返回的是当前线程的中断状态，会移除当前线程的中断状态
         */
        Thread.currentThread().interrupt();

        System.out.println("interrupted1:" + Thread.interrupted());
        System.out.println("interrupted2:" + Thread.interrupted());
        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());

        MappedByteBuffer mappedByteBuffer;

//        Selector serverSelector = Selector.open();
//        serverSelector.select();

        final int a = 3;
    }


    public static void testString() {
        String a = new String("ab");
        String b = new String("ab");
        String c = "ab";
        String d = "a" + "b";
        String e = "b";
        String f = "a" + e;

//        System.out.println(a==b); //a b地址引用不同 不相等
//        System.out.println(a.equals(b)); // a值=b值

//        System.out.println("aintern:"+a.intern());
//        System.out.println("bintern:"+b.intern());
        // 采用new 创建的字符串对象不进入字符串池
        // 调用b.intern()时，会判断常量池中是否有是否有ab，如果没有就添加到字符串池中，然后返回字符串的引用。
        // b.intern() 和 a的地址一定不一样，但是 b.intern() 和 a.intern()的地址是一样的
        System.out.println("b.intern() == a:" + (b.intern() == a));
        //当b调用intern的时候，会检查字符串池中是否含有该字符串。由于之前定义的c已经进入字符串池中，所以会得到相同的引用。
        System.out.println(b.intern() == c);
        System.out.println(b.intern() == d);
        System.out.println(b.intern() == f);
        System.out.println(b.intern() == a.intern());


        StringBuilder sb = new StringBuilder();
        StringBuffer sf = new StringBuffer();
        sf.append(1);
        sb.append(1);
        String string = new String();
        String.valueOf(1);
        string.getBytes();
        synchronized (new Object()) {

        }


    }

}
