# 继承关系
- 顶层父接口：Throwable
- 子类：
- Exception ：异常，
    - 分为运行时异常（也称为不检查异常）：NullPointException，ArrayIndexOutOfBoundsException
    - 非运行时异常（检查异常，在编辑期进行捕获）：IOException，
- Error：错误，指的是程序不正常是出现的错误，不可以被预知所以不可以被捕获，如：OutOfMemoryError  StackOverflowError


# 对比Exception与Error的区别
## 举例：ClassNotFoundException 和 NoClassDefFoundError的局别
- 如果编译时能找到这个类，但运行时找不到这个类，就抛出NoClassDefFoundError
- NoClassDefFoundError 也是在运行期发生的。简单的重现方式：**编译后删除依赖的class文件后运行即可**



# 异常的处理方式
- try catch，在catch中打印日志的方式，最好不要使用printStackTrace()，因为这个是将异常与堆栈信息打印在标准输出中，在分布式系统中你很难
判断出到底输出到哪里去了。

- 不要生吞异常：既不打印日志也不将异常抛出，无法判断程序出错的位置
```
try{
逻辑
}catch(Exception e){
这里不做任何处理
}
```
- 尽量不要使用Exception通用的异常要使用特定的异常处理

- 不建议使用try catch将代码块全部包装，只捕获需要捕获的代码，因为会有额外的开销。   不要使用异常处理来控制代码的流程，要使用ifelse switch

- Java每实例化一个Exception都会对当时的栈进行快照，如果次数比较频繁的话 开销就不可以被忽略了




