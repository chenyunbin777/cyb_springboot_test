package com.cyb.codetest.JVM;

/**
 * 代码就是对volatile类型的变量启动了20个线程，每个线程对变量执行1w次加1操作，如果volatile变量并发操作没有问题的话，那么结果应该是输出20w，
 * 但是结果运行的时候每次都是小于20w，
 *
 * 1 这就是因为race++操作不是原子性的，是由4条字节码指令构成的，是分多个步骤完成的。
 * 2 假设两个线程a、b同时取到了主内存的值，是0，这是没有问题的，在进行++操作的时候假设线程a执行到一半，线程b执行完了，这时线程b立即同步给了主内存，主内存的值为1，
 * 而线程a此时也执行完了，同步给了主内存，此时的值仍然是1，线程b的结果被覆盖掉了
 *
 * 作者：_fan凡
 * 链接：https://www.jianshu.com/p/15106e9c4bf3
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * @author cyb
 * @date 2020/12/30 4:22 下午
 */
public class VolatileTest {
    public static volatile int race = 0;

    public static void increase() {
        race++;
    }

    private static final int THREADS_COUNT = 20;


    public static void main(String[] args) throws InterruptedException {
        System.out.println(race);
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    race++;//这个操作不是原子操作的，是由4条字节码指令构成的
                }
            }
            );
            threads[i].start();
        }

        //等待所有累加线程结束
        while (Thread.activeCount() > 1) {
            Thread.yield();//当前线程让出CPU时间片
            System.out.println(Thread.activeCount());
        }
//        Thread.sleep(10000);
        System.out.println(race);
    }
}
