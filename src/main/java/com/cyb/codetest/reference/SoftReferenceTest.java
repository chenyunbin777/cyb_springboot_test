package com.cyb.codetest.reference;

import com.alibaba.fastjson.JSON;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Date;

/**
 * @author cyb
 * @date 2022/3/10 上午11:48
 */
public class SoftReferenceTest {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue queue = new ReferenceQueue();
        Object object = new Object();
        Date date = new Date();
        SoftReference softReference = new SoftReference(date,queue);

        softReference=null;
        System.gc();

        Reference obj = queue.remove();

        //这里的obj就是一个虚幻引用，那么如果获取到虚幻引用引用到的对象呢
        //java异常.md.lang.ref.PhantomReference@7aec35a
        System.out.println("obj==="+obj);
        System.out.println("obj==="+ JSON.toJSONString(obj));

    }
}
