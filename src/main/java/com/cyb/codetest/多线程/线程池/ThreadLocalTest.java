package com.cyb.codetest.多线程.线程池;

/**
 * cnblogs.com/jobbible/p/13364292.html 原理讲解的很透彻
 * ThreadLocal是什么？
 * 每个线程在对内存中开辟的一块工作内存，同时把线程的共享数据拷贝了一份放进去，相当于做的本地副本，不会像synchronized一样每次修改都要同步到主内存中
 * <p>
 * 2、ThreadLocal的实现是这样的：每个Thread 维护一个 ThreadLocalMap 映射表，这个映射表的 key 是 ThreadLocal实例本身，value 是真正需要存储的 Object。
 * <p>
 * 3、也就是说 ThreadLocal 本身并不存储值，它只是作为一个 key 来让线程从 ThreadLocalMap 获取 value。值得注意的是图中的虚线，表示 ThreadLocalMap 是使用 ThreadLocal 的弱引用作为 Key 的，弱引用的对象在 GC 时会被回收。
 * <p>
 * 4 ThreadLocal可能发生内存泄漏的问题：ThreadLocalMap使用ThreadLocal的弱引用作为key，
 * 如果一个ThreadLocal没有外部强引用来引用它，那么系统 GC 的时候，这个ThreadLocal势必会被回收，
 * 这样一来，ThreadLocalMap中就会出现key为null的Entry，就没有办法访问这些key为null的Entry的value，ove();
 * 如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链：
 * Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value永远无法回收，造成内存泄漏。
 * <p>
 * 5、总的来说就是，ThreadLocal里面使用了一个存在弱引用的map, map的类型是ThreadLocal.ThreadLocalMap. Map中的key为一个threadlocal实例。这个Map的确使用了弱引用，不过弱引用只是针对key。每个key都弱引用指向threadlocal。 当把threadlocal实例置为null以后，没有任何强引用指向threadlocal实例，所以threadlocal将会被gc回收。
 * 但是，我们的value却不能回收，而这块value永远不会被访问到了，所以存在着内存泄露。因为存在一条从current thread连接过来的强引用。只有当前thread结束以后，current thread就不会存在栈中，强引用断开，Current Thread、Map value将全部被GC回收。最好的做法是将调用threadlocal的remove方法，这也是等会后边要说的。
 *
 * @author cyb
 * @date 2021/1/2 10:15 下午
 */
public class ThreadLocalTest {

    public static void main(String[] args) {
        ThreadLocal threadLocal1 = new ThreadLocal();
        ThreadLocal threadLocal2 = new ThreadLocal();
        ThreadLocal threadLocal3 = new ThreadLocal();
        ThreadLocal threadLocal4 = new ThreadLocal();

        threadLocal1.set("a");
        threadLocal2.set("b");
        threadLocal3.set("b");
        threadLocal4.set("b");


//        threadLocal1.remove(); //这样就不会有内存泄漏的情况


    }
}
