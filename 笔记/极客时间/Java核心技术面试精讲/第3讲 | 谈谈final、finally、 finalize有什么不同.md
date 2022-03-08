# 第3讲 | 谈谈final、finally、 finalize有什么不同

##final
- 修饰类：不可以被继承
- 修饰方法：不可以被重写
- 修饰变量：变量不可以被赋值
    - 如果是集合list，例如：
    ```
 //修饰还是可以被添加或者修改元素的 只是不能被赋值修改
 final List<String> strList = new ArrayList<>();
 strList.add("Hello");
 strList.add("world");  
 List<String> unmodifiableStrList = List.of("hello", "world");
 unmodifiableStrList.add("again");
    ```
    
    
## finalize
- finalize是Object的一个方法 ，他的设计目的是保证对象在被垃圾收集前完成特定资源的回收。java9开始被标记为deprecated。
- 忘记这个方法就可以了 就当他不存在就好了

## finalize扩展
- 有没有替换finalize的方式？答案必定是有的
	- 	如果是想要做一些对象回收后的资源回收或者资源关闭等操作。可以利用java的虚幻引用PhantomReference，他的作用是在某一些对象被回收的时候发出一个通知告知想要得到这个消息的代码去执行一些额外的任务。
	- 使用方法:
		- 1 必须和ReferenceQueue配合使用
		- 2 PhantomReference的get方法始终返回null
		- 3 当垃圾回收器决定对PhantomReference对象进行回收时，会将其插入ReferenceQueue中。
		我们可以利用使用方法3来在某些对象被jvm回收之后执行一些操作
- 例子： 这里只是简单的用法，实际上可以在线程中去执行监听的操作
```
ReferenceQueue queue = new ReferenceQueue();
Object object = new Object();
PhantomReference phantomReference = new PhantomReference(object,queue);
 //一定是null
System.out.println(phantomReference.get()); 
//如果引用队列中存在了object对象，那么证明我们监控的object对象已经被jvm 回收了
Reference obj = queue.poll();
if(Objects.nonNull(obj)){
      System.out.println("虚幻引用对象呗jvm回收了");
}
```

- java中的虚幻引用使用场景：NIO中的直接内存或者堆外内存中，源码中会使用虚幻引用来做直接内存的回收处理

## finally
- 异常处理最后进行的逻辑可以放在这里，如资源关闭，日志打印等

## 总结
- 这三个里边最重要的就是final，重点要关注final的用法和使用场景。

# 评论
谈谈final、finally、 finalize有什么不同？
这三个就是卡巴斯基和巴基斯坦的关系，有个基巴关系。。。