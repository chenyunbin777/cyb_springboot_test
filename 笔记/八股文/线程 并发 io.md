# volatile

- 1 mesi缓存一致性协议：当CPU写数据时，如果发现操作的变量是共享变量，即其他CPU中也存在该变量的副本，会发出信号通知其他CPU将该变量的缓存行置为无效。因此当其他CPU需要读取这个变量的时候，发现自己的缓存中缓存该变量的缓存行是无效的，那么它就会从内存中重新读取
- 2 可见性的保证
总线嗅探机制：使用 volatile 修饰共享变量后，每个线程要操作变量时会从主内存中将变量拷贝到本地内存作为副本，当线程操作变量副本并写回主内存后，
**会通过 CPU 总线嗅探机制**告知其他线程该变量副本已经失效，需要重新从主内存中读取。
注意：基于 CPU 缓存一致性协议，JVM 实现了 volatile 的可见性，但由于总线嗅探机制，
会不断的监听总线，如果大量使用 volatile 会引起总线风暴。所以，volatile 的使用要适合具体场景。
总线风暴：由于volatile的mesi缓存一致性协议需要不断的从主内存嗅探和cas不断循环无效交互导致总线带宽达到峰值
解决办法：部分volatile和cas使用synchronize

- 总线风暴
由于Volatile的MESI缓存一致性协议，需要不断的从主内存嗅探和cas不断循环，无效交互会导致总线带宽达到峰值。

- 3 有序性：
禁止指令重排序优化：在指令前加一个lock前缀指令
happen-before：volatile修饰的变量，写操作先行发生于读。
as-if-serial：不管怎么重排序（编译器和处理器为了提高并行度），（单线程）程序的执行结果不能被改变。
- 4 如何保证原子性：
Atomic ：UnSafe ：CAS去修改。



#一 锁的实现
1 synchronized：是交给jvm来管理的是jvm的**内置锁**，底层使用的字节码指令是monitorenter和monitorexit来实现
- 1 这两个字节码都需要一个Reference类型的参数来指明要锁定和解锁的对象（指明对象或者去调用的实例或者类对象）
    - 锁计数器会根据monitorenter和monitorenter的执行而加减
- 2 对于同一个线程而言，可重入锁
- 3 非公平锁，抢占式锁，当前所有执行的线程可以进行抢锁操作
- **jdk1.6发布**之后synchronized与ReentrantLock的性能基本上是完全持平，因此性能因素根本不是选择ReentrantLock的理由。
jvm未来对于性能的改进还是偏向于synchronized，所以优选synchronized来实现需求。

实际工作中为什么优先选择syn呢，因为随着jvm的升级优化，syn的性能也是会越来越好的，虚拟机在未来的性能改进中肯定也会更加偏向原生的synchronized
的，所有提倡synchronized可以实现需求的，优先考虑使用synchronized来进行同步操作。



#二 synchronized锁的优化
- 4 自旋锁：避免的线程切换对于资源的消耗，但是他还是要占用CPU的时间，自选的次数一定要有一定的限度。
默认自旋次数10次
- 5 自适应自旋锁：自选时间不在固定，而是由前一次自旋获取锁的时间判断的，
    - 如果前一次获取锁很顺利，那么也证明下一次也有可能成功，那么我们就允许他自旋等待相对更长时间，如100次；
    - 如果对于某个锁，自旋很少成功，那么我们还是直接省略掉该锁的自旋过程。
- 6 锁消除：如果某一代码块并不会发生并发请求的过程，会将加的锁进行消除。
- 锁粗化：如果JVM探测到有这样一串零碎的代码都对同一个对象进行多次的加减锁操作时，
如：for循环内加锁， 多个append加锁。 那么就会将加锁粗化，放到循环外边和包住多个append()方法。
- 7 锁升级过程 
- 先要了解对象头的构成：
    - 第一部分Mark Word：保存GC分代年龄，对象的hashcode，锁状态标志位，线程持有的锁，偏向锁线程id，偏向时间戳。
    - 第二部分保存指向方法区的对象类型数据的指针。如果是数组对象，还有一部分用于保存数组长度。
- 锁状态标志位
- 01：无锁状态
- 01：偏向锁：开启的偏向锁之后，当锁对象第一个次被线程获取的时候，jvm会把对象头中的标志位置为01，然后通过
CAS的方式将**线程id**记录在**mark word**中。
    如果这时有其他线程来获取锁那么就结束偏向模式，改为轻量级锁模式
    同样的，如果明显存在其他线程申请锁，那么偏向锁将很快膨胀为轻量级锁。使用参数-XX:-UseBiasedLocking禁止偏向锁优化（默认打开）
- 00：轻量级锁：2个线程的时候会使用轻量级锁来优化。 在当前线程的栈帧中创建一个**Lock Record**，用于存储锁对象的mark word信息。
之后会通过CAS操作是的 Mark Word更新为指向Lock Record的指针。

那么解锁得操作也是一样的，会通过cas的方式将Mark Word和线程中复制的之前的数据 Lock Record替换回来，这样就完成了解锁操作。
但是如果替换失败的，说明当前有线程在获取锁，那么我们需要在释放锁的同时，唤醒被挂起的线程。

- 10：重量级锁：如果2个线程以上，那么就升级为重量级锁，将 Mark Word存储指向重量级锁（互斥量）的指针。
在解锁的时候通过CAS操作将   Mark Word的信息从Lock Record中替换回来


#三 happen-before 先行发生原则：他是判断数据之间是否存在竞争和线程是否安全的主要依据。
先行发生原则是java内存模型中定义的两项操作之间的偏序关系。 不一定要依靠synchronized和volatile来保证程序的有序性。
- 1 程序顺序：在一个线程内，按照程序代码顺序 执行， 前面先行与后面代码。
- 2 锁获取顺序：对于同一个锁，unlock先行发生与后面对同一个锁的lock操作。
- 3 线程启动规则：Thread的start()先行发生于此线程的每一个动作
- 4 线程终止规则：线程所有操作先行发生于终止检测。
- 5 线程中断规则：对于线程的interrupt()先行发生于被中断线程的代码检测到中断事件的发生。
可通过Thread.interrupted()来检测线程是否中断。
- 6 对象终结规则：一个对象的初始化先行发生于finalize()方法的开始
- 7 传递性：A先行发生于B，B先行发生于C，A先行发生于                                                                  

