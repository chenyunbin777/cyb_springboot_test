package com.cyb.codetest.base;

/**
 * IntegerCache
 * static final Integer cache[];缓存了-128~127的Integer数组
 *
 * @author cyb
 * @date 2021/2/21 4:53 下午
 */
public class IntegerTest {
    public static void main(String[] args) {
        // -128 ~ 127
        Integer integer1 = new Integer(123);
        Integer integer2 = new Integer(123);
        System.out.println(integer1 == integer2);
        System.out.println(integer1.intValue() == integer2.intValue());
        System.out.println(integer1 == 123);
        System.out.println(integer2 == 123);

        Integer integer3 = new Integer(129);
        Integer integer4 = new Integer(129);
        System.out.println(integer3.intValue() == integer4.intValue());

        //每当自动装箱过程发生时(或者手动调用valueOf()时)，就会先判断数据是否在该区间，如果在则直接获取数组中对应的包装类对象的引用，如果不在该区间，则会通过new调用包装类的构造方法来创建对象。
        Integer integer5 = 111;
        Integer integer6 = 111;
        System.out.println(integer5 == integer6);
        System.out.println(integer5.intValue() == integer6.intValue());
        System.out.println(integer5 == 111);


        //Double类 与float都是创建新对象


    }
}
