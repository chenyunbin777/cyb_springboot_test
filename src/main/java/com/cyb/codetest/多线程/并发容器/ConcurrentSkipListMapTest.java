package com.cyb.codetest.多线程.并发容器;

import com.alibaba.fastjson.JSON;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author cyb
 * @date 2021/1/12 2:15 下午
 */
public class ConcurrentSkipListMapTest {

    public static void main(String[] args) {
        ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap();
        concurrentSkipListMap.put(1, 1);
        concurrentSkipListMap.put(3, 10);
        concurrentSkipListMap.put(2, 14);
        System.out.println(JSON.toJSONString(concurrentSkipListMap));
        concurrentSkipListMap.get(1);
    }
}
