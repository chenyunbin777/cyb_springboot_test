package com.cyb.codetest.集合;

import com.alibaba.fastjson.JSON;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author cyb
 * @date 2021/1/3 7:50 下午
 */
public class SkipListTest {

    public static void main(String[] args) {
        ConcurrentSkipListMap<String,Integer> skipListMap = new ConcurrentSkipListMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return 0;
            }
        });
        skipListMap.put("a",1);
        skipListMap.put("c",3);
        skipListMap.put("b",2);
        skipListMap.put("d",4);
        System.out.println("skipListMap:"+ JSON.toJSONString(skipListMap));

        Set<Integer> mset= new ConcurrentSkipListSet<>();

        mset.add(1);
        mset.add(21);
        mset.add(6);
        mset.add(2);
        //输出是有序的，从小到大。
        //skipListSet result=[1, 2, 6, 21]
        System.out.println("ConcurrentSkipListSet result="+mset);
        ConcurrentSkipListSet<String> myset = new ConcurrentSkipListSet<>();
        System.out.println(myset.add("abc"));
        System.out.println(myset.add("fgi"));
        System.out.println(myset.add("def"));
        System.out.println(myset.add("Abc"));
        /*
         * 输出是有序的:ConcurrentSkipListSet contains=[Abc, abc, def, fgi]
         */
        System.out.println("ConcurrentSkipListSet contains="+myset);


    }
}