# 线程 协程 异步
- 线程，会用用户态和内核态的
- 协程：编程语言级别实现，GO lang可以实现的用户自定义的线程，并不需要用户态和内核态的切换。可以大量的开辟 **上千万个协程**。
- 异步：需要操作系统的支持，

# 5个io模型
- 什么是io？磁盘的读写，网络数据的传输。
- **io 的过程： DMA, Direct Memory access,直接存储器**
    - 1 如果Thread1要读取磁盘数据，cpu不会直接操作硬盘，而是cpu向DMA下达指令（磁盘设备信息，文件读取的位置），DMA告知硬盘读取文件。
    - 2 硬盘会将数据读取到内存中 ，读取完成返回消息给 DMA，
    - 3 DMA再以**中断**的形式通知cpu读取完成
    - 4 CPU再去内存中读取数据。
    - 什么是中断：中断是指计算机运行过程中，出现某些意外情况需主机干预时，机器能自动停止正在运行的程序并转入处理新情况的程序，处理完毕后又返回原被暂停的程序继续运行。
    - DMA的作用：提高CPU 的利用率，因为io的操作都交给了DMA去管理，这期间CPU还是可以去执行其他的线程的操作。并不会在该线程上阻塞。
    

我们先来了解下什么是同步/异步，以及什么是阻塞/非阻塞。在IO操作中，IO分两阶段（一旦拿到数据后就变成了数据操作，不再是IO)：

第一阶段：数据准备阶段
第二阶段：内核空间复制数据到用户进程缓冲区（用户空间）阶段 在操作系统中，程序运行的空间分为内核空间和用户空间。 
应用程序都是运行在用户空间的，所以它们能操作的数据也都在用户空间。

一般来讲： 阻塞IO模型、非阻塞IO模型、IO复用模型(select/poll/epoll)、信号驱动IO模型都属于同步IO，因为阶段2是阻塞的(尽管时间很短)。
同步IO和异步IO的区别就在于第二个步骤是否阻塞： 如果不阻塞，而是操作系统帮你做完IO操作再将结果返回给你，那么就是异步IO。

## 一BIO
   - 1 BIO通信模型：阻塞io，分为 单线程，多线程，线程池三种处理方式
    Socket socket = serverSocket.accept(); //该方法将阻塞，直到建立连接。
        - （1） 服务端通过acceptor线程来监听客户端连接。那么我们同会使用一个while（true）来轮询调用accepte()监听客户端的连接
等待客户端的请求。
        - （2） 客户端的请求到达之后，通常会申请一个新的线程来执行对应的请求操作，这时不会在执行其他的客户端请求，其他的请求会一直阻塞知道我们的当前请求执行完毕之后才会执行。
这就是所谓的阻塞io的操作了。
        - 缺点：每次请求都会申请一个新的线程，线程的**频繁的申请与销毁会消耗很多的资源**，而且是类似于一种单线程的方式来处理的。
        - 优点：当然是简单了，每次只处理一个请求。
    - 2 伪异步io：
        - BIO的多线程模式：优化后的阻塞io，防止**频繁的申请与销毁线程**我们改为使用多线程模式来代替单线程的模式，
    我们**使用线程池**来处理客户端的多个请求。
        - 伪异步I/O通信框架采用了线程池实现，因此避免了为每个请求都创建一个独立线程造成的线程资源耗尽问题。
    不过因为它的底层任然是同步阻塞的BIO模型，因此无法从根本上解决问题。
    - 3 BIO总结：TCP连接数小（单机小于1000）的时候，很有优势，简单，每次只处理当前的IO。 如果高并发量的时候"阻塞"这个缺点就暴露无遗。
## 二 NIO：new io或者非阻塞io 或 NON-Boclking io，相比于BIO (多路复用io模型)
同步非阻塞的方式，
- 1 Channel模型：全双工的模式，既可以读也可以写。既可以把channel中的数据读到buffer中，也可以将数据从buffer中读到管道中。
- 2 selector：使用多路复用io模型，只是用一个线程就可以处理成千上万的请求。
要使用Selector，得向Selector注册Channel，然后调用它的**select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪**。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新连接进来，数据接收等。

- 3 ByteBuffer：全部都变成了使用缓冲区来处理读写的数据。处理的速度更快。因为NIO中使用了直接内存，不需要再从主内存中拷贝数据到线程的本地内存中之后再去处理了
    我们可以直接通过：直接内存获取主内存的数据，而不需要将数据拷贝到本地内存在处理了。
    这里呢我们就是用到了一种技术，叫做**PageCache**。java.nio.MappedByteBuffer文件中实现了**零拷贝**的技术。
- ，轮询请求 1 **数据**准备好 2 内核态**复制**到用户态
- JDK 的 NIO 底层由 epoll 实现，该实现饱受诟病的空轮询 bug 会导致 cpu 飙升 100%
    - bug描述:即使是关注的select轮询事件的key为0的话，NIO照样不断的从select本应该阻塞的
    情况中wake up出来
    
    - Netty的解决办法：对Selector的select操作周期进行统计，每完成一次空的select操作进行一次计数，
      若在某个周期内连续发生N次空轮询，则触发了epoll死循环bug。重建Selector，判断是否是其他线程发起的重建请求，
      若不是则将原SocketChannel从旧的Selector上去除注册，重新注册到新的Selector上，并将原来的Selector关闭。
    
   
- 项目庞大之后，自行实现的 NIO 很容易出现各类 bug，维护成本较高
   - netty的ByteBuf和 java nio ByteBuffer区别比较
   - ByteBuf：ByteBuf通过两个位置指针来协助缓冲区的读写操作，读操作使用readerIndex，写操作使用writerIndex。
        - readerIndex和writerIndex的取值一开始都是0，随着数据的写入writerIndex会增加，读取数据会使readerIndex增加，
        但是它不会超过writerIndex。在读取之后，0～readerIndex的就被视为discard的，调用discardReadBytes方法，
        可以释放这部分空间，它的作用类似ByteBuffer的compact方法。readerIndex和writerIndex之间的数据是可读取的，
        等价于ByteBuffer position和limit之间的数据。writerIndex和capacity之间的空间是可写的，
        等价于ByteBuffer limit和capacity之间的可用空间。
        
        
   - java nio ByteBuffer：
   o-position-limit/capacity   :每次读取的都是position-limit之间的数据。  每次读数据前必须使用flip()方法
   limit = postion，postion = 0 ， 才可以读取到真正的数据。
   如果部署需flip()操作的容易出错。 
   


    
