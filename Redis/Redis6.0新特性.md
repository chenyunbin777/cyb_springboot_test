# 一 多线程的支持
## 配置
 - io-threads-do-reads no   //默认是不开启Redis多线程的，如果需要开启设置为yes
 - 注释:Usually threading reads doesn't help much.
 NOTE 1: This configuration directive cannot be changed at runtime via
 CONFIG SET. Aso this feature currently does not work when SSL is
 enabled.//无法通过在运行时更改此配置指令配置集,Aso启用SSL时，此功能当前不起作用。
 NOTE 2: If you want to test the Redis speedup using redis-benchmark, make
 sure you also run the benchmark itself in threaded mode, using the
 --threads option to match the number of Redis threads, otherwise you'll not
 be able to notice the improvements.
 // 如果你想用Redis基准测试Redis的加速，那么当然，您也可以在线程模式下运行基准测试本身，
 使用--threads选项匹配Redis线程的数量，否则您将无法注意到改进。也就是说**开启多线程后，还需要设置线程数，否则是不生效的。**
 
 
 - io-threads 4  //设置线程核数
    - Redis is mostly single threaded, however there are certain threaded
    operations such as UNLINK, slow I/O accesses and other things that are performed on side threads.
    - Now it is also possible to handle Redis clients socket reads and writes
    in different I/O threads. Since especially writing is so slow, normally
    Redis users use pipelining in order to speed up the Redis performances per core, and spawn multiple instances in order to scale more. Using I/O threads it is possible to easily speedup two times Redis without resorting to pipelining nor sharding of the instance.
    - By default threading is disabled, we suggest enabling it only in machines
    that have at least 4 or more cores, leaving at least one spare core.
    Using more than 8 threads is unlikely to help much. We also recommend using threaded I/O only if you actually have performance problems, with Redis instances being able to use a quite big percentage of CPU time, otherwise there is no point in using this feature.
    - So for instance if you have a four cores boxes, try to use 2 or 3 I/O
    threads, if you have a 8 cores, try to use 6 threads. In order to
    enable I/O threads use the following configuration directive:
    
    - Redis大多是单线程的，但是也有某些线程诸如取消链接、慢速I/O访问和其他在边线程上执行的操作。
    现在还可以处理Redis客户端的socket读写，Redis用户使用流水线来加速每个核心的Redis性能，并生成多个实例以扩展更多。
    **使用I/O线程可以轻松地将Redis的速度提高两倍，而无需借助于管道或实例的分片。**
    
    - 默认情况下线程被禁用，我们建议只在计算机中启用它**至少有4个或更多cpu核**，至少留下一个空闲的核。
    
    - **使用超过8个线程不大可能有多大帮助**。我们还建议：**仅当您实际存在性能问题时才使用线程I/O**，
    因为Redis实例可以占用相当大比例的CPU时间，否则使用此功能没有任何意义。
    
    - 例如，如果你有一个四核的机器，试着使用2或3个I/O，线程，如果你有一个8核机器，尝试使用6线程。
 

# 二 客户端缓存
https://www.jianshu.com/p/539edd85b186
讲解与练习：https://chenssy.blog.csdn.net/article/details/107241158?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-3.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-3.control
这个比较详细的命令中的各个选项的使用方法：https://blog.csdn.net/wsdc0521/article/details/106766096
##  什么是客户端缓存(Client side caching)
- 通常的缓存会放在应用和DB之间，比如redis。客户端缓存是指在应用服务内部再加一层缓存，也就是内存缓存，从而进一步提升访问速度。

## 命令
- CLIENT TRACKING ON|OFF [REDIRECT client-id] [PREFIX prefix] [BCAST] [OPTIN] [OPTOUT] [NOLOOP]

## Redis处理客户端缓存的方式
- Redis会"记住"每个客户端请求的key，当key的值发现变化时会**发送失效信息**给客户端(invalidation message)。
- 应用收到请求后自行处理有变化的key, 进而实现client cache与redis的一致。
- redis对客户端缓存的支持方式被称为Tracking（追踪），分为两种模式：
    - 默认模式：Redis Server端**维护一个失效表**记录每个Client访问的Key，当发生变更时，向client推送数据过期消息。
        - 优点：只对Client发送其访问过的被修改的数据
        - 缺点：Server端需要额外存储较大的数据量。
    - 广播模式：客户端订阅key前缀的广播(空串表示订阅所有失效广播)，Redis服务端**维护一个前缀表**记录key前缀与client的对应关系。当相匹配的key发生变化时，通知所有访问过相同的key前缀的client。
        - 优点：服务端记录信息比较少，内存消耗少
        - 缺点：客户端会收到自己未访问过的key的失效通知
        
## RESP3协议
- RESP全称 RedisSerializationProtocol
- redis6.0开始使用新的协议RESP3。该协议增加了很多数据类型。新协议目的之一是支持客户端缓存。
- hello命令: client告知服务端使用的协议版本，服务端返回一些简要的版本。发送hello 2, 表示使用RESP2， hello 3表明使用RESP3协议。默认开始的是RESP2。
- client tracking on/off: 开启/关闭tracking
- push数据：带外数据，它是redis主动推送的数据。向client推送的数据过期消息即是通过此协议实现的。
> 注意: 只有开启hello 3的端，才能接收push数据(key失效数据)



## 练习
- 1 普通模式 使用 telnet localhost 6379 来模拟客户端
    - telnet localhost 6379
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    hello 3  // 开启RESP3协议
    ... 省略展示信息
    - client tracking on  //开启tracking并读取a的值
    +OK
    set a 1
    +OK
    get a
    $1
    1
    
    - 这个时候如果使用redis-cli作为另外一个client更新a的值，telnet这个client应该能获得通知:
    - set a 5  更新a为5 
    也会收到a的失效通知
    2
    $10
    invalidate
    *1
    $1
    a
    
    - 如果不再次获取a的值，就算是再次修改a的值也不会受到失效通知
    - set a 5 再次将a设置为5 ，客户端也会收到失效通知
    
    - 设置 a 10s后过期， 这时客户端也会收到对应的失效通知
    - 127.0.0.1:6379> expire a 10
    (integer) 1

