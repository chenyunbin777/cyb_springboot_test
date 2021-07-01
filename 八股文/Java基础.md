# 1 ThreadLocal 原理
 - 1 每个线程在对内存中开辟的一块工作内存，同时把线程的共享数据拷贝了一份放进去，相当于做的本地副本，不会像synchronized一样每次修改都要同步到主内存中
 *
 - 2、ThreadLocal的实现是这样的：每个Thread 维护一个 ThreadLocalMap 映射表，这个映射表的 key 是 ThreadLocal实例本身，value 是真正需要存储的 Object。
 *
 - 3、也就是说 ThreadLocal 本身并不存储值，它只是作为一个 key 来让线程从 ThreadLocalMap 获取 value。值得注意的是图中的虚线，表示 ThreadLocalMap 是使用 ThreadLocal 的弱引用作为 Key 的，弱引用的对象在 GC 时会被回收。
 *
 - 4 ThreadLocal可能发生内存泄漏的问题：ThreadLocalMap使用ThreadLocal的弱引用作为key，
 * 如果一个ThreadLocal没有外部强引用来引用它，那么系统 GC 的时候，这个ThreadLocal势必会被回收，
 * 这样一来，ThreadLocalMap中就会出现key为null的Entry，的value，就没有办法访问这些key为null的Entry
 * 如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链：
 * Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value永远无法回收，造成内存泄漏。
 *
 - 5、总的来说就是，ThreadLocal里面使用了一个存在弱引用的map, map的类型是ThreadLocal.ThreadLocalMap. Map中的key为一个threadlocal实例。这个Map的确使用了弱引用，不过弱引用只是针对key。每个key都弱引用指向threadlocal。 当把threadlocal实例置为null以后，没有任何强引用指向threadlocal实例，所以threadlocal将会被gc回收。
 * 但是，我们的value却不能回收，而这块value永远不会被访问到了，所以存在着内存泄露。因为存在一条从current thread连接过来的强引用。只有当前thread结束以后，current thread就不会存在栈中，强引用断开，Current Thread、Map value将全部被GC回收。最好的做法是将调用threadlocal的remove方法，这也是等会后边要说的。
 *
 也就是value与当前线程有一条强引用，如果当前线程一直存在就会存在内存泄漏
 
# 2 谈谈classloader
- 双亲委派模型：加载类的时候会一直向上请求到，最上层启动类加载器（下层是扩展类和系统类加载器）
- 这样做的好处：就是Java类随着他的类加载器一起具备了一种带着优先级的层次关系.**防止一个类经过不同的类加载器加载而被识别成不同的类**.
    - 例如：类java.lang.Object,他存放在rt.jar之中，无论哪个类加载器加载这个类，最终都是委派给最顶层的 **启动类加载器进行加载**，因此Object类在程序的各种类加载器环境中都是同一个类
    - 安全，防止多个加载器加载相同的类，但是识别为不同的类（instanceof）
- 只有被同一个类加载器实例加载并且文件名相同的class文件才被认为是同一个class.
  
## 3 自定义ClassLoader
- 1、为什么要自定义ClassLoader
因为系统的ClassLoader只会加载指定目录下的class文件,如果你想加载自己的class文件,那么就可以自定义一个ClassLoader.
而且我们可以根据自己的需求，对class文件进行加密和解密。
- 2.如何自定义ClassLoader
    - 2.1新建一个类继承自java.lang.ClassLoader,重写它的findClass方法。
    - 2.2将class字节码数组转换为Class类的实例
    - 2.3调用loadClass方法即可

   
# 4 sgi介绍
- osgi，英文全称（Open Service Gateway Initiative）就是动态模块化系统，它能在运行时更新相关的模块，也就是热插拔。
osgi是一个规范，并不是一个实现，目前OSGi规范的主流实现框架有Eclipse Equinox以及Apache Felix。
在osgi中把模块称为bundle。每个bundle有自己的类加载器。每个bundle都有一个/META-INF/MANIFEST.MF，
其中包括了bundle的一些信息（例如暴露给其他bundle的包，启动器等）。它比较适合需求变更比较频繁，扩展需求比较高的应用。
- 链接：https://www.jianshu.com/p/8566f6e7ed0a
```
<dependency>
      <groupId>org.apache.felix</groupId>
      <artifactId>org.osgi.core</artifactId>
      <version>1.4.0</version>
      <scope>provided</scope>
    </dependency>
```
# 5 Https请求慢的解决办法，DNS，携带数据，直接访问IP



# 6 抽象类和接口的区别
- 抽象类
    - 可以有抽象方法，也可以有非抽象方法
    - 主要作用是提取子类的公共方法 
    - 类不可以多继承
- 接口
    - 都是抽象方法
    - 提取实现类的通用实现，他们可以根据这个模板进行自己的实现业务。
    - 可以多实现
    - 都是 public static final 类型的常量
    - 接口之间可以继承
    
# 7 跳跃表
- 我们知道，链表的检索效率是非常低的，如果要拿到100条数据中间的数据，则需要遍历50个数据才行，为了解决这个问题，跳表应运而生
- 节点的插入：其实每个节点的层数是随机的，而且新插入一个节点不会影响其它节点的层数。
因此，插入操作只需要修改插入节点前后的指针，而不需要对很多节点都进行调整。这就降低了插入操作的复杂度。实际上，这是skiplist跳表的一个很重要的特性，这让它在插入性能上明显优于平衡树的方案。
  
 
# 8 什么是死锁？检测死锁？ 如何解决？
- 线程死锁是指由于两个或者多个线程互相持有对方所需要的资源，导致这些线程处于等待状态，无法前往执行。 
  
# 9 并发集合
- Collection：父接口
- List：extends Collection，是Collection子接口
    - ArrayList<E> extends AbstractList<E> implements List
    - LinkedList<E> extends AbstractSequentialList<E> implements List
    
- Set：extends Collection，是Collection子接口
    - HashSet：底层实现是使用的Map的key来保存value。
    - TreeSet：
    
- Map：
    - HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>
    - ConcurrentHashMap<K,V> extends AbstractMap<K,V> implements ConcurrentMap
    - TreeMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>
        - TreeMap中的元素默认按照keys的自然排序排列
        - 底层结构：TreeMap是桶+红黑树的实现方式.TreeMap的底层结构就是一个数组,数组中每一个元素又是一个红黑树.
        当添加一个元素(key-value)的时候,根据key的hash值来确定插入到哪一个桶中(确定插入数组中的位置),
        当桶中有多个元素时,使用红黑树进行保存;
    