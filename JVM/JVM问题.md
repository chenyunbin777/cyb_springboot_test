一 JVM内存分哪几个区，每个区的作用是什么

二 直接内存（堆外内存）和堆内存对比

三 简述内存分配与回收策略

四 对象的创建

五 · 简述重排序，内存屏障，happen-before，主内存，工作内存

#六 Java中存在内存泄漏问题吗？请举例说明

内存泄漏：也就是会有对象无法回收掉，一直占用的内存的空间
在内存对象明明已经不需要的时候，还仍然保留着这块内存和它的访问方式（引用），这是所有语言都有可能会出现的内存泄漏方式。编程时如果不小心，我们很容易发生这种情况，如果不太严重，可能就只是短暂的内存泄露。
解决办法：
（1）所以应该尽量减小对象的作用域。

（2）以及手动设置null值。
```例子：
   public class Simple {
       Object object;
       public void method1(){
           object = new Object();
           //...其他代码
       }
   }
```

这里的object实例，其实我们期望它只作用于method1()方法中，且其他地方不会再用到它，但是，当method1()方法执行完成后，object对象所分配的内存不会马上被认为是可以被释放的对象，只有在Simple类创建的对象被释放后才会被释放，严格的说，这就是一种内存泄露。解决方法就是将object作为method1()方法中的局部变量。当然，如果一定要这么写，可以改为这样：

```
public class Simple {
    Object object;
    public void method1(){
        object = new Object();
        //...其他代码
        object = null;
    }
}
```
这样，之前“new Object()”分配的内存，就可以被GC回收。



# 七 jstack，jstat，jmap，jconsole怎么用
- jmap
    - jmap -histo pid | head -n20   查看堆内存中的存活对象，并按空间排序





# 八 32 位 JVM 和 64 位 JVM 的最大堆内存分别是多数？32 位和 64 位的 JVM，int 类型变量的长度是多数？
理论上说上 32 位的 JVM 堆内存可以到达 2^32，即 4GB，但实际上会比这个小很多。不同操作系统之间不同，
如 Windows 系统大约 1.5 GB，Solaris 大约 3GB。64 位 JVM允许指定最大的堆内存，理论上可以达到 2^64，
这是一个非常大的数字，实际上你可以指定堆内存大小到 100GB。甚至有的 JVM，如 Azul，堆内存到 1000G 都是可能的。   

int 类型变量的长度是8字节，32位不会变。


# 九  JVM自身会维护缓存吗？是不是在堆中进行对象分配，操作系统的堆还是JVM自己管理堆
答案：是的，JVM自身会管理缓存，它在堆中创建对象，然后在栈中引用这些对象。


# 十 什么情况下会发生栈内存溢出
栈帧创建超过了所允许的范围。 可以由-Xss来设置堆栈大小

	
# 十一 双亲委派模型是什么
是几种类加载器组合而成的一种模型。当顶层启动类无法加载这个请求，子加载器才会尝试加载。

从开发者的角度，类加载器可以细分为：
启动（Bootstrap）类加载器：负责将 Java_Home/lib下面的类库加载到内存中（比如rt.jar）。由于引导类加载器涉及到虚拟机本地实现细节，开发者无法直接获取到启动类加载器的引用，所以不允许直接通过引用进行操作。
标准扩展（Extension）类加载器：是由 Sun 的 ExtClassLoader（sun.misc.Launcher$ExtClassLoader）实现的。它负责将Java_Home /lib/ext或者由系统变量 java.ext.dir指定位置中的类库加载到内存中。开发者可以直接使用标准扩展类加载器。
应用程序（Application）类加载器：是由 Sun 的 AppClassLoader（sun.misc.Launcher$AppClassLoader）实现的。它负责将系统类路径（CLASSPATH）中指定的类库加载到内存中。开发者可以直接使用系统类加载器。由于这个类加载器是ClassLoader中的getSystemClassLoader()方法的返回值，因此一般称为系统（System）加载器。
除此之外，还有自定义的类加载器，它们之间的层次关系被称为类加载器的双亲委派模型。该模型要求除了顶层的启动类加载器外，其余的类加载器都应该有自己的父类加载器，而这种父子关系一般通过组合（Composition）关系来实现，而不是通过继承（Inheritance）。


# 十二 并行与并发
- 并行：只多条垃圾收集线程并行工作，但此时用户线程仍然处于等待状态
- 并发：只用户线程与垃圾收集线程同时执行（但不一定是并行的，可能会交替执行），用户程序在继续运行，而垃圾收集程序运行在另一个cpu上


# 十三 遇到线上full gc 怎么办？从止血方案 说到排查 再到解决问题？你的思路是怎么样的？
https://www.jianshu.com/p/e749782fff2b?from=singlemessage
https://blog.csdn.net/qqxx6661/article/details/108245054
https://blog.csdn.net/weixin_39647412/article/details/111666872