## 三 多路复用io ：DMA控制器
- linux系统中一切都是以文件的形式存在的：每一个网络连接在内核中都是以文件描述符file descriptor，简称fd
- **以下是多路io复用的三种实现方式**
    - 1 select处理方式: 
    while(true){
        //通过ulimit -n 命令可以查询rset的大小。
        // rset：是一个bitmap：来表示哪些文件描述符是有数据的，也就是说事件准备好被执行了。 大小为1024个。
        FD_SET(&rset) //1 初始化rset bitmap，全都置位0
        for(int i= 0;i<1024;i++){
            FD_SET(fds[],&rset); //2 将rset进行初始化，例如：fd[5] = {1,2,5,7,9},那么 rset = 1010100110
        }
        
        //3 将rset从用户态拷贝到内核态去判断，到底哪个fd是有可读事件发生。
        //比如：1，2都是有时间发生的，5，7，9没有事件发生。那么就将rset设置更改为 0000000110，然后select()方法返回。
        //为什么linux select函数的第一个参数总应该是fdmax + 1：表示的是文件描述符的数量，从0开始所以比最大的描述符多1
        
        //第一个参数：max+1 传入的原因：fds=1，2，3，5，9  那么 max+1 = 10，内核中就会在rset的 前十个元素中遍历判断
        哪些fd是有事件发生了        
        //第二个参数：读文件描述符集合，第三个参数：写文件描述符集合 ，第四个参数：异常描述符文件集合，第五个参数：超时时间
        select(max+1,&rset,NULL,NULL,NULL);//max:是fd的最大值
        
        //遍历rset集合，判断到底是哪些fd有数据。
        for(int i= 0;i<1024;i++){
            //判断哪一个fd中有数据，也就是说rset中哪一位是1。
            if(FD_ISSET(fds[i],&rset){
                memset(buffer,0,MAXBUF);
                read(fds[],buffer,MAXBUF);//读fd数据到buffer中
                puts(buffer);//将数据进行处理
            }
        }
        
    }
    - select的缺点
        - （1）每次都需要重新初始化rset，重新判断fds[]数组中哪些有数据准备好了，也就是说rset bitmap不可以重用。
        因为你下次在判断的时候你也不知道之前的fb上到底有没有数据，所以需要重新判断。
        - （2）文件描述符是有上限的，有rset大小决定，1024个为上限
        - （3）每次都需要从用户态切换到内核态，有较大的开销
        - （4）for循环遍历两遍fbs[],来判断到底是哪几个fb有数据可以处理。
    
    - 2 poll：每次判断fd是否有数据时，会将pollfd 结构体中的revents 置位为 POLLIN，判断完fd只有在置位成原来的0.
    pollfds[]数组来表示fds[]是否准备好数据了，所以不止1024个fd。
    struct pollfd{
        int fd;
        short events;
        short revents;
    }
        - 解决了（1）（2）
    
    - 3 epoll: 使用场景Redis， ngnix， java nio linux操作系统下
        - （1）首先在内核中创建了
            - 1 红黑树（保存在内核cache中，）：用于保存所有的fd，查询效率相比数组或者bitmap更高。用户线程也不必要每次都把所有的fd拷贝给内核态
            只需要增量的给内核态就好。如果红黑树中不存在就插入其中。
            - 2 就绪列表：用于保存所有的就绪事件（就绪fd），内核直接把就绪的fd返回给用户态即可，不需要每次都拷贝所有的
            fd给用户态，再去让用户态判断一遍。
            
            - 3 回调函数，告诉内核，如果这个fd的中断到了，就把它放到准备就绪list链表里。所以，当一个fd（例如socket）上有数据到了，内核在把设备（例如网卡）上的数据copy到内核中后就来把fd（socket）插入到准备就绪list链表里了。
            
            如此，一颗红黑树，一张准备就绪fd链表，少量的内核cache，就帮我们解决了大并发下的fd（socket）处理问题。
    - 事件触发方式 
        - LT水平触发：只要事件没有处理完毕，每一次epoll_wait都触发该事件， select poll epoll模式都是这个，但是epoll支持边缘触发优化。
        - ET边沿触发：无论事件是否处理完毕，仅触发一次
        
，也可以叫时间监听模型：epoll改进了select 突破了1024个连接，扫描使用了红黑树 nio。



## 三 信号驱动IO模型：信号量
在信号驱动IO模型中，当用户线程发起一个IO请求操作，会给对应的socket注册一个信号函数，然后用户线程会继续执行，
当内核数据就绪时会发送一个信号给用户线程，用户线程接收到信号之后，便在信号函数中调用IO读写操作来进行实际的IO请求操作。
这个一般用于UDP中，对TCP套接口几乎是没用的，原因是该信号产生得过于频繁，并且该信号的出现并没有告诉我们发生了什么事情

## 四 AIO：异步io模型
- **用户线程完全不需要关心实际的整个IO操作是如何进行的**，只需要先发起一个请求，当接收内核返回的成功信号时表示IO操作已经完成，可以直接去使用数据了。
- 所以也可以叫做内核驱动模型，个人理解。
- 注意，异步IO是需要操作系统的底层支持，在Java 7中，提供了Asynchronous IO。简称AIO
- 因为无论是多路复用IO还是信号驱动模型，**IO操作的第2个阶段都会引起用户线程阻塞，也就是内核进行数据拷贝的过程都会让用户线程阻塞。**

## 五 Reactor IO设计模型 和 Proactor模式
在Proactor模式中，当检测到有事件发生时，会新起一个异步操作，然后交由内核线程去处理，当内核线程完成IO操作之后，发送一个通知告知操作已完成，可以得知，异步IO模型采用的就是Proactor模式。

- 多线程模型弊端：服务器为每个client的连接都采用一个线程去处理，使得资源占用非常大。因此，当连接数量达到上限时，再有用户请求连接，直接会导致资源瓶颈，严重的可能会直接导致服务器崩溃。
- 线程池模型弊端：虽然解决了创建和销毁线程的弊端，但是没有解决 如果出现大量的长连接没有释放的情况，可能会导致线程池没有资源来处理新的请求。
这样影响用户体验，所以说这种模式适合，大量短连接的情况。

- 以上的分析，为了解决大量长连接的IO处理，Reactor模式因运而生。
在Reactor模式中，会先对每个client注册感兴趣的事件，然后有一个线程专门去轮询每个client是否有事件发生，
当有事件发生时，便顺序处理每个事件，当所有事件处理完之后，便再转去继续轮询。
- 多路复用IO就是采用Reactor模式

- Reactor 单线程模型：Reactor线程负责dispatch 分发所有的请求到Handler中处理（IO读写，编解码）
Reactor和Hander， 处于一条线程执行单线程模型仅仅适用于handler 中业务处理组件能快速完成的场景。
- Reactor 多线程模型：多线程的Reactor的特点是一个Reactor线程和多个处理线程，将业务处理即process交给线程池进行了分离，Reactor线程只关注事件分发和字节的发送和读取。
- Reactor 主从模型：
对于多个CPU的机器，为了充分利用系统资源会将Reactor拆分为两部分。
    - 1 Main Reactor 负责监听连接，将accept连接交给Sub Reactor处理，主Reactor用于响应连接请求。
    - 2 Sub Reactor 处理accept连接，从Reactor用于处理IO操作请求。
    
- 好处
    响应快，不为单个同步时间所阻塞，虽然Reactor自身依然是同步的。
    编程相对简单，可以最大程度的避免复杂的多线程以及同步问题和多线程以及多进程的切换开销。
    可扩展性，可以方便的通过增加Reactor实例个数来充分利用CPU资源。
    可复用性， Reactor框架本身与具体事件处理逻辑无关，具有很高的复用性。
    Reactor模式的缺点是什么呢？
- 缺点：
    相比传统的模型，Reactor增加了一定的复杂性，因而具有一定的门槛，并且不易于调试。
    Reactor模式需要底层的Synchronous Event Demultiplexer支持，比如Java中的Selector支持，操作系统的select系统调用支持。
    Reactor模式在IO读写数据时会在同一线程中实现，即使使用多个Reactor机制的情况下，那些共享一个Reactor的Channel如果出现一个长时间的数据读写，
会影响这个Reactor中其他Channel的相应时间。例如在大文件传输时，IO操作会影响其他客户端的时间，因而对于这种操作，使用传统的Thread-Per-Connection或许是一个更好的选择，或者采用Proactor模式。
   
Reactor模式的优点很明显：解耦、提升复用性、模块化、可移植性、事件驱动、细粒度的开发控制等。
Reactor模式的缺点也很明显：模型复杂，涉及到内部回调、多线程处理、不容易调试、**需要操作系统底层支持**，
因此导致不同操作系统可能会产生不一样的结果
- 核心组件：Reactor：IO事件的派发者，相当于有分发功能的Selector。
          Acceptor：处理连接的客户端事件
          Handler:用来抽象诸如decode、process、encode这些过程。相当于消息读写处理等操作类。




# JUC：并发包   ：以AQS为核心进行设计的
# AQS：核心：1 自旋  2 CAS  3 LockSupport.park 4 阻塞队列的设计 5 中断锁的设计
AQS：AbstractQuenedSynchronizer抽象的队列式同步器。是除了java自带的synchronized关键字之外的锁机制。
AQS的全称为（AbstractQueuedSynchronizer），这个类在java.util.concurrent.locks包
实现了AQS的锁有：自旋锁、互斥锁、读锁写锁、条件产量、信号量、栅栏都是AQS的衍生物
如图示，AQS维护了一个volatile int state和一个FIFO线程等待队列，多线程争用资源被阻塞的时候就会进入这个队列。
state就是共享资源，其访问方式有如下三种：
getState();setState();compareAndSetState();

AQS 定义了两种资源共享方式：
1.Exclusive：独占，只有一个线程能执行，如ReentrantLock
2.Share：共享，多个线程可以同时执行，如Semaphore、CountDownLatch、ReadWriteLock，CyclicBarrier

以ReentrantLock为例子讲解一下AQS的实现原理：非公平锁或者非公平锁
- state是我们的线索的共享资源，也就是锁资源，
    - 如果获取到锁资源：每次获取锁state都会通过CAS的方式+1 每一次重入获取锁也会CAS +1。
java.util.concurrent.locks.AbstractOwnableSynchronizer.exclusiveOwnerThread 这个当前的独占的线程设置为当前线程
可以通过默认的非公平锁的实现方式源码看的到

## java.util.concurrent.locks.ReentrantLock.FairSync 以**公平锁为例**讲解我们的一个加锁的过程
- 1 第一步调用ReentrantLock.lock()方法内部调用的是下面的方法
final void lock() {
    acquire(1);
}
- 2 第二步：
    - tryAcquire(arg)中主要是尝试去获取锁资源，也就是去判断state的状态是不是0 
    - addWaiter(Node.EXCLUSIVE) 中主要是将当前线程的nowNode节点加入到阻塞队列（其实实现的是一个双向的一个链表）
    如果链表没有初始化的话，我们进行初始化，结果
    head头结点的Node结构：Thread = null， prev = null， next = 当前线程的nowNode
    nowNode：  Thread = currentThread，prev = head，next = tail
    tail：指向nowNode
   
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            //如果acquireQueued返回true，说明我们的线程当前是中断的状态
            //所以这里还原我们线程的中断状态，**不要影响用户的行为（用户设置了）
            selfInterrupt();//Thread.currentThread().interrupt();一行代码，设置线程中断状态
    }
