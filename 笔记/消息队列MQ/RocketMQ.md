# rocketMQ几个概念：
https://blog.csdn.net/hanziang1996/article/details/93886361  实战
https://cloud.tencent.com/developer/article/1172240
- 好处：
    - 1 异步处理，同步性不是特别强的功能，可以交给MQ来异步处理，降低系统处理任务的压力
    - 2 通过发布订阅模式，降低服务之间的耦合
    - 3 削峰，防止同一时间大量的请求发送到我们的consumer端，一时间处理不过来导致服务的异常。
    - 4 灵活可扩展性：RocketMQ 天然支持集群，其核心四组件（Name Server、Broker、Producer、Consumer）每一个都可以在没有单点故障的情况下进行水平扩展。
    - 5 支持顺序消息：可以保证消息消费者按照消息发送的顺序对消息进行消费。顺序消息分为全局有序和局部有序，
    一般推荐使用局部有序，即生产者通过将某一类消息按顺序发送至同一个队列来实现。
        - 分区有序：一个Topic可能会对应多个分区queueId，单个分区消费有序
        - 全局有序：一个Topic设置一个分区即可
-  RocketMQ 的部署结构图，里面涉及了 RocketMQ 核心的四大组件：Name Server、Broker、Producer、Consumer ，每个组件都可以部署成集群模式进行水平扩展。
    - 生产者（Producer）负责产生消息，生产者向消息服务器发送由业务应用程序系统生成的消息。 RocketMQ 提供了三种方式发送消息：同步、异步和单向。
        - 同步：同步发送指消息发送方发出数据后会在收到接收方发回响应之后才发下一个数据包。一般用于重要通知消息，例如重要通知邮件、营销短信。
        - 异步：异步发送指发送方发出数据后，不等接收方发回响应，接着发送下个数据包，一般用于可能链路耗时较长而对响应时间敏感的业务场景，例如用户视频上传后通知启动转码服务。
        - 单向：单向发送是指只负责发送消息而不等待服务器回应且没有回调函数触发，适用于某些耗时非常短但对可靠性要求并不高的场景，例如日志收集。
    - 生产者组
      生产者组（Producer Group）是一类 Producer 的集合，这类 Producer 通常发送一类消息并且发送逻辑一致，所以将这些 Producer 分组在一起。从部署结构上看生产者通过 Producer Group 的名字来标记自己是一个集群。
    - 消费者：消费者（Consumer）负责消费消息，消费者从消息服务器拉取信息并将其输入用户应用程序。
    站在用户应用的角度消费者有两种类型：拉取型消费者、推送型消费者。
        - 拉取型消费者：拉取型消费者（Pull Consumer）主动从消息服务器拉取信息，只要批量拉取到消息，用户应用就会启动消费过程，所以 Pull 称为主动消费型。
        - 推送型消费者：推送型消费者（Push Consumer）封装了消息的拉取、消费进度和其他的内部维护工作，将消息到达时执行的回调接口留给用户应用程序来实现。
    - Broker 消息服务器：是消息存储中心消息服务器，主要作用是接收来自 Producer 的消息并存储， Consumer 从这里取得消息。
        - Master 既可以写又可以读，Slave 不可以写只可以读
        - 从物理结构上看 Broker 的集群部署方式有四种：
            - 1 单 Master：单点问题，不建议
            - 2 多 Master：缺点是单台机器宕机期间，该机器上未被消费的消息在机器恢复之前不可订阅，消息实时性会受影响。
            - 3 多 Master 多 Slave（异步刷盘）（异步复制）：每个 Master 配置一个 Slave，所以有多对 Master-Slave，消息采用异步复制方式，主备之间有毫秒级消息延迟。
                - 也就是当master收到消息后，先向producer 返回ack成功状态，然后再异步将数据同步至salve。
                - 缺点是 Master 宕机时在磁盘损坏情况下会丢失极少量消息。
                
            - 4 多 Master 多 Slave（同步双写）：每个 Master 配置一个 Slave，所以有多对 Master-Slave ，消息采用同步双写方式，主备都写成功才返回成功。
                - 优点是：数据与服务都没有单点问题，Master 宕机时消息无延迟，服务与数据的可用性非常高。
                - 缺点是：性能相对异步复制方式略低，发送消息的延迟会略高。
                
            - 5 复制（同步和异步）：复制指的是当消息发送到对应的master broker之后，如何将数据同步给对应的salve。
            - 6 刷盘（同步和异步）：刷盘是指，数据发送到broker之后 如何将数据写入到磁盘中。
            - 7 很重要：一个master可以配置多个salve 但是如果master宕机之后，也只会一个
            slave中拉取消息，**所以生产环境建议salve不要配置太多**
            - 9 broker关机恢复机制：
                - （1）abort：我们正常启动mq是会创建一个abort空文件，正常关闭mq的时候会删除这个文件
                但是当异常关闭的时候不会删除掉这个文件。
                - （2）checkpoint：是保存broker正常存储各种数据的时间。
                    - 数据日志存储时间：最后一条已经存储的Commit log的时间
                    - consume queue：最后一条已经存储的consume queue索引的时间
                    - index log：最后一条已经存储的index log时间
            - 10 commit log异步复制流程
                - HAService.AcceptSocketService（底层实现是java 的nio）:当Master broker启动的时候会启动这个线程服务去监听Slave的同步数据请求
                并且创建一个HAConnection
                - HAConnection（底层实现是java 的nio）：也会同时创建HAConnection.WriteSocketService 和 HAConnection.ReadSocketService
                    - HAConnection.ReadSocketService（是一个线程） ：读取接受slave的同步数据请求，保存到HAConnection中
                    - HAConnection.WriteSocketService（是一个线程）：将读取到的slave的同步数据请求，从Commit log中查询数据，并发送给salve。
                    - volatile long slaveRequestOffset：表示salve请求同步的位点值，volatile保证多线程的可见性
                    - volatile long slaveAckOffset：表示salve已经保存的位点值，保证多线程的可见性
             
            - 11 commit log同步复制流程 
                - org.apache.rocketmq.store.CommitLog.submitReplicaRequest 中处理同步消息
                - 1 BrokerRole配置为同步master SYNC_MASTER，如果salve是正常的，我们创建同步请求封装在GroupCommitRequest中。
                - 2 HAService.GroupTransferService groupTransferService :会通过同步加锁（原子锁）的方式，将同步消息放入队列requestsWrite
                中。
                - 3 GroupTransferService中同步成功，每消费完成一个salve 就唤醒一个consumer消费者，并且设置设置flushOKFuture为true
                - 同步逻辑在org.apache.rocketmq.store.ha.HAService.GroupTransferService.doWaitTransfer
                - org.apache.rocketmq.store.ha.HAService.GroupTransferService.swapRequests：交换requestsWrite和requestsRead队列
                ``` 
                    public CompletableFuture<PutMessageStatus> submitReplicaRequest(AppendMessageResult result, MessageExt messageExt) {
                      if (BrokerRole.SYNC_MASTER == this.defaultMessageStore.getMessageStoreConfig().getBrokerRole()) {
                          HAService service = this.defaultMessageStore.getHaService();
                          if (messageExt.isWaitStoreMsgOK()) {
                              if (service.isSlaveOK((long)result.getWroteBytes() + result.getWroteOffset())) {
                                  CommitLog.GroupCommitRequest request = new CommitLog.GroupCommitRequest(result.getWroteOffset() + (long)result.getWroteBytes(), (long)this.defaultMessageStore.getMessageStoreConfig().getSlaveTimeout());
                                  service.putRequest(request);
                                  service.getWaitNotifyObject().wakeupAll();
                                  return request.future();
                              }
              
                              return CompletableFuture.completedFuture(PutMessageStatus.SLAVE_NOT_AVAILABLE);
                          }
                      }
              
                      return CompletableFuture.completedFuture(PutMessageStatus.PUT_OK);
                    }
                ```
                - 索引
                    - Consume Queue：用于消费者拉取消息，更新消费点位offset，对应类ConsumeQueue
                        - ConsumeQueue：物理点位 + 消息体大小 + Tag hash值
                    - Index File：Hash索引，每一个消息都会有一个唯一key，主要是通过Topic + key 查询时使用,对应类IndexFile
    - NameServer ：用来保存 Broker 相关元信息并给 Producer 和 Consumer 查找 Broker 信息。
        - 所以从功能上看应该是和 ZooKeeper 差不多，据说 RocketMQ 的早期版本确实是使用的 ZooKeeper ，后来改为了自己实现的 NameServer 。
        - 1 Topic路由管理：最核心模块，决定了Topic分区数据会保存在哪个broker上，就是最为网络路由存在
        - 2 Remoting通信模块：基于netty的网络通信封装，是RocketMQ的公共模块，担任MQ的各个模块间的通信任务。
        - 3 定时任务模块：定时扫描宕机的broker，定时打印KV配置，定时扫描超时请求。  
        - 4 KV管理模块，全剧配置
    - Tag：使用标签，同一业务模块不同目的的消息就可以用相同 Topic 而不同的 Tag 来标识。
