package com.cyb.codetest.jdk8.接口中的静态方法;

/**
 * @author cyb
 * @date 2022/9/11 下午11:53
 */
public interface Jdk8InterfaceTest {

    /**
     * 不是抽象的方法，是静态的方法，用于接口的扩展
     */
    public static void f1(){

    }

    static void f2(){

    }


    /**
     * 可以声明默认方法
     * 之所以引进的默认方法。他们的目的是为了解决接口的修改与现有的实现不兼容的问题。
     * 这种也可以通过适配器的方式去添加功能
     */
    public default void print1(){
        System.out.println("我是一辆车!");
    }

    public default void print2(){
        System.out.println("我是一辆车!");
    }
}
