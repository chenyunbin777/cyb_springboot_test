package com.cyb.codetest.设计模式.单例模式;

/**
 * 是否 Lazy 初始化：是
 * 是否多线程安全：否 ,可以在 getInstance上加synchronized保证安全
 * 实现难度：易
 * @author cyb
 * @date 2021/1/1 11:54 下午
 */
public class LazyerSingleton {
    private LazyerSingleton instance = null;

    public LazyerSingleton getInstance() {
        if(instance == null){
            instance = new LazyerSingleton();
        }
        return instance;
    }
}