- MQ有什么不足之处么？
    - 消息重复问题，它不能保证不重复，只能保证正常情况下不重复
    - 不支持分布式事务
- 如何保证消息可靠性？
    - 同步双写：多broker 多slaver，同步双写机制，master和slaver都写入成功才会返回，这样保证了MQ消息的可靠性。
- 如何保证顺序消费
    - 同步：同步发送指消息发送方发出数据后会在收到接收方发回响应之后才发下一个数据包。


#RocketMQ消息的生产者模式模式
- 普通消息
- 分区有序消息，但broker有序
- 事务消息：half消息到broker（消费者不可见），生产者用户处理本地事务，成功则提交commit到broker，broker重新投递消息。
失败，则rollback
    - 如果生产者一直没有commit，broker会有一个检测机制，超时就会将half消息删除
- 异步消息消息，生产者拥有毁掉方法


##RocketMQ消息的两种消费模型
- 1 广播消费（Broadcasting）
广播消费模式下，相同消息**消费者组（Consumer Group）**的每个消费者（Consumer）实例都接收全量的消息。
即在该模型下，每一条消息会被消费者组内的每一个消费者成员消费读取到。
    - 应用场景：刷新分布式缓存

- 2 集群消费（Clustering） 常用模型
集群消费模式下,相同**消费者组（Consumer Group）的每个消费者（Consumer）实例平均分摊消息。
即该模型下，每条消息只能被消费者组（Consumer Group）**内的一个消费者成员消费读取到。


