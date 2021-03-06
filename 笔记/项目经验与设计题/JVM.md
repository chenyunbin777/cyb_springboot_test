# 一 一个对象的创建的流程
jvm遇到一条new指令时先去检查是否可以在常量池中定位到一个类的符号引用，
并且检查这个符号引用代表的类是否已经经过了类加载的过程

- 1 类加载机制：
    - 1 类加载：
        - 通过类的全限定名找到对应的类的二进制字节流
        - 将二进制字节流（静态数据结构）转换为方法区运行时结构
        - 在内存中生成一个java.lang.Class对象,作为运行时方法区这个类的访问入口
    
    - 2 验证：是否符合字节码的标准。
    为了确保Class文件的字节流中包含的信息符合当前虚拟机的要求,并且不会危害虚拟机自身的安全.
    大致会完成下面4个阶段的检验动作:文件格式验证,元数据验证,字节码验证,符号引用验证
    
    - 3 准备：赋0值的过程，是将static类型的变量初始化成0，
    而不包括实例变量,实例变量将会在对象实例化时随着对象一起分配在Java堆中.
    static int a = 123;这个阶段之后还是0
    特殊情况：如果是static final a = 123;这个阶段是123
    - 4.解析:虚拟机将**常量池内**的符号引用替换为直接引用的过程
    
    常量池：计数器+数据区域（cp_info）
    常量池内部数据是由cp_info结构组成一个cp_info结构对应一个常量。在字节码中共有14种类型的cp_info（如下图6所示），每种类型的结构都是固定的。
    cp_info = tag(1字节) + 数据，先由tag确定常量池的对象是哪个，在根据之后对象的结构来进行字节码的解析。
     
    符号引用：的字面量形式是定义在.class文件中的。
            - 1.类或接口的解析
            - 2.字段解析
            - 3.类方法解析
            - 4.接口方法解析
    - 5 初始化：初始化static类变量和static静态代码块
        - 父类中定义的静态语句块要优先于子类的变量赋值操作
        - 执行顺序 
            父类静态代码块
            子类静态代码块
            父类代码块
            父类构造方法
            子类代码块
            子类构造方法
       static{
            i = 100;//可以复制，但是不可以访问定义在static块之后的变量
       }     
       static int i = 1;
       ---------------------
       static int a = 1;
       static{
            a = 2; 
       }
       
       执行初始化方法<clinit>()之后a = 2；，因为是按代码的顺序执行的            
       
    
- 2 双亲委派模型：
    - 1 是否加载完成
    - 2 如果父加载器!=null，调用父类的loadClass()
    - 3 如果父加载器==null,则默认直接调用顶层的启动类加载器进行类加载
    - 4 如果父加载器都没有加载成功，那么抛出ClassNotFoundException
    - 5 最后在使用自己的findClass()来加载。
    
    - 破坏双亲委派模型的一些方式，破坏，重写loadClass() （正常自定义类加载器应该重写findClass()方法）
        - Java SPI的实现，使用父类加载器请求子类加载器完成类加载
        - OSGi：热部署，
    - 好处：保证我们加载的类的安全，
    你想想如果没有双亲委派模型：使用不同的类加载器价在同一个类，那么这就是不相同的类。
    如果有双亲委派模型：那么就是相同的类了。如果你自定义一个Object类，放在你的classpath下我们将会有多个Object类，
    我们的系统将会一片混乱。
    
    保证java程序的稳定运作
    
