package com.cyb.codetest.设计模式.单例模式;

/**
 * JDK 版本：JDK1.5 起
 * 是否 Lazy 初始化：是
 * 是否多线程安全：是
 * 实现难度：较复杂
 *
 * @author cyb
 * @date 2021/1/2 12:04 上午
 */
public class DoubleCheckLockSIngleton {

    /**
     * volatile的作用是保证了instance =  new DoubleCheckLockSIngleton(); 不会指令重排序
     * 保证了不会有多线程的问题，  instance被分配内存之后他就不是null了，当其他线程判断的时候会直接返回instance 导致报错。
     * <p>
     * 1 给实例分配内存
     * 2 初始化Singeton()构造器
     * 3 将instance对象指向分配的内存（instance非null了）
     * <p>
     * 1.分配对象空间；
     * 2.初始化对象；
     * 3.设置instance指向刚刚分配的地址。
     * 要保证 指令3 在最后执行
     */
    private static volatile DoubleCheckLockSIngleton instance = null;

    public static DoubleCheckLockSIngleton getInstance() {
        //第一个instance == null的作用是不加锁的判断使程序运行效率更高
        //当线程判断了instance 不是null就可以直接返回。
        if (instance == null) {
            synchronized (DoubleCheckLockSIngleton.class) {
                //第二个instance == null的作用是害怕在加锁之前，第一个instance == null之后有其他已经创建好了instance实例
                if (instance == null) {
                    instance = new DoubleCheckLockSIngleton();
                }
            }
        }

        return instance;
    }
}