## 可靠消费
- 正常消费+重试+死信（Dead Lock Queue）
- 正常消费失败，重试16次，如果还没有完成就放入死信队列，代人工处理
- 死信队列topic：%DLQ%XXX
- 重试队列topic：%RETRY%XXX

## MQ存储结构
- commit log，每一个文件大小是1G，尾部通过offset相连。
- 为什么写入文件这么快， 零拷贝，pageCache技术。减少了用户态和内核态数据的拷贝。
直接通过java进程拷贝得到内核态的数据。
    - java.nio.MappedByteBuffer.java文件中实现了零拷贝技术。这需要操作系统底层的支持。
    - 零拷贝的速度相当于**内存操作**，所以速度更快。不需要再去磁盘中读取数据。 

## MQ索引文件
- consumer queue：消费队列，主要用于消费的拉取消息，更新消费位点。
    - 物理位点：保存了消息在commit log中的位点值。
    - 消息体大小
    - tag 的hash值

- index file：使用的hash索引，类似于hashMap，按照消息的产生顺序进行存储。
    - hash槽中保存最新消息的指针。

## Broker删除过期文件机制
- 1 删除过期的消息文件CommmitLog
    - 三个条件：
    - 1 当前时间==已经配置的过期时间
        - **注意：条件：当前时间 >= 文件最新修改时间+过期时间**
        - **配置在哪里配置？**
    - 2 磁盘使用空间超过85%
    - 3 手动执行删除
- 2 commit log文件删除之后，索引文件也无用了，所以也会定期索引文件。

## 事务消息
- 类似于2PC 2阶段提交，
- 1 生产者发送Half消息给 broker，此时消息不会被消费
- 2 生产者收到broker的回复消息之后，执行本地事务
- 3 执行成功后，向broker发送commit消息，说你已经可以提交事务了。
    - 如果本地事务执行失败，那么就发送rollback给broker进行回滚。
- 4 此时消息就对消费者可见，就可以进行消费了。

- 问题：如果broker没有收到生产者的commit 或者 rollback消息的话， broker有一个
"回查机制"，
    - timeout：超时时间，多长时间没有收到commit 或者 rollback 就进行回查。
    - checkMax：最大回查次数，如果超过这个次数的话，事务消息将被忽略。
    


# 如何处理消息堆积

- 1.增加消费者数量：
    . 通过增加消费者实例来提高消息处理能力。
    . 确保消费者能够并行处理消息。
  2.优化消费者处理逻辑：
    . 检查并优化消息处理代码，提高单个消费者的处理效率。
    . 考虑使用批量处理来减少网络开销。
  3.使用背压（Back Pressure）机制：
    . 实现一个机制，当消费者处理不过来时，可以向生产者发出信号减缓生产速度。
  4.消息优先级处理：
    . 实现消息优先级队列，优先处理重要消息。
  5.临时存储和延迟处理：
    . 将过多的消息临时存储到其他存储系统（如数据库），稍后再处理。
  6.横向扩展：
    . 增加服务器资源，扩展消费者集群。
  7.监控和告警：
    . 实施监控系统，及时发现并报告消息堆积问题。
- 消费降级：减小消息的产生                     
  原文链接：https://blog.csdn.net/u014109358/article/details/141363486