package com.cyb.codetest.reference;

import com.alibaba.fastjson.JSON;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

/**
 *
 * 虚引用必须和引用队列 （ReferenceQueue）联合使用。
 * 作用：在这个被虚引用引用的对象被收集器回收时收到一个系统通知
 * 这个讲的很好：https://baijiahao.baidu.com/s?id=1688415473876693422&wfr=spider&for=pc
 * @author cyb
 * @date 2022/3/8 下午4:53
 */
public class PhantomReferenceTest {


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        ReferenceQueue queue = new ReferenceQueue();
        Object object = new Object();
        Date date = new Date();
        /**
         * 虚引用使用上需要注意几点：
         *
         * 1 必须和ReferenceQueue配合使用
         * 2 PhantomReference的get方法始终返回null
         * 3 当垃圾回收器决定对PhantomReference对象进行回收时，会将其插入ReferenceQueue中。
         */
        PhantomReference phantomReference = new PhantomReference(date,queue);


        System.out.println(phantomReference.get());
        //对象设置为null主动调用gc
        date = null;
        System.gc();

        //如果引用队列中存在了object对象，那么证明我们监控的object对象已经被jvm 回收了
        Reference obj = queue.remove();
        if(Objects.nonNull(obj)){
            Field rereferent = Reference.class
                    .getDeclaredField("referent");
            rereferent.setAccessible(true);
            Object result = rereferent.get(obj);
            System.out.println("gc will collect："
                    + result.getClass() + "@"
                    + result.hashCode() + "\t"
                    +  JSON.toJSONString(result));
            System.out.println(JSON.toJSONString(obj));

            System.out.println("虚幻引用对象被jvm回收了");
        }


    }
}
