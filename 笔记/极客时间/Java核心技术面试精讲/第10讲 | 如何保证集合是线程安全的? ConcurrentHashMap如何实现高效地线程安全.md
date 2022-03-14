# 第10讲 | 如何保证集合是线程安全的? ConcurrentHashMap如何实现高效地线程安全？
## java 7
- 在JDK1.7中ConcurrentHashMap采用了数组+Segment+分段锁的方式实现。
- 分段锁示意图：通过分段加锁segment，一个hashmap里有若干个segment，每个segment里有若干个桶，桶里存放K-V形式的链表，
put数据时通过key哈希得到该元素要添加到的segment，然后对segment进行加锁，然后在哈希，计算得到给元素要添加到的桶，然后遍历桶中的链表，替换或新增节点到桶中

Segment--->Key,Value
       --->Key,Value--->Key,Value
       
Segment--->Key,Value
       --->Key,Value       
 
Segment--->Key,Value
       --->Key,Value--->Key,Value
       
       
       
- ConcurrentHashMap中的分段锁称为Segment，它即类似于HashMap的结构，
即内部拥有一个Entry数组，数组中的每个元素又是一个链表,同时又是一个ReentrantLock（**Segment继承了ReentrantLock**）。
- Segment的数量是由concurrencyLevel决定的，默认是16，也可以在构造方法中指定。他必须是2的幂次方，如果输入的参数是15这种会自动调整为2的幂次方，如16

- size()方法：如果不进行同步，简单计算所有的Segment的总值，可能会因为并发put导致结果不准确，但是直接锁定所有的Segment进行计算，就会变得非常昂贵
    - 分段计算两次，两次结果相同则返回，否则对所以段加锁重新计算
## java8
- 不在使用Segment，初始化操作大大简化，修改为lazy-load形式。
- **1.8以后的锁的颗粒度，是加在链表头上的，这个是个思路上的突破。**
- 数据存储利用volatile来保证可见性
- 使用CAS等操作，在特定场景下无所并发操作
- 使用Unsafe，LongAdder之类的底层手段，进行极端的情况的优化。 **使用Unsafe减少了间接调用底层逻辑的开销，这里直接使用的就是底层调用逻辑**
    - private static final sun.misc.Unsafe U;
    - initTable() 初始化使用U.compareAndSwapInt(this, SIZECTL, sc, -1) 来CAS轮询判断是否可以创建Node<K,V>[] tab数组
    - tabAt(Node<K,V>[] tab, int i) ：U.getObjectVolatile获取Node节点
```

 //数据存储Node的数据结构
 static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key; //final修饰保证不可变
        volatile V val; //保证val的可见性
        volatile Node<K,V> next;
        // … 
    }

final V putVal(K key, V value, boolean onlyIfAbsent) { if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        //f就是当前位置的Node
        Node<K,V> f; int n, i, fh; K fk; V fv;
        //第一次put的时候table没有初始化，则初始化table
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        //通过哈希计算出一个表中桶bucket的位置,如果该位置没有Node就去创建一个Node，这里实现的就是懒加载
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // 如果链表是空的，利用CAS去进行无锁线程安全操作
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                break; 
        }
        /*
         * 如果检测到某个节点的hash值是MOVED，则表示正在进行数组扩张的数据复制阶段，
         * 则当前线程也会参与去复制，通过允许多线程复制的功能，一次来减少数组的复制所带来的性能损失
         */
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else if (onlyIfAbsent // 不加锁，进行检查
                 && fh == hash
                 && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                 && (fv = f.val) != null)
            return fv;
        else {
            V oldVal = null;
            //对链表的头部加锁
            synchronized (f) {
                   // 细粒度的同步修改操作... 
                   // 新值的覆盖和新值的插入
                }
            }
            // 链表超过阈值8，进行树化
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}

```