- 2.1 tryAcquire(int acquires) 方法尝试去获取锁：
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) { //如果State==0，说明还没有线程持有锁，
                那么就要判断：1 还是进入阻塞队列等待 2 当前的线程是直接持有锁 
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            //如果还是当前线程尝试获取锁，直接将status+1就好，这就是重入锁的实现
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
- 2.1.1 
https://blog.csdn.net/weixin_30342639/article/details/107372644/
!hasQueuedPredecessors()：这里主要是公平锁的执行逻辑（非公平锁只是没有!hasQueuedPredecessors() &&这个逻辑其他都一样）
**公平锁的目的就是让想要获取锁的线程尽可能的都可以获得到锁**，尽量不出现有某一个线程一直在等待的情况
那么AQS是怎么实现的呢？  

    - FairSync 公平锁，必须要去排队执行
        -         final void lock() {
                      acquire(1); //
                  }

    - NonfairSync 非公平锁，lock会先尝试是否可以获取到锁，如果可以获取到那么就相当于直接插队获取到锁资源直接执行，不需要排队。
        - final void lock() {
                      if (compareAndSetState(0, 1))
                          setExclusiveOwnerThread(Thread.currentThread());
                      else
                          acquire(1);
                  }




主要是如果存在阻塞队列，下一次执行的线程要是阻塞队列的第一个Node.Thread。
public final boolean hasQueuedPredecessors() {
	// The correctness of this depends on head being initialized
	// before tail and on head.next being accurate if the current
	// thread is first in queue.
	Node t = tail; // Read fields in reverse initialization order
	Node h = head;
    Node s;
	//1 h != t 	Node s;如果成立说明当前的队列是有线程的，那么当前队列只好去排队 加入阻塞队列, 返回true。
	//不成立返回false ，说明head==tail没有线程，也就是说没有线程等待，那么我们的当前线程直接去CAS获取锁就好了
	（1）head == null  tail == null 队列没有初始化
	（2）head == tail = Node（Thread = null prev =null） 队列初始化完成，只有一个节点 head和tail都指向这个节点。
	//2 (s = h.next) == null：这个判断的主要目的就是队列中是否只有一个节点（队头队尾都指向的那个节点）
	//3 s.thread != Thread.currentThread() 返回true，就是当前线程不等于在排队的第一个线程s；
	//返回false，表示当前来参与竞争锁的线程和第一个排队的线程是同一个线程
	
	//分析返回值：
	- 1 如果 h != t成立 true，那么队列中一定有线程等待,
	    - 1.1 假设：(s = h.next) == null 是 false（有可能存在中间状态，在h != t判断完之后，队列结构发生了变化）
	    这时就要分析s.thread != Thread.currentThread()的情况
	    s.thread != Thread.currentThread() 
	    （1）返回true，就是当前线程不等于在排队的第一个线程s： true&&（false||true） = true，那么当前线程就不会获取锁，直接尝试加入阻塞队列。
        （2）返回false，表示当前来参与竞争锁的线程和第一个排队的线程是同一个线程：true&&（false||false） = false，那么当前线程就尝试去获取锁
        - 疑问：比如如果在在排队，那么他是park状态，如果是park状态，自己怎么还可能重入啊。
        - 解答疑问：**公平锁的实现精髓所在**       
        举例 ：head-》t2（第一个排队的线程） ，此时正在执行的线程是t1，此时t1执行完成执行锁的释放，调用unlock()方法。
        1 会触发t2的唤醒，那么我们的t2就在acquireQueued(final Node node, int arg)这里执行第二次自旋操作，去尝试获取锁。
        2 这时就会执行 tryAcquire(int acquires) 方法冲尝试获取锁，在调用hasQueuedPredecessors()判断当前线程是不是t2.
        3 那指定是t2，那么我们就可以获取到锁了。
        4 如果在t2判断的同时，过来一个t3的话也去执行tryAcquire(int acquires)尝试获取锁，那么t3并不是第一个排队的线程
        所有t3会在**方法acquireQueued中自旋2次**去判断是否进入队列，还是可有获取到锁资源。
      
        - 1.2  (s = h.next) == null 是 true，说明有可能存在中间状态，在h != t判断完之后，队列结构发生了变化
        变成了只有一个节点newNode，head和tail都指向它. 说明队列中唯一的一个节点线程被唤醒了，也就改变成了这样的结构。
        head->newNode(Thread = null) newNode.next = null
        
        **此时true && true = true，因为已经有线程被唤醒去执行了，那么就去自旋判断 到底是进入队列还是可以得到锁。**
         
    - 2 如果 h != t 不成立 返回false，那么当前线程直接去尝试获取锁。
	//（(s = h.next) == null 或者 当前线程不是head.next，不是头结点的下一个节点）
	return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
}

- 2.2 acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
- 2.2.1 初始化队列，并且将当前的线程的节点加入队列
private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        //1 说明队列已经初始化，直接将node加入到队列中，并且设置为tail
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        //**初始化队列，执行了两次自旋操作，非常牛皮**
        // 第一步：head与tail同事指向一个新newNode(Thread=null)
        // 第二步：head指向newNode，newNode.next = node,node.prev = newNode,tail指向node。
        
        enq(node);
        return node;
    }


