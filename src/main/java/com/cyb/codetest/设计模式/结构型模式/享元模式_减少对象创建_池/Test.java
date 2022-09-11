package com.cyb.codetest.设计模式.结构型模式.享元模式_减少对象创建_池;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cyb
 * @date 2022/9/10 下午2:52
 */
public class Test {
    private static final Map<Integer,Integer> map  = new HashMap();
    public static void main(String[] args) {

        map.put(1,1);

        map.put(1,2);
        map.remove(1);
        System.out.println(map);

        final int a = 0;
    }
}