- 3 为对象分配空间：从java堆划分出一块内存区域来
    
    - 1 为对象分配内存空间的方式
        - 指针碰撞：内存空间是绝对规整的，通过指针的移动来分配对象的内存。
            - 适用于 标记整理或者复制算法的gc回收器，Serial，ParNew
        - 空闲列表：内存是不规整的，通过列表记录哪些内存块时可用的，然后分配一块足够大小的内存。
            - CMS，标记整理，内存不规整，有内存碎片，适用于空闲列表
        
    - 2 分配完内存之后，java需要对该内存空间都初始化为**零值**
        - 这一步保证了 实例字段在java代码中可以不赋初始值，就可以直接使用。
     
    - 3 设置对象头数据：设置GC分代年龄，hashcode，线程id，锁标志位，类的元数据信息
    
         - （1）Mark Word：GC分代年龄，hashcode，线程id，锁标志位，类的元数据信息
         - （2）如果是数组结构，还会保存一个数组的长度
         - （3）对其填充：jvm规定对象的大象一定是8字节的整数倍，如果不够填充来补齐。   
     
    - 4 初始化对象，执行<init> 方法初始化对象，这样一个真正可用的对象才算完全产生出来。     


# 二 对象的布局
## 1 对象头（Mark Word）
- gc分代年龄，对象hashcode
- 类元数据指针：jvm通过这个指针来确定，该对象是哪个类的实例。
- 锁标志位
- 偏向线程id
- 线程持有的锁
- 特殊情况：如果是数组对象，还必须包括一个记录数组长度的信息
    
## 2 实例数据

## 3 对齐填充
- 因为HotSpot VM的自动内存管理系统要求对象的起始地址必须是8字节的整数倍
也就是对象大小必须是8字节的整数倍，如果对象实例数据部分没有对齐时，填充来补全


# 三 对象的访问定位
- 1 句柄池：
    -通过java栈的本地变量表中的Reference引用数据-》句柄池（到对象实例的数据的指针）（java堆中）
-》实例池（java堆中）或者 -》对象类型数据（方法区）

    - 好处：对象在堆中被移动后，不需要修改reference信息。
- 直接指针：通过Reference指针定位到对象实例数据的位置。（HotSpot默认的）
    - 好处：速度更快，节省了一次定位数据的开销

# 四 HotSpot 的算法实现
----------------------以下是重点----------------------
- 1 准确式GC：虚拟机知道内存中的某个位置是什么类型的变量，也就是说可以知道值到内存中保存的到底是数值呢还是引用。
比如说：内存中保存123整数。jvm就是知道他到底是reference引用类型指向123的内存地址还是一个数值为123的整数。
    - 那如何实现的准确式GC呢？ 通过数据结构OopMap，在类加载完成时，计算出对应的数据。可以更好的帮助GC Root对象的枚举
  
- 3 热点探测技术：Hot Spot采用的是基于：**计数器的热点探测** ：会为每个方法和每个代码块建立计数器，执行次数超过指定的阈值之后
说明该代码就是热点代码。那么这时就会执行 JIT编译器，编译称为本地代码，提高执行效率
    - 1 方法调用计数器：记录方法调用次数
    - 2 回边计数器：记录方法中循环体的调用次数

- 6 安全点：GC的位置，也就是Stop The World的位置。
    - 选定标准：是否有让程序长时间执行的特征。也就是指令序列复用：方法调用，循环跳转，异常跳转
    - 主动式中断：为了让所有的线程都跑道 safe point在执行GC。 
        实现方式： jvm设置一个标志位，所有的线程执行时会主动轮询这个标志位，遇到之后就可以进行GC操作了。
        
- 7 安全区域：如果程序线程不执行的时候呢（如：线程Sleep或者blocked状态），就不会去轮询safepoint 的标志位，
这个时候我们通过安全区域来处理。
    - 概念：安全区域：就是在一段代码片段中，引用关系不会产生变化。在这个区域内执行GC都是安全的。
----------------------以上是重点----------------------


- 4 为什么HotSpot虚拟机要实现两个不同的即时编译器的原因？？？
    - HotSpot虚拟机中内置了两个即时编译器：Client Complier和Server Complier，简称为C1、C2编译器，
分别用在客户端和服务端，目前主流的HotSpot虚拟机中默认是采用解释器与其中一个编译器直接配合的方式工作。
    - 程序使用哪个编译器，取决于虚拟机运行的模式。HotSpot虚拟机会根据自身版本与宿主及其的硬件性能**自动选择运行模式**，
    - 用户也可以使用“-client”或“-server”参数去强制指定虚拟机运行在Client模式或Server模式。
    - **用Client Complier获取更高的编译速度，用Server Complier 来获取更好的编译质量。**
   