- 2.2.2 acquireQueued
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                //设置当前p为当前节点node的前驱节点pred
                final Node p = node.predecessor();
                //这至少要判断两次，
                //第一次自旋 死循环判断 在shouldParkAfterFailedAcquire中先将当前的status状态如果是0设置为-1
                //第二次在判断当前节点的前驱节点是不是head，如果是就直接加锁好了，这里就不需要通过park()阻塞该线程了，
                节省了线程切换的资源，**绝妙设计！！！！**
                
                **如果前一个节点是头节点**，才可以尝试获取资源，也就是实际上的CLH队列中的第一个节点
                队列中只有第一个节点才有资格去尝试获取锁资源（FIFO），如果获取到了就不用被阻塞了
                获取到了说明在此刻，之前的资源已经被释放了
                if (p == head && tryAcquire(arg)) { 
                    //直接设置当前节点为head节点（Thread==null），
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                
               1 走到这里说明要么前一个节点不是head节点，要么是head节点但是尝试加锁失败。
               2 此时将队列中当前节点之前的一些CANCELLED状态的节点剔除；
               3 前一个节点状态如果为SIGNAL时，就会阻塞当前线程，这里的parkAndCheckInterrupt阻塞操作是很有意义的。
               因为如果不阻塞的话，那么获取不到资源的线程可能会在这个死循环里面一直运行，会一直占用CPU资源
               ————————————————
               版权声明：本文为CSDN博主「天瑕」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
               原文链接：https://blog.csdn.net/weixin_30342639/article/details/107372644/
                //parkAndCheckInterrupt()中会进行当前线程的阻塞
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
- 2.2.3 判断线程的状态 0:初始状态  -1：阻塞状态（都是当前节点去设置前一个节点的阻塞状态）
    - 方法 shouldParkAfterFailedAcquire(Node pred, Node node)
    - 参数1：pred 为当前线程node的前一个线程节点   参数2：当前线程节点
    - 问题1：为什么在这个方法中不直接判断waitStatus==0就直接park阻塞呢？
      将当前的线程的前一个节点waitStatus设置为-1，在下一次acquireQueued中的自旋，我们还会进行判断当前线程
      是不是可以获取到锁资源，如果可以获取到锁资源的话，那么就不需要进行"park"阻塞的操作，这样即节省了内存资源 还节省了线程切换的CPU资源
    - 问题2：为什么当前线程要去**修改上一个节点**的waitStatus=-1 而当前线程waitStatus=0？
            为什么不可以直接设置当前线程waitStatus=-1
    因为 ：1 如果当前线程在LockSupport.park(this);之前设置waitStatus=-1，那么如果程序出现什么问题不能执行LockSupport.park(this)
    这时就会有问题了，因为当前线程状态设置为-1表示为阻塞状态，然而并没有阻塞。
    也不可能在LockSupport.park(this);之后设置当前线程状态了，都已经阻塞了，当前线程就已经改变了就无法设置线程的资源了。
    2 还有一个重要的原因就是在ReentrantLock解锁的时候会用到这个状态。
    
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)//  //waitStatus == -1这里返回true，来说明当前线程可以去进行park操作
            /*
             * 如果前一个节点的状态是SIGNAL，意味着当前节点可以被安全地阻塞
             *
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             * 发现传入的前驱的状态大于0，即CANCELLED。说明前驱节点已经因为超时或响应了中断，
             * 而取消了自己。所以需要跨越掉这些CANCELLED节点，直到找到一个<=0的节点
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            //**将当前节点的前一个节点的waitStatus** 设置为-1
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
- 2.2.4 ：parkAndCheckInterrupt()：将当前线程睡眠，并且，醒来之后返回当前线程的中断状态。
    private final boolean parkAndCheckInterrupt() {
        //阻塞当前线程
        LockSupport.park(this);
        //返回的是当前线程的中断状态，但是interrupted方法会移除当前线程的中断状态
        //之后又在tryAcquire(arg)方法中的selfInterrupt()方法中有恢复的中断的状态，因为不能影响用户行为
        return Thread.interrupted();
    }

    - 如果没有获取到所资源：调用addWaiter(Node.EXCLUSIVE)为当前线程和给定模式(独占)创建节点并将其插入队列尾部排队。
    如果队列为null 那么先创建，在插入。
    
    - **重点**：在**初始化队列方法**java.util.concurrent.locks.AbstractQueuedSynchronizer.enq中，
    队列的**head的Node所维护的Thread一直都是null**。 原因就是持有锁的那个线程不参与排队
    然后通过**自旋（for(;;)死循环）+CAS**的方式将当前的线程入队
        private Node enq(final Node node) {
            for (;;) {//自旋
                Node t = tail;//第一次自旋，将tail和head都指向一个Thread = null的一个newNode节点
                if (t == null) { // Must initialize
                    if (compareAndSetHead(new Node()))
                        tail = head;
                } else { //第二次自旋时，t = tail ！= null了 这时走到else。
                    //1 将我们的当前节点的prev指向newNode节点，
                    node.prev = t;
                    //2 将node变为tail节点
                    //3 将newNode节点的next指向当前节点
                    if (compareAndSetTail(t, node)) {
                        
                        t.next = node;
                        return t;
                    }
                }
            }
        }
        //最后的连接情况： head->newNode节点 , newNode节点指向node当前节点，node prev指向newNode，
        此时的node就是tail节点，node.next = null
    
    - acquireQueued(final Node node, int arg)添加到队列之后，在尝试获取锁
        - 1 如果：当前节点的前节点为头节点，也就是说当前节点是队列中的第一个节点，那么我们就尝试去获取锁。
        - 2 如果获取锁获取到了。则清理前节点，头结点指向当前节点。
        - shouldParkAfterFailedAcquire：应该在获取锁失败之后阻塞么？
        这时我们需要判断对应的当前线程的状态：waitStatus，默认的状态都是0
        
    //每一次的ReentrantLock.lock()都有可能会执行一个下面的自旋操作，
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                // 3 判断到这个位置的时候，t2也就是当前的node，  p也就是head节点
                // 此时t2可以获取锁成功，进入if， 我们将t2所在的node设置为head，之前head节点移除队列。
                //
                if (p == head && tryAcquire(arg)) {
                //设置头结点为t2 node， 头结点一定是Thread=null的，prev=null
                    setHead(node);
                    p.next = null; // help GC ，这一步是将head节点出队，移除链表
                    failed = false;
                    return interrupted; //至此我们的自旋结束
                }
                // 1 这里t1执行完成唤醒t2之后，并且t2没有进行中断操作parkAndCheckInterrupt()返回false
                // 2 那么我们继续进行自旋   
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            //如果发生异常调用cancelAcquire方法，此方法是把当前节点先更新为取消状态,并清除该节点。
            if (failed)
                cancelAcquire(node);
        }
    }
    

    /**
     * Convenience method to park and then check if interrupted
     *
     * @return {@code true} if interrupted
     */
    private final boolean parkAndCheckInterrupt() {

        //如果某一个线程t2执行完成下面得park之后，会将程序阻塞到这里
        //如果当前的执行的线程t1执行完成进行锁的释放时，会将head的waitState状态设置为0并且调用LockSupport.unpark(t2);唤醒t2
        //这时如果t2没有进行中断操作，那么parkAndCheckInterrupt()方法就会返回false
        LockSupport.park(this);
        return Thread.interrupted();
    }






