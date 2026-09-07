package com.cyb.codetest.JVM;

/**
 *
 * 堆大小初始、总大小    年轻代大小
 * -Xms100m -Xmx100m -Xmn40M -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC
 * @author cyb
 * @date 2024/9/11 下午1:34
 */
public class MyHelloGc {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("***************HELLO GC");

        //参数：-Xms100m -Xmx100m -Xmn40M -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC
        //年轻代40m不足以存放50m的对象，所以晋升到老年代
        byte[] bytesArr = new byte[50 * 1024 * 1024];

        //Thread.sleep(Integer.MAX_VALUE);
    }

//    原文链接：https://blog.csdn.net/qq_33229669/article/details/106035861
}