- 5 GC Root对象：全局静态遍历和常量和栈帧本地变量表
- 2 编译器和解释器混合工作模式
     - 编译器：会事先用比较多的时间把整个程序的源代码编译成另外一种代码，后者往往较前者更加接近机器码，所以执行的效率会更加高。时间是消耗在预编译的过程中。
         - JIT编译器：当虚拟机发现某个方法或代码块运行特别频繁时，就会把这些代码认定为“Hot Spot Code”（热点代码），
         为了提高热点代码的执行效率，在运行时，虚拟机将会把这些代码编译成与本地平台相关的机器码，这样可以提高效率。
     - 解释器：解释器会一行一行的读取源代码，解释，然后立即执行。
      

# GC相关
## GCRoot对象
- java虚拟机栈的生成的栈帧中的本地变量表中的对象。
- 本地方法栈，c++引用的 ，也就是native
- final 和 static类型的变量

## 年轻代如何进入老年代
- 优先Eden区域分配对象
- 大对象进入老年代，例如特别长的字符串或者数组。
- 根据对象的年龄age来判断是否进入老年代，默认age》=15就会进入老年代，也可以自己设置。
- 动态对象年龄判断：年轻代中 ToSurvivor空间有相同年年龄的对象大于一半ToSurvivor空间，年龄大于或等于这个年龄的对象进入老年代



# jdk1.6实现的逃逸分析
- 1 栈上分配对象内存：如果方法内变量不会被外部方法或变量获取的到的对象，我们可以将其在栈上分配，
这样的好处就是会随着方法的销毁而销毁

- 2 同步消除：如果一个变量确定不会逃逸出线程，不会被其他线程访问，我们就可以消除其同步状态，也就是锁。
synchronized (new Object()){ //每次代码块都会new一个新的锁，所以每个线程锁住的都是不同的对象
           
       }     
- 3 标量替换：创建一个对象时，如果确定了该对象中哪些变量可以用到的话，我们可以直接在栈上创建对应的变量，而不创建对象。
这样优化了内存的配置和gc的消耗。

# JVM 分析命令
- jps
- jstat:查看内存的状态
    - jstat -gcutil 3087 1000
    S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT  
    0.00  30.12   9.78   0.01  92.60  85.71      1    0.009     0    0.000    0.009 都是百分比
    
    - jstat -gc 3087 1000 打印内存的值
    S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
    10752.0 10752.0  0.0   3238.6 65536.0  33913.4   175104.0     16.0    10496.0 9719.2 1280.0 1097.1      1    0.009   0      0.000    0.009
    - jstat -gcnew 3087 1000 打印新生代的值
     S0C    S1C    S0U    S1U   TT MTT  DSS      EC       EU     YGC     YGCT  
     512.0 4096.0  256.0    0.0  3  15 4096.0  60416.0   9131.0      8    0.045
    - jstat -gcold 3087 1000  
    MC       MU      CCSC     CCSU       OC          OU       YGC    FGC    FGCT     GCT   
    10496.0   9961.1   1280.0   1122.8    175104.0      2636.5      8     0    0.000    0.045
- jmap： jmap [option] <pid>  java进程的内存状态给dump下来
    - jmap -histo pid | head -n20   查看堆内存中的存活对象，并按空间排序
    - jmap -histo[:live] pid  打印java对象堆（存活）的直方图 
    - jmap -dump:format=b,file=heap 7276：使用hprof二进制形式,输出jvm的heap内容到文件中（文件可以指定）
        - 通过**JVisualVM工具**导入dump出来的堆内存文件，同样可以看到各个对象所占空间
    - jmap histo:live

- jstack pid：打印线程的信息，可以发现死锁，死循环等线程的信息
    - Found one Java-level deadlock。 死锁