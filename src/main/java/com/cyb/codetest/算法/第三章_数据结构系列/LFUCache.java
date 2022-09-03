package com.cyb.codetest.算法.第三章_数据结构系列;

import com.okta.commons.lang.Collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * @author cyb
 * @date 2022/8/29 下午9:33
 */
public class LFUCache {

    HashMap<Integer, Integer> keyToVal = new HashMap<>();
    HashMap<Integer, Integer> keyToFreq = new HashMap<>();
    HashMap<Integer, LinkedHashSet> freqToKeys = new HashMap<>();

    public int capacity;
    //记录最小频次
    public int minFreq = 0;

    public LFUCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {
        if (!keyToVal.containsKey(key)) {
            return -1;
        }

        //增加key的使用频次数
        increaseFreq(key);

        return keyToVal.get(key);
    }


    public void put(int key, int value) {

        if (capacity <= 0) {
            return;
        }
        //1 判断是否存在key，如果存在更新value，频次
        if (keyToVal.containsKey(key)) {
            keyToVal.put(key, value);
            System.out.println();
            increaseFreq(key);
            return;
        }

        //2 key不存在判断是否到达阈值
        if (keyToVal.size() >= capacity) {
            //过期key，淘汰一个freq最小的
            removeMinFreq();
        }

        keyToVal.put(key, value);
        keyToFreq.put(key, 1);

        //1 新建 2 再插入key
        freqToKeys.putIfAbsent(1, new LinkedHashSet());
        freqToKeys.get(1).add(key);
        //插入key之后最小的freq一定是1
        minFreq = 1;

    }


    private void increaseFreq(int key) {

        int freq = keyToFreq.get(key);

        keyToFreq.put(key, freq + 1);

        //将对应的freq的可以移除掉
        freqToKeys.get(freq).remove(key);

        System.out.println("increaseFreq freqToKeys:" + freqToKeys);

        //如果存在freq+1频次则直接添加，不存在则创建一个
        freqToKeys.putIfAbsent(freq + 1, new LinkedHashSet());
        freqToKeys.get(freq + 1).add(key);


        //如果freq对应的set集合为空的话 就删除掉
        if (freqToKeys.get(freq).isEmpty()) {
            freqToKeys.remove(freq);

            //如果恰巧是最小频次，minFreq++
            if (freq == minFreq) {
                minFreq++;
            }
        }


    }


    /**
     * 删除最小的freq的key , freq相同删除最老的key
     */
    private void removeMinFreq() {
        LinkedHashSet<Integer> minFreqSet = freqToKeys.get(minFreq);

        System.out.println(minFreq);

        //最先插入的key
        Integer delKey = minFreqSet.iterator().next();
        minFreqSet.remove(delKey);

        //如果minFreq对应的集合再删除key之后为空了  就直接删除对应集合
        if (minFreqSet.isEmpty()) {
            freqToKeys.remove(minFreq);

        }

        keyToVal.remove(delKey);
        keyToFreq.remove(delKey);

    }


    public static void main(String[] args) {
        LFUCache lFUCache = new LFUCache(2);
        lFUCache.put(1, 1);

        sysOut(lFUCache);
        System.out.println("-----------");

        lFUCache.put(2, 2);

        sysOut(lFUCache);
        System.out.println("-----------");

        lFUCache.get(1);
        sysOut(lFUCache);
        System.out.println("-----------");

        lFUCache.put(3, 3);

        sysOut(lFUCache);
        System.out.println("-----------");


    }


    public static void sysOut(LFUCache lFUCache) {
        System.out.println("keyToVal:" + lFUCache.keyToVal);
        System.out.println("keyToFreq:" + lFUCache.keyToFreq);
        System.out.println("freqToKeys:" + lFUCache.freqToKeys);
    }
}
