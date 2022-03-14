# 第8讲 | 对比Vector、ArrayList、LinkedList有何区别？

- Vector:线程安全，通过动态数组实现，扩容时变为原数组的2倍
- ArrayList：非线程安全，通过动态数组实现，扩容时变为原数组的1.5倍
- LinkedList:非线程安全，通过双向链表实现

# 对比
- Vector ArrayList：适合随机访问；不适合删除插入，会移动数组元素
- LinkedList：适合删除插入，不适合随机访问


# 一些实用方法
- 快速生成list：List<String> of = List.of("12", "34");


# 实现一个优先级队列，vip客户 普通客户
- 程序代码方法实现：实用java的优先级队列PriorityQueue
- 技术架构方面实现
