# CAS 相关

- 什么是 CAS? 即比较并替换，实现并发算法时常用到的一种技术。CAS 操
  作包含三个操作数
    - 内存位置
    - 预期原值
    - 新值。
- 执行 CAS 操作的时候，将内存位置的值与预期原值比较，如果相匹配，那么处理器会自动将该位置值更新为新值，
否则，处理器不做任何操作。 只要是内存中的值与预期的原值是一样的，也就是说没有被其他的线程改变，那么就可以进行更新（没有并发修改）
  
- CAS 是一条 CPU 的原子指令（cmpxchg 指令），不会造成所谓的数据不一致问题，Unsafe 提供的 CAS 方法（如
  compareAndSwapXXX）底层实现即为 CPU 指令 cmpxchg。

  
## 典型应用
- CAS 在 java.util.concurrent.atomic 相关类、Java AQS、CurrentHashMap
  等实现上有非常广泛的应用。如下图所示，AtomicInteger 的实现中，静态字段
  valueOffset 即为字段 value 的内存偏移地址，valueOffset 的值在 AtomicInteger
  初 始 化 时， 在 静 态 代 码 块 中 通 过 Unsafe 的 objectFieldOffset 方 法 获 取。 在
  AtomicInteger 中提供的线程安全方法中，**通过字段 **valueOffset** 的值可以定位到
  AtomicInteger 对象中**value的内存地址**，从而可以根据 CAS 实现对 value 字段的
  原子操作。**

- 