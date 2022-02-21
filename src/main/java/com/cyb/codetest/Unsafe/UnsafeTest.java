package com.cyb.codetest.Unsafe;

import sun.misc.Cleaner;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 堆内内存通常，我们在 Java 中创建的对象都处于堆内内存（heap）中，堆内内存是由
 * JVM 所管控的 Java 进程内存，并且它们遵循 JVM 的内存管理机制，JVM 会采用
 * 垃圾回收机制统一管理堆内存。
 * <p>
 * 堆外内存：与之相对的是堆外内存，存在于 JVM 管控之外的内
 * 存区域，Java 中对堆外内存的操作，依赖于 Unsafe 提供的操作堆外内存的 native
 * 方法。
 *
 * @author cyb
 * @date 2022/2/17 下午5:44
 */
public class UnsafeTest {


    public static void main(String[] args) {

//        Unsafe unsafe = reflectGetUnsafe();
        long size = 1024;

        //分配内存
//        long base = unsafe.allocateMemory(size);


        //内存初始化
//        unsafe.setMemory(base, size, (byte) 0);

        //跟踪DirectByteBuffer对象的垃圾回收，以实现堆外内存的释放
//        Cleaner cleaner = Cleaner.create(this, new Deallocator());


        //Cleaner继承自 ref.PhantomReference 虚幻引用

        /**
         * 当某个被 Cleaner 引用的对象将被回收
         *         时，JVM 垃圾收集器会将此对象的引用放入到对象引用中的 pending 链表中，等待
         *         Reference-Handler 进行相关处理。其中，Reference-Handler 为一个拥有最高
         *         优先级的守护线程，会循环不断的处理 pending 链表中的对象引用，执行 Cleaner
         *         的 clean 方法进行相关清理工作
         */


        /**
         * 所以当 DirectByteBuffer 仅被 Cleaner 引用（即为虚引用）时，其可以在任意
         * GC 时段被回收。当 DirectByteBuffer 实例对象被回收时，在 Reference-Handler
         * 线程操作中，会调用 Cleaner 的 clean 方法根据创建 Cleaner 时传入的 Deallocator 来进行堆外内存的释放。
         */




    }


//    private static Unsafe reflectGetUnsafe() {
//
//        try {
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            return (Unsafe) field.get(null);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return null;
//        }
//
//    }
}