>总结: 
>1 当开启了tracking后，客户端缓存的key如果在别处被修改为与原值一样，也会收到失效消息；
>2 当客户端缓存失效后，该key再被修改时，客户端不会再收到消息；
>  也就是如果客户端不在再查询一次该key，那么就不会收到失效通知；当客户端再查询该key之后 才会在客户端缓存key的值；
>3 当客户端缓存的key因过期策略或内存淘汰策略被驱逐时，服务端也会发送失效消息给开启了tracking的客户端：

- 2 广播模式

    - hello 3 开启RESP3
    - client tracking on bcast  开启广播模式的客户端缓存tracking，默认会收到所有的key的失效信息
    - client tracking on bcast prefix abc 开启广播模式的客户端缓存tracking，只接受指定前缀'abc'的key的失效信息
    - set abc_1 1  修改了abc_1 的值之后 客户端会收到失效的通知
    
>总结: 这个模式下，客户端不用缓存key（get key） 也可以接受到对应的失效消息。
>也就是说：广播模式下，只要符合客户端设置的key前缀的key发生新增、修改、删除、过期、淘汰等动作，即使该key没有被该客户端缓存，也会收到key的失效消息；
>另外：如果要开启不同配置的客户端缓存，需要把先前的tracking关闭 ，client tracking off    

- 3 重定向模式：为了兼容RESP2协议，在Redis6中客户端缓存可以以重定向(Redirect)的方式实现，不再使用 RESP3 原生支持的PUSH消息，而是将消息通过 Pub/Sub 通知给另外一个客户端连接

    - client id  
      :7
    - client tracking on bcast redirect 5    客户端Id：7 将失效消息转发到5号客户端（客户端开启Tracking客户端缓存 并指定需要接收失效消息的客户端ID）
      +OK
    
    - client id  
      :5
    - subscribe _redis_:invalidate  5号客户端订阅了用于接收失效消息的客户端订阅的频道

- NOLOOP选项
    - 我们的客户端修改自己已缓存的key的时候也会收到这个key的过期信息，事实上这个客户端是不需要收到该消息的，这造成了浪费，因此我们可以使用NOLOOP选项将该客户端设置为：本客户端修改的key不会收到相关的失效信息。
    
    - client tracking on noloop 开启客户端缓存的NOLOOP选项
- OPTIN 和 OPTOUT
    - 在默认模式或重定向模式下，我们可以有选择的对需要的key进行缓存，而由于广播模式是匹配key前缀，因此不能使用此命令。
    - OPTIN : 只有执行client caching yes之后的第一个key才会被缓存；
    - OPTOUT : 与OPTIN相反，执行client caching no之后的第一个只读key不会被缓存；
     
    - client tracking on optin 开启客户端缓存optin选项
    - client caching yes 此命令后面第一个只读key会被缓存
    
    - client tracking optout  开启客户端缓存optout选项
    - client caching no 此命令后第一个只读key不会被缓存

    > 注意：在redis6.0.3版本中outin和optout选项时灵时不灵，可能还有BUG；
    > 我自己测试时发现，无论修改那个key client caching yes命令都会进行失效通知，**这个命令最好不用！！！**
                                                                                                                                                                                             >
                                                                                                                                                                                             >
- 失效表key上限
    - 可以使用 tracking_table_max_keys参数修改服务端失效表内记录的缓存的key的数量，当失效表内记录的缓存key达到配置的数量时会随机从失效表内移除缓存：
    - config get tracking-table-max-keys 查询最大缓存的数量  
      1# "tracking-table-max-keys" => "1000000"  默认是100w
    - config set tracking-table-max-keys 300   设置最大缓存数量为300
      
      
## RESP2 与 RESP3的对比

- RESP 全称 RedisSerializationProtocol，是 Redis 服务端与客户端之间通信的协议。
在Reds6之前的版本，使用的是RESP2协议，数据都是以字符串数组的形式返回给客户端，不管是 list 还是 sorted set。
因此客户端需要自行去根据类型进行解析，这样会增加了客户端实现的复杂性。
为了照顾老用户，Redis6在兼容 RESP2 的基础上，开始支持 RESP3，但未来会全面切换到RESP3之上。
今天的客户端缓存在基于RESP3才能有更好的实现，可以在同一个连接中运行数据的查询和接收失效消息。
而目前在RESP2上实现的客户端缓存，需要两个客户端连接以转发重定向的形式实现。
- 例子：
127.0.0.1:6379> hmset hash a 1 b 2 c 3 
OK

127.0.0.1:6379> hello 3
1# "server" => "redis"
2# "version" => "6.0.9"
3# "proto" => (integer) 3
4# "id" => (integer) 6
5# "mode" => "standalone"
6# "role" => "master"
7# "modules" => (empty array)

127.0.0.1:6379> hgetall hash
1# "a" => "1"
2# "b" => "2"
3# "c" => "3"

127.0.0.1:6379> hello 2
 1) "server"
 2) "redis"
 3) "version"
 4) "6.0.9"
 5) "proto"
 6) (integer) 2
 7) "id"
 8) (integer) 6
 9) "mode"
10) "standalone"
11) "role"
12) "master"
13) "modules"
14) (empty array)

127.0.0.1:6379> hgetall hash
1) "a"
2) "1"
3) "b"
4) "2"
5) "c"
6) "3"


# 三 