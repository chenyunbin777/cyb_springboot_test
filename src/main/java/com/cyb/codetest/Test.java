package com.cyb.codetest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String[] args) {
        System.out.println();

        HashMap hashmap = new HashMap();
        LinkedList linkedList = new LinkedList();
        Set set = new HashSet();

        String str = "123";
        String intern = str.intern();

        testString();
        AtomicInteger AtomicInteger = new AtomicInteger(10);
    }



    public  static void testString(){
        String a = new String("ab");
        String b = new String("ab");
        String c = "ab";
        String d = "a" + "b";
        String e = "b";
        String f = "a" + e;

//        System.out.println(a==b); //a b地址引用不同 不相等
//        System.out.println(a.equals(b)); // a值=b值

//        System.out.println("aintern:"+a.intern());
//        System.out.println("bintern:"+b.intern());
        // 采用new 创建的字符串对象不进入字符串池
        // 调用b.intern()时，会判断常量池中是否有是否有ab，如果没有就添加到字符串池中，然后返回字符串的引用。
        // b.intern() 和 a的地址一定不一样，但是 b.intern() 和 a.intern()的地址是一样的
        System.out.println("b.intern() == a:"+(b.intern() == a));
        //当b调用intern的时候，会检查字符串池中是否含有该字符串。由于之前定义的c已经进入字符串池中，所以会得到相同的引用。
        System.out.println(b.intern() == c);
        System.out.println(b.intern() == d);
        System.out.println(b.intern() == f);
        System.out.println(b.intern() == a.intern());
    }

}
