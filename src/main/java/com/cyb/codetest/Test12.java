package com.cyb.codetest;

import com.alibaba.fastjson.JSON;
import sun.jvm.hotspot.utilities.BitMap;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Random;

/**
 * @author cyb
 * @date 2021/3/3 9:44 上午
 */
public class Test12 {
    static final int DEFAULT_SIZE = 2 << 24;
    //后面hash函数会用到，用来生成不同的hash值，可随意设置,
    //我觉得就是让value的hash值更分散的分布在bits上，这样使结果更准确
    private static final int[] seeds = new int[]{7, 11, 13, 31, 37, 61,};

    public static void main(String[] args) {
//        BigDecimal bigDecimal = new BigDecimal(null);
//        BigDecimal bigDecimal2 = new BigDecimal(null);
//        BigDecimal add = bigDecimal.add(bigDecimal2);
//        System.out.println(bigDecimal.compareTo(bigDecimal2));

//        BitMap bitMap = new BitMap(100);
//        bitMap.atPut(1,true);
//        System.out.println(JSON.toJSONString(bitMap));
        //bit数组，用来存放结果
        BitSet bitSet = new BitSet(2 << 24);


        Integer key1 = new Integer(10);
        int h = 0;
        for (int seed : seeds) {
            int hashValue = hash(key1, seed);
            System.out.println("hashValue:" + hashValue);
            bitSet.set(hashValue, true);
        }

        System.out.println("bitSet：" + JSON.toJSONString(bitSet));


        for (int seed : seeds) {
            int hashValue = hash(key1, seed);
            boolean b = bitSet.get(hashValue);
            if (!b) {
                System.out.println("不存在与bitSet中");
            }
        }

    }

    //hash函数，借鉴了hashmap的扰动算法
    private static int hash(Object key, int i) {
        int h;
        return key == null ? 0 : (i * (DEFAULT_SIZE - 1) & ((h = key.hashCode()) ^ (h >>> 16)));
    }
}
