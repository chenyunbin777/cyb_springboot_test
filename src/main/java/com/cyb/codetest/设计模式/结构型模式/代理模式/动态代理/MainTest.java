package com.cyb.codetest.设计模式.结构型模式.代理模式.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author cyb
 * @date 2021/1/6 3:02 下午
 */
public class MainTest {
    public static void main(String[] args) {
        Subject subject = new SubjectImpl();
        InvocationHandler subjectProxy = new SubjectProxy(subject);
        //代理类的类加载器 被代理的接口 代理类实例
        Subject proxyInstance = (Subject) Proxy.newProxyInstance(subjectProxy.getClass().getClassLoader(), subject.getClass().getInterfaces(), subjectProxy);
        proxyInstance.hello("world");
    }
}