- 止血方案：加内存，重启机器
- 排查：dump线上gc日志，使用java的jvisualvm工具


# 十四 锁膨胀的过程
https://blog.csdn.net/fan1865221/article/details/96338419
- 在 jdk6 之后便引入了“偏向锁”和“轻量级锁”，所以总共有4种锁状态，级别由低到高依次为：无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态。
这几个状态会随着竞争情况逐渐升级，此过程为不可逆。所以 synchronized 锁膨胀过程其实就是无锁 → 偏向锁 → 轻量级锁 → 重量级锁的一个过程。

[ˈmɑːnɪtər] 

- 在使用 synchronized 来同步代码块的时候，编译后，会在代码块的起始位置插入 **monitorenter指令**，在结束或异常处插入 **monitorexit指令**。
当执行到 monitorenter 指令时，将会尝试获取对象所对应的 ** monitor **的所有权，即尝试获得对象的锁。而 synchronized 用的锁是存放在 Java对象头 中的。
所以引出了两个关键词：“Java 对象头” 和 “Monitor”

对象头中的 Mark Word 保存着锁的标志位： 无锁：01 偏向锁：01 轻量级锁：00 重量级锁：10
- 1 无锁
- 2 偏向锁：是指当一段同步代码一直被同一个线程所访问时，即不存在多个线程的竞争时，那么该线程在后续访问时便会自动获得锁，从而降低获取锁带来的消耗，即提高性能。
    - 当一个线程访问同步代码块并获取锁时，会在 Mark Word 里存储锁偏向的线程 ID。在线程进入和退出同步块时不再通过 CAS 操作来加锁和解锁，而是检测 Mark Word 里是否存储着指向当前线程的偏向锁。
    轻量级锁的获取及释放依赖多次 CAS 原子指令，而偏向锁只需要在置换 ThreadID 的时候依赖一次 CAS 原子指令即可。
    - 偏向锁只有遇到其他线程尝试竞争偏向锁时，持有偏向锁的线程才会释放锁，线程是不会主动释放偏向锁的。
    - 偏向锁在 JDK 6 及之后版本的 JVM 里是默认启用的。可以通过 JVM 参数关闭偏向锁：-XX:-UseBiasedLocking=false，关闭之后程序默认会进入轻量级锁状态。
- 3 轻量级锁：引入轻量级锁的主要目的是：在多线程竞争不激烈的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗。
    - 需要注意的是轻量级锁并不是取代重量级锁，而是在大多数情况下同步块并不会出现严重的竞争情况，所以引入轻量级锁可以减少重量级锁对线程的阻塞带来的开销。
    - 所以偏向锁是认为环境中不存在竞争情况，而轻量级锁则是认为环境中不存在竞争或者竞争不激烈，所以轻量级锁一般都只会有少数几个线程竞争锁对象，
    **其他线程只需要稍微等待（自旋）下就可以获取锁**，但是自旋次数有限制，如果自旋超过一定的次数，或者一个线程在持有锁，一个在自旋，又有第三个来访时，轻量级锁膨胀为重量级锁，
    重量级锁使除了拥有锁的线程以外的线程都阻塞，防止CPU空转。

