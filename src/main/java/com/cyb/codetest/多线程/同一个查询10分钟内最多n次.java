package com.cyb.codetest.多线程;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;

/**
 * @author cyb
 * @date 2021/1/18 2:48 下午
 */
public class 同一个查询10分钟内最多n次 {

    static HashMap<Integer, Count> map = new HashMap();

    static int n = 10;

    public static void main(String[] args) {


        for (int i = 0; i < 11; i++) {
            requestCount(111);
        }

    }

    public static void requestCount(Integer requestId) {
        Count count = null;
        if (map.containsKey(requestId)) {
            count = map.get(requestId);
            count.setCount(count.getCount() + 1);
        } else {
            count = new Count(1, new Date());
            map.put(requestId, count);
        }

        System.out.println("count.getCount():" + count.getCount());
        //同一个查询id10分钟内最多n次
        long x = System.currentTimeMillis() - count.getDate().getTime();
        if (x < 10 * 60 * 1000 && count.getCount() < n) {
            //执行业务逻辑
            System.out.println("执行业务逻辑");
            return;
        }
        //其他请求直接略过。。。
        System.out.println("其他请求直接略过。。。");
    }
}

@Data
class Count {
    int count;
    java.util.Date Date;

    public Count(int count, java.util.Date date) {
        this.count = count;
        Date = date;
    }
}
