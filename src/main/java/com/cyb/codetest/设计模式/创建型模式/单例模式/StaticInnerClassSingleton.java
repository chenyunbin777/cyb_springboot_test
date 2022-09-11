package com.cyb.codetest.设计模式.创建型模式.单例模式;

/**
 * 静态内部类
 * 类加载机制保证了懒加载,只有通过显式调用 getInstance 方法时，才会显式装载 SingletonHolder 类，从而实例化 instance
 * <p>
 * 是否 Lazy 初始化：是
 * 是否多线程安全：是
 *
 * @author cyb
 * @date 2021/1/2 12:17 上午
 */
public class StaticInnerClassSingleton {

    private static class Inner {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }

    public static final StaticInnerClassSingleton getInstance() {
        return Inner.INSTANCE;
    }

}
