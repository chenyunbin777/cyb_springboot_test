package com.cyb.codetest.多线程.并发容器;

import com.alibaba.fastjson.JSON;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author cyb
 * @date 2021/1/12 2:13 下午
 */
public class CopyOnWriteArraySetTest {
    public static void main(String[] args) {
        CopyOnWriteArraySet copyOnWriteArraySet = new CopyOnWriteArraySet();
        copyOnWriteArraySet.add(1);

        System.out.println(JSON.toJSONString(copyOnWriteArraySet));
    }
}