```
W
souchelogs@sc-prod-coastline-001:~$ ps -ef | grep java
souche    1635     1  3 Dec10 ?        16:40:17 /opt/souche/java/bin/java -Djava.util.logging.config.file=/home/souche/projects/coastline/.base/conf/logging.
properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catali
na.webresources -server -Xms2g -Xmx2g -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -Xmn768m -XX:MaxDirectMemorySize=256m -XX:SurvivorRatio=8 -XX:+UseConc
MarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSMaxAbortablePrecleanTime=5000 -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:+
UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrent -Dsun.rmi.dgc.server.gcInterval=2592000000 -Dsun.rmi.dgc.client.gcInterval=2592000000 -Xloggc:
/home/souche/projects/coastline/logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Dfile.encoding=UTF-8 -Dcom.alibaba.rocketmq.client.sendSmartMsg=false
 -Djava.security.egd=file:/dev/./urandom -javaagent:/home/souche/bin/transmittable-thread-local-2.7.0-SNAPSHOT.jar -javaagent:/home/souche/bin/souche-agent/c
oastline/scTracer/agent/souche-agent.jar -Djmx.export.registerUrl=http://optimus-eureka.internal.souche-inc.com/eureka -DAPP_NAME=coastline -DAPP_HOME=/home/
souche/projects/coastline -Ddubbo.op_token=xWZlkX1AZoizJXWw -Dignore.endorsed.dirs= -classpath /opt/souche/tomcat/bin/bootstrap.jar:/opt/souche/tomcat/bin/to
mcat-juli.jar -Dcatalina.base=/home/souche/projects/coastline/.base -Dcatalina.home=/opt/souche/tomcat -Djava.io.tmpdir=/home/souche/projects/coastline/.base
/temp org.apache.catalina.startup.Bootstrap start



souche   25276     1 36 Dec25 ?        2-04:51:35 /opt/souche/java/bin/java -Djava.util.logging.config.file=/home/souche/projects/cupid-x/.base/conf/logging.
properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catali
na.webresources -server 


-Xms4g  堆初始大小 4g
-Xmx4g  最大堆大小 4g
-Xmn1536m 年轻代大小
-XX:MetaspaceSize=256m 元空间初始值
-XX:MaxMetaspaceSize=256m  元空间最大值
-XX:MaxDirectMemorySize=512m 直接内存
-XX:SurvivorRatio=8 Eden占新生代的8/10 Eden：Survivor：Survivor = 8：1：1
-XX:+UseConcMarkSweepGC   使用CMS收集器
-XX:+UseCMSCompactAtFullCollection  默认就是开启的：用于在CMS顶不住要进行FGC的时候后开启内存整合碎片。 但是会增加停顿时间。
-XX:CMSMaxAbortablePrecleanTime=5000   并发可中止的预清理阶段：扫描多长时间（默认5秒）就终止该阶段
-XX:+CMSClassUnloadingEnabled 
-XX:CMSInitiatingOccupancyFraction=80 ：CMS的回收阈值是 80
-XX:+UseCMSInitiatingOccupancyOnly  ：遇上个参数配合使用，如果没有则CMSInitiatingOccupancyFraction会根据实际情况改变
-XX:+ExplicitGCInvokesConcurrent ：命令JVM无论什么时候调用系统GC，都执行CMS GC，而不是Full GC。
-Dsun.rmi.dgc.server.gcInterval=2592000000  控制Full gc时间间隔
-Dsun.rmi.dgc.client.gcInterval=2592000000  控制Full gc时间间隔

https://blog.csdn.net/flysqrlboy/article/details/88679457
2.2 CMS垃圾回收的6个重要阶段
initial-mark 初始标记（CMS的第一个STW阶段），标记GC Root直接引用的对象，GC Root直接引用的对象不多，所以很快。
concurrent-mark 并发标记阶段，由第一阶段标记过的对象出发，所有可达的对象都在本阶段标记。
concurrent-preclean 并发预清理阶段，也是一个并发执行的阶段。在本阶段，会查找前一阶段执行过程中,从新生代晋升或新分配或被更新的对象。通过并发地重新扫描这些对象，预清理阶段可以减少下一个stop-the-world 重新标记阶段的工作量。
concurrent-abortable-preclean 并发可中止的预清理阶段。这个阶段其实跟上一个阶段做的东西一样，也是为了减少下一个STW重新标记阶段的工作量。增加这一阶段是为了让我们可以控制这个阶段的结束时机，比如扫描多长时间（默认5秒）或者Eden区使用占比达到期望比例（默认50%）就结束本阶段。
remark 重标记阶段（CMS的第二个STW阶段），暂停所有用户线程，从GC Root开始重新扫描整堆，标记存活的对象。需要注意的是，虽然CMS只回收老年代的垃圾对象，但是这个阶段依然需要扫描新生代，因为很多GC Root都在新生代，而这些GC Root指向的对象又在老年代，这称为“跨代引用”。
concurrent-sweep ，并发清理。

-Xloggc:/home/souche/projects/cupid-x/logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Dfile.encoding=UTF-8 -Dcom.alibaba.rocketmq.client.sendSmartMsg=false
-Djava.security.egd=file:/dev/./urandom -javaagent:/home/souche/bin/transmittable-thread-local-2.7.0-SNAPSHOT.jar -javaagent:/home/souche/bin/souche-agent/cu
pid-x/scTracer/agent/souche-agent.jar -Djmx.export.registerUrl=http://optimus-eureka.internal.souche-inc.com/eureka -DAPP_NAME=cupid-x -DAPP_HOME=/home/souch
e/projects/cupid-x -Ddubbo.op_token=W49CcvataLvhV27D -Dignore.endorsed.dirs= -classpath /opt/souche/tomcat/bin/bootstrap.jar:/opt/souche/tomcat/bin/tomcat-ju
li.jar -Dcatalina.base=/home/souche/projects/cupid-x/.base -Dcatalina.home=/opt/souche/tomcat -Djava.io.tmpdir=/home/souche/projects/cupid-x/.base/temp org.a
pache.catalina.startup.Bootstrap start
```






