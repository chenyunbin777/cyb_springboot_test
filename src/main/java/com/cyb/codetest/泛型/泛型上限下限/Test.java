package com.cyb.codetest.泛型.泛型上限下限;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cyb
 * @date 2022/9/12 下午1:37
 */
public class Test {


    /**
     * T extends Number 定义T参数的类型上限为Number，都必须是Number或者其子类
     * T2 super Number 定义T2参数的类型下限为Number，都必须是Number或者其父类
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Number> T get(T value) {
        System.out.println("value:" + value);
        return value;
    }

//    public static <T2 super Integer> T2 get2(T2 value) {
//        System.out.println("value:" + value);
//        return value;
//    }

    public static void main(String[] args) {
        get(1);
//        get2(2);


        // ? super Integer 定义通配符参数的类型下限为Integer，都必须是Integer或者其父类
        List<? super Integer> list1 = new ArrayList<>();
        List<? super Number> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        System.out.println("list1："+list1);


    }

}
