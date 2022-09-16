package com.cyb.codetest.泛型.类泛型;

import java.util.List;

/**
 * 类泛型可以定义多个参数
 * @author cyb
 * @date 2022/9/12 下午1:24
 */
public class Box2<T,T2> {

    private T t;
    private T2 t2;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }


    public void add2(T t) {
        this.t = t;
    }

    public T get2() {
        return t;
    }

    public static void main(String[] args) {
        Box2<Integer,Integer> integerBox = new Box2<>();
        Box2<String,String> stringBox = new Box2<>();

        integerBox.add(new Integer(10));
        stringBox.add(new String("菜鸟教程"));

        System.out.printf("整型值为 :%d\n\n", integerBox.get());
        System.out.printf("字符串为 :%s\n", stringBox.get());


        integerBox.add2(new Integer(20));
        stringBox.add2(new String("菜鸟教程2"));

        System.out.printf("整型值为 :%d\n\n", integerBox.get2());
        System.out.printf("字符串为 :%s\n", stringBox.get2());

    }
}
