package com.cyb.codetest.设计模式.代理模式.动态代理;

/**
 * @author cyb
 * @date 2021/1/6 3:01 下午
 */
public class SubjectImpl implements Subject {
    @Override
    public void hello(String param) {
        System.out.println("hello  " + param);
    }
}