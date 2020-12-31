package com.cyb.codetest.JVM;

/**
 * 双检锁单例
 * 指令重排序：是指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各相应电路单元处理。最终会保证的出正确的结果
 * @author cyb
 * @date 2020/12/30 5:09 下午
 */
public class DoubleCheckLock {

    private volatile static DoubleCheckLock instance;

    public static DoubleCheckLock getInstance() {
        if(instance == null){
            synchronized (DoubleCheckLock.class){
                if(instance == null){
                    // 内部过程
                    //1 给实例分配内存
                    //2 初始化Singeton()构造器
                    //3 将instance对象指向分配的内存（instance非null了）
                    // volatile的作用就是让着按照1 2 3 的顺序来执行（通过内存屏障来实现），如果没有volatile修饰，就有可能让3不在最后执行，
                    // 那么 另外的线程也执行同样的代码在判断if(instance == null)时instance不为null了，return instance会报错的。
                    instance = new DoubleCheckLock();
                }
            }
        }


        return instance;
    }
}
