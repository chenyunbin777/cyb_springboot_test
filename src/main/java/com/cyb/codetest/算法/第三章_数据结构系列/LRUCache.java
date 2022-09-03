package com.cyb.codetest.算法.第三章_数据结构系列;

import java.util.LinkedHashMap;

/**
 * @author cyb
 * @date 2022/8/29 下午8:56
 */
public class LRUCache {

    LinkedHashMap<Integer,Integer> cache = new LinkedHashMap();

    public int capacity;
    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public int get(int key) {

        if(!cache.containsKey(key)){
            return -1;
        }


        makeRecently(key,cache.get(key));
        return cache.get(key);

    }

    public void put(int key, int value) {

        //更新key的value,并且更新key，value到队尾
        if(cache.containsKey(key)){
            cache.put(key,value);
            makeRecently(key,value);
            return;
        }

        //如果当前cache容量满了，需要淘汰掉最久未使用的元素，也就是队头
        if(cache.size() >= capacity){
            Integer oldestKey = cache.keySet().iterator().next();
            cache.remove(oldestKey);
        }

        cache.put(key,value);
    }


    /**
     * 将
     * @param key
     * @param value
     */
    public void makeRecently(int key,int value){

        //删除原来的元素
        cache.remove(key);
        //再将该元素插入到队尾中
        cache.put(key,value);

    }


    public static void main(String[] args) {
        LinkedHashMap<Integer,Integer> cache = new LinkedHashMap();
        cache.put(1,1);
        cache.put(2,2);
        cache.put(3,3);
        cache.put(4,4);

        System.out.println(cache);


    }
}