- 3 释放逻辑
    
    public final boolean release(int arg) {
        //设置state为 state - 1，如果有重入的情况，直到state==0才释放锁
        if (tryRelease(arg)) {
            Node h = head;
            
            if (h != null && h.waitStatus != 0)
                //唤醒下一个可以执行的线程
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
    private void unparkSuccessor(Node node) {
            /*
             * If status is negative (i.e., possibly needing signal) try
             * to clear in anticipation of signalling.  It is OK if this
             * fails or if status is changed by waiting thread.
             */
             //将头结点的state==-1 CAS置为0
            int ws = node.waitStatus;
            if (ws < 0)
                compareAndSetWaitStatus(node, ws, 0);
            /*
             * Thread to unpark is held in successor, which is normally
             * just the next node.  But if cancelled or apparently null,
             * traverse backwards from tail to find the actual
             * non-cancelled successor.
             */
             //从头节点的下一个节点开始判断
            Node s = node.next;
            //若后继结点为空，或状态为CANCEL（已失效），则从后尾部往前遍历找到最前的一个处于正常阻塞状态的结点,进行唤醒
            //为什么从尾部开始，而不从头部开始：因为为了防止有线程切换，此时遍历的过程中，有可能找不到后续的节点了
            head-》node1-》node2  如果此时node1节点的next == null，也就是找不到 node2几点了。
            if (s == null || s.waitStatus > 0) {
                s = null;
                for (Node t = tail; t != null && t != node; t = t.prev)
                    if (t.waitStatus <= 0)
                        s = t;
            }
            //如果后继节点不是null，那么直接唤醒head节点的下一个节点
            if (s != null)
                LockSupport.unpark(s.thread);
        }


- Node是我们阻塞队列的每一个节点
/** waitStatus value to indicate thread has cancelled */
static final int CANCELLED =  1;   //该节点被取消了
/** waitStatus value to indicate successor's thread needs unparking */
static final int SIGNAL    = -1;   //该节点后续节点需要被唤醒
/** waitStatus value to indicate thread is waiting on condition */
static final int CONDITION = -2;  //该节点进入了等待队列，即Condition的队列里
/**
 * waitStatus value to indicate the next acquireShared should
unconditionally propagate
 */
static final int PROPAGATE = -3;  //共享节点，该节点进锁后会释放锁，。


# AQS 共享模式
- Share：共享，多个线程可以同时执行，如Semaphore、CountDownLatch、ReadWriteLock，CyclicBarrier
## 1 共享锁加锁
AQS获取共享锁是通过调用acquireShared() 这个顶层方法,我们看一下这个方法的源代码:  
  public final void acquireShared(int arg) {
      1 这个方法中有一个if判断,当tryAcquireShared()这个返回值是小于0的时候获取锁失败，进入doAcquireShared（）方法。
      tryAcquireShared方法是用来获取共享模式下的锁,对于tryAcquireShared()这个方法我们重点看一下他的返回值。
      2 当失败的时候返回的是负值,如果返回的是0表示获取共享模式成功但是它下一个节点的共享模式无法获取成功。
      如果返回的是正数也就是大于0,表示当前线程获取共享模式成功,并且它后面的线程也可以获取共享模式。
     if (tryAcquireShared(arg) < 0)
         doAcquireShared(arg);
  }
    private void doAcquireSharedInterruptibly(int arg)
        throws InterruptedException {
        //将共享节点加入队列的尾部
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
        //通过自旋(for(;;))获取前驱节点,如果前驱节点是头节点,那么调用tryAcquireShared()方法获取当前节点的状态,
        注意此方法的返回值在上面已经介绍过,等于0表示不用唤醒后继节点,只有大于0才会唤醒后面的所有节点。
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        //此方法传递了2个参数，一个是当前节点,一个是tryAcquireShared方法的返回值。
                        设置当前加点为头节点，然后判断是否还有共享资源也就是r参数，如果有继续唤醒后续节点。
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                //还是判断当前节点的前驱节点的状态是不是-1，自旋判断2次设置，如果还不可以获取锁，那么就阻塞。
                //跟互斥锁一样的逻辑
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        //设置node为新head
        setHead(node);
        /*
         * The conservatism in both of these checks may cause
         * unnecessary wake-ups, but only when there are multiple
         * racing acquires/releases, so most need signals now or soon
         * anyway.
         */
        // propagate > 0，短路后面的判断
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
            (h = head) == null || h.waitStatus < 0) {
            //唤醒后继共享节点
            Node s = node.next;
            if (s == null || s.isShared())
                //可能会造成不必要的唤醒,而没有锁资源造成重新阻塞
                doReleaseShared();
        }
    }
    

##2 共享锁解锁 
public final boolean releaseShared(int arg) {
        //释放锁，也就是把共享资源还回去，也就是state+++++++1
        if (tryReleaseShared(arg)) {
            //
            doReleaseShared();
            return true;
        }
        return false;
    }

 我们继续看一下具体的唤醒操作doReleaseShared() 这个方法
 private void doReleaseShared() {
    for (;;) {
        Node h = head;
        if (h != null && h != tail) {
            int ws = h.waitStatus;
            //当前线程状态如果是Node.SIGNAL,Node.SIGNAL的值是-1,是一个静态常量，此值表示当前线程被挂起。
            if (ws == Node.SIGNAL) {
                //将当前线程state更新为0
                if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                    continue;            // loop to recheck cases
                //唤醒下一个等待队列中的线程。
                unparkSuccessor(h);
           }
           //如果本身头节点属于重置状态waitStatus==0，并且把它设置为传播状态-3那么就向下一个节点传播,说明该头节点是可以唤醒下一个节点的。
           //至此我们知道了PROPAGATE的作用，就是为了避免线程无法会唤醒的窘境。,因为共享锁会有很多线程获取到锁或者释放锁，所以有些方法是并发执行的，就会产生很多中间状态，而PROPAGATE就是为了让这些中间状态不影响程序的正常运行。
            else if (ws == 0 &&
                     !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                continue;                // loop on failed CAS
       }
        if (h == head)                   // loop if head changed
            break;
   }
 } 
 
- 总结
https://blog.csdn.net/weixin_36586120/article/details/108642253
- ReentrantReadWriteLock中PROPAGATE只是一个中间状态，共享锁的传播性由setHeadAndPropagate完成。
- 对于有资源概念的Semaphore，PROPAGATE和setHeadAndPropagate组合完成共享锁的传播性。 
- 共享锁的传播性目的是尽快唤醒同步队列中等待的线程，使其尽快获取资源（锁），但是也有一定的副作用，可能会造成不必要的唤醒。
- **PROPAGATE只设置给head的waitStatus，让head节点具有传播性。**
- PROPAGATE作为中间状态的流转（h.ws=0 ---> h.ws=-3 ---> h.ws=-1）和临界判断（h.ws < 0）。

**PROPAGATE状态存在的意义是它的符号和SIGNAL相同**，都是负数，所以能用< 0检测到。
因为线程刚被唤醒，但还没设置新head前，当前head的status是0，所以把0变成PROPAGATE，好让被唤醒线程可以检测到。


出现h.ws=0 ---> h.ws=-3的情况：
有一线程获取共享锁后唤醒后继节点（h.ws=-1--->h.ws=0），这时有另一个线程释放了共享锁（h.ws=0--->h.ws=-3）。（ReentrantReadWriteLock和Semaphore都可能有这种情况）
有一线程释放了共享锁（h.ws=-1--->h.ws=0）又有一线程释放了共享锁（h.ws=0--->h.ws=-3）。（Semaphore可能有这种情况，ReentrantReadWriteLock不可能，因为ReentrantReadWriteLock不是每次释放共享锁都会唤醒head后继节点，必须完全释放锁）




linux没有实现，windows实现了。
AsynchronousServerSocketChannel



netty：React模型，io线程和业务线程池
dubbo：最大线程数200
redis：文件事件 业务事件，调整线程数


# 用户态与内核态
- 内核态：系统调用、中断、异常。需要用户态内核态切换

# 线程池
- corePoolSize:超过这个数量的时候，核心线程和普通的线程超过 keepAliveTime 没有任务执行的时候，就会销毁。
- HashSet<Worker>:线程池工作线程的集合，销毁线程的时候，就会把对应的Worker = null；
- Work类是线程池的核心类
    - final Thread thread; 具体工作的线程
    - Runnable firstTask; 第一次执行的任务
    - run() 方法，可以实现复用线程。
        - while (task != null || (task = getTask()) != null) {
            - 1 判断线程状态，先不管
            - 2 判断task是不是null，如果不是null，就执行
            - 3 执行任务，finally中间task =null，进行回收。
            - 4 在进行while判断，task==null了，所以判断**getTask()中是否可以获取到 阻塞队列中的是否有任务**
- 1 Work什么时候被创建呢？
    - executor(new Runnable(()->{}))) 方法创建
        - 1 判断当前线程数与corePoolSize
        - 2 判断队列满不满，如果不满就加入队列，满了就继续下面得判断
        - 3 在创建非核心线程，看是否可以创建成功
            - 创建失败直接抛出异常，一共四个。。。
        - 总结：也就是线程池的工作流程了。
    - addWorker():添加线程
        - 死循环for(;;)
            - 1 判断线程池状态，先不管
            - 2 retry:for(;;)
                - 如果工作线程wc大于当前容量CAPACITY ，返回false；或者如果wc大于等于：核心线程数 or 最大线程数，也返回false
                - CAS添加线程数量，如果成功，那么就跳出循环 break retry。
            - 3 加锁：执行workers.add(w); 尝试添加Worker到workers set集合中，如果添加成功 执行Worker中的线程Thread t.start()
            那么在初始化Worker的时候，thread就是worker，因为把this传了过去
            Worker(Runnable firstTask) {
                        setState(-1); // inhibit interrupts until runWorker
                        this.firstTask = firstTask;
                        this.thread = getThreadFactory().newThread(this);
             }
                
    - submit(new Callable() or new Runnable):内部调用也是 executor()方法，可以有返回值的调用；并且线程池内部执行不会有异常抛出，
    只有get()调用时可以抛出异常。
- 2 什么时候开始执行呢？ 就是在addWorker()方法中t.start();




# ThreadLocal
- 一个线程有可能有多个ThreadLocal线程本地对象，他们对应的都是ThreadLocal中的结构ThreadLocal.ThreadLocalMap threadLocals = null;
- ThreadLocalMap中的Entry[] table 是存放线程本地变量的，
    - Entry中是key value 的结构，key就是ThreadLocal本身，value就是当前线程执行threadLocal.set(value)进去的值。
    
- table默认大小是16，必须为2的幂
- threshold为resize阈值，是table的长度*2/3的负载因子
- ThreadLocalMap使用**线性探测法**来解决散列冲突，所以实际上Entry[]数组在程序逻辑上是作为一个**环形存在**的。
- 计算数组下标的方法是int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
threadLocalHashCode = nextHashCode（一个AtomicInteger变量）+ 0x61c88647
每次计算nextHashCode都会+上一个0x61c88647，这个数与斐波那契散列的黄金分割数有关系。这样计算数组下标可以使分布更均匀，减少hash冲突。

        /**
         * Set the value associated with key.
         *
         * @param key the thread local object
         * @param value the value to be set
         */
        private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);

            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                ThreadLocal<?> k = e.get();
                //如果key存在了直接覆盖value即可
                if (k == key) {
                    e.value = value;
                    return;
                }
                //如果key==null，还需要判断对应的value是不是null的
                if (k == null) {
                    //判断k对应的value是不是null的
                    //1 先向前找到一个key为null 的记录下标
                    //2 先后找，如果找到了key，那么直接覆盖value即可以
                    //3 如果没有找到key，那么我们就新建一个Entry
                    // **最后会遍历table 删除一些key过期的条目**。
                     replaceStaleEntry(key, value, i);
                    return;
                }
            }

            tab[i] = new Entry(key, value);
            int sz = ++size;
            //cleanSomeSlots进行key==null的Entry，如果没有释放任何一个Entry并且当前的大小以及超过了table的阈值
            那么我们就进行rehash操作，将table扩容为之前的2倍
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                rehsh();
        }