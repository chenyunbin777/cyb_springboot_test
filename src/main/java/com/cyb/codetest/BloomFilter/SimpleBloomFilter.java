package com.cyb.codetest.BloomFilter;

import com.alibaba.fastjson.JSON;

import java.util.BitSet;
import java.util.HashMap;

/**
 * 简易的布隆过滤器
 *
 * @author cyb
 * @date 2020/12/17 4:04 下午
 */
public class SimpleBloomFilter {


    //布隆过滤器容量
    private static final int DEFAULT_SIZE = 2 << 24;

    //后面hash函数会用到，用来生成不同的hash值，可随意设置,
    //我觉得就是让value的hash值更分散的分布在bits上，这样使结果更准确
    private static final int[] seeds = new int[]{7, 11, 13, 31, 37, 61,};

    //bit数组，用来存放结果
    private BitSet bitSet = new BitSet(DEFAULT_SIZE);

    //
//    private SimpleHash[] func = new SimpleHash[seeds.length];

    public static void main(String[] args) {
//        HashMap
        String value = " stone2083@yahoo.cn ";
        SimpleBloomFilter filter = new SimpleBloomFilter();
        System.out.println(filter.contains(value));
        filter.add(value);
        System.out.println(filter.contains(value));
    }

//    public SimpleBloomFilter() {
//        for (int i = 0; i < seeds.length; i++) {
//            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
//        }
//
//        System.out.println("func："+JSON.toJSONString(func));
//    }

    /**
     * 计算出元素的hash值，并且设置结果为true
     *
     * @param value
     */
    public void add(String value) {
        for (int seed : seeds) {
            int hashValue = hash(value, seed);
            System.out.println("hashValue:" + hashValue);
            bitSet.set(hashValue, true);
        }
    }

    /**
     * 判断value是否存在于布隆过滤器中，
     * 存在的判断依据是：通过这几个hash函数计算的hash值所在的位置，在bitSet上都是true。也就是说这些hash值都被计算过
     *
     * @param value
     * @return
     */
    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
//        for (SimpleHash f : func) {
//            ret = ret && bits.get(f.hash(value));
//        }
        for (int seed : seeds) {
            ret = ret && bitSet.get(hash(value, seed));
        }

        return ret;
    }

    //hash函数，借鉴了hashmap的扰动算法
    private int hash(Object key, int i) {
        int h;
        return key == null ? 0 : (i * (DEFAULT_SIZE - 1) & ((h = key.hashCode()) ^ (h >>> 16)));
    }


//    public static class SimpleHash {
//
//        private int cap;
//        private int seed;
//
//        public SimpleHash(int cap, int seed) {
//            this.cap = cap;
//            this.seed = seed;
//        }
//
//        public int hash(String value) {
//            int result = 0;
//            int len = value.length();
//            for (int i = 0; i < len; i++) {
//                result = seed * result + value.charAt(i);
//            }
//            return (cap - 1) & result;
//        }
//
//    }
}
