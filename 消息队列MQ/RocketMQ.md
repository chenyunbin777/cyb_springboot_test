# rocketMQ几个概念：
https://blog.csdn.net/hanziang1996/article/details/93886361

- producer：消息生产者，生产者的作用就是将消息发送到 MQ，生产者本身既可以产生消息，如读取文本信息等。也可以对外提供接口，由外部应用来调用接口，再由生产者将收到的消息发送到 MQ

- producer group: 生产者组，简单来说就是多个发送同一类消息的生产者称之为一个生产者组。在这里可以不用关心，只要知道有这么一个概念即可,Producer实例可以是多机器、单机器多进程、单进程中的多对象。Producer可以发送多个Topic。处理分布式事务时，也需要Producer集群提高可靠性

- consumer : 消息消费者，简单来说，消费 MQ 上的消息的应用程序就是消费者，至于消息是否进行逻辑处理，还是直接存储到数据库等取决于业务需要

- consumer group : 消费者组，和生产者类似，消费同一类消息的多个 consumer 实例组成一个消费者组.Consumer实例 的集合。Consumer 实例可以是多机器、但机器多进程、单进程中的多对象。同一个Group中的实例，在集群模式下，以均摊的方式消费；在广播模式下，每个实例都全部消费。

- Topic : Topic 是一种消息的逻辑分类，比如说你有订单类的消息，也有库存类的消息，那么就需要进行分类，一个是订单 Topic 存放订单相关的消息，一个是库存 Topic 存储库存相关的消息

- Message : Message 是消息的载体。一个 Message 必须指定 topic，相当于寄信的地址。Message 还有一个可选的 tag 设置，以便消费端可以基于 tag 进行过滤消息。也可以添加额外的键值对，例如你需要一个业务 key 来查找 broker 上的消息，方便在开发过程中诊断问题

- Tag : 标签可以被认为是对 Topic 进一步细化。一般在相同业务模块中通过引入标签来标记不同用途的消息

- Broker : Broker 是 RocketMQ 系统的主要角色，其实就是前面一直说的 MQ。Broker 接收来自生产者的消息，储存以及为消费者拉取消息的请求做好准备
    -过提供轻量级的 Topic 和 Queue 机制来处理消息存储,同时支持推（push）和拉（pull）模式以及主从结构的容错机制。
- Name Server : Name Server 为 producer 和 consumer 提供路由信息，**相当于Dubbo的注册中心**
    - 提供轻量级的服务发现和路由。 每个 NameServer 记录完整的路由信息，提供等效的读写服务，并支持快速存储扩展。
- Push Consumer : 应用通常向Consumer对象注册一个Listener接口，一旦收到消息，Consumer对象立刻回调Listener接口方法。所以，所谓Push指的是客户端内部的回调机制，并不是与服务端之间的机制

- Pull Consumer : 应用通常主动调用Consumer从服务端拉消息，然后处理。这用的就是短轮询方式了，在不同情况下，与长轮询各有优点