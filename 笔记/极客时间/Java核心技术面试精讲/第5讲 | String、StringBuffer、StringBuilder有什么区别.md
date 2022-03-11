# 第5讲 | String、StringBuffer、StringBuilder有什么区别？

- 底层实现都是char数组（jdk9之后是byte数组实现）

## String 
- final类型不可以被修改，如果通过+来复制给String并不是修改了 而是创建了一个新的String类型的变量
- 字符串缓存：java6之后提供了intern()方法，
    - 缺点：需要显式的调用有代码污染。
- 保证了基础线程安全，因为你无法对他内部数据进行任何的修改

## StringBuffer
- 线程安全的String，所有的方法都是用Synchronized修饰
- 不必纠结于synchronized的性能，"过早优化都是万恶之源"

## StringBuilder
- 线程不安全

