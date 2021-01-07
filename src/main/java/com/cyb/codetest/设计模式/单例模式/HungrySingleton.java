package com.cyb.codetest.设计模式.单例模式;

/**
 * 饿汉式
 * 是否 Lazy 初始化：否
 *
 * 是否多线程安全：是
 *
 * 实现难度：易
 *
 * 描述：这种方式比较常用，但容易产生垃圾对象。
 * 优点：没有加锁，执行效率会提高。
 * 缺点：类加载时就初始化，浪费内存。
 *
 * instance 在类装载时就实例化
 *
 * @author cyb
 * @date 2021/1/1 11:53 下午
 */
public class HungrySingleton {

    private static HungrySingleton instance = new HungrySingleton();

    public static HungrySingleton getInstance(){
        return instance;
    }
}
