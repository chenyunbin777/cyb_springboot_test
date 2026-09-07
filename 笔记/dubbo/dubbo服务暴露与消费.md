# *****调用链路*****：可以直接通过调用链路来宏观获取dubbo的一些特点
 1 Directory：接口通过com.alibaba.dubbo.rpc.cluster.Directory.list(org.apache.dubbo.rpc.Invocation)方法调用获取可以providers地址，
 也就是对应的List<Invoker<T>>的一个list，还有一个的实现类是RegistryDirectory，他和接口名是一对一的关系，主要负责订阅和拉取服务提供者，动态配置和路由。
 2 负载均衡：随机负载 最少可用负载 一致性hash负载 轮询负载
 3 路由机制：
    - 条件路由：condition://127.0.0.1/com.foo.BarService/category=routers&dynamic=false&rule=
    condition:// 表示路由规则的类型，支持条件路由规则和脚本路由规则，可扩展，必填。
    0.0.0.0 表示对所有 IP 地址生效，如果只想对某个 IP 的生效，请填入具体 IP，必填。
    com.foo.BarService 表示只对指定服务生效，必填。
    category=routers 表示该数据为动态配置类型，必填。
    dynamic=false 表示该数据为持久数据，当注册方退出时，数据依然保存在注册中心，必填。
    - 1 排除预发布机：可以反问host不等于下面机器的
    => host != 172.22.3.91
    - 2 白名单 ：  host不等于下面机器的都不能访问（机器10.20.153.10,10.20.153.11都是可以访问的）
    host != 10.20.153.10,10.20.153.11 =>
    - 3 黑名单：：  host等于下面机器的都不能访问（机器10.20.153.10,10.20.153.11是这两个不可以访问）
    host = 10.20.153.10,10.20.153.11 =>
    - 4 为重要应用提供额外的机器：
    application != kylin => host != 172.22.3.95,172.22.3.96
    - 5 读写分离： 查询方法可以访问如下机器
    method = find*,list*,get*,is* => host = 172.22.3.94,172.22.3.95,172.22.3.96
    其他不是查询的可以访问下面的机器
    method != find*,list*,get*,is* => host = 172.22.3.97,172.22.3.98
    
 4 cluster容错机制
    - failover：如果当前机器访问失败，可以使用retries = 3配置重试的次数，重试其他机器
        - 
    - failfast：快速失败，返回异常
    - failsafe：快速失败，没有返回直接忽略
    - failback：如果调用失败，会定时重试，使用定时线程池，会将调用失败的请求保存到一个ConcurrentHashMap中。
    - available：直接找到一个可以用的Invoker直接调用
    - Broadcast：广播给所有的Invoker来调用，只要一个失败就全部失败
    - Forking：同时并行请求多个服务，有任何一个返回，直接返回。
 5 过滤器：注意他对于性能的影响。
    - AccessLogFilter：打印每一次请求的日志，服务提供端
    - ActiveLimitFilter：限制消费端对服务端的最大并行调用数，消费端
    - GenericFilter [dʒəˈnerɪk] ：服务端的，实现泛化调用，实现序列化检查和处理，服务提供端
    - GenericImplFilter：消费端，同上
    - TimeoutFilter：服务调用超时，记录告警日志，服务提供端
    - ExecuteLimitFilter：限制服务端最大并行调用数，服务提供端
        - executes = "10" 这个配置就是并发执行数，分为接口级别和方法级别
        <dubbo:service interface=""com.foo.BarService" execute = "10" /> 接口层
        <dubbo:service interface=""com.foo.BarService">
        //方法层
            <dubbo:method name= "sayHello" execute = "10" />
        </dubbo:service>
    - TpsLimitFilter：用于服务端的限流，服务提供端
 
 6 SPI的扩展机制
    - 与java SPI的不同之处
        - java SPI
            - 会一次性实例化扩展点的所有实现（没用上也会加载，浪费资源），
            - java SPI加载失败会吞掉异常信息
        - dubbo SPI
            - 只是加载了配置文件中的类，缓存在内存中，并不会立即初始化。
            - 加载失败，异常信息会打印出来
            - 实现了IOC和AOP机制。一个扩展点可以通过setter方法直接注入其他扩展方法；
            AOP的思想就是对于特定的通用的功能进行增强（前后增加特有的逻辑）
    - @SPI:设置一个默认的接口的实现类
    - @Adaptive：可以自己通过URL动态获取自定义的实现类，自适应注解
    - @Activate：
    - ExtensionLoader  [ɪkˈstenʃn] 是整个扩展机制的主要逻辑类
        - 1 @SPI实现：org.apache.dubbo.common.extension.ExtensionLoader.getExtension(java.lang.String)
            - getExtension(String name)
            -  [ɪkˈstenʃn] getExtension(String name, boolean wrap)如果name==true那么就加载并返回默认扩展类
            - createExtension开始创建扩展点实现类
                - getExtensionClasses()尝试从缓存中加载，没有则调用loadExtensionClasses()方法加载扩展类
                    - loadExtensionClasses()中cacheDefaultExtensionName();会检查是否有@SPI注解，获取注解中的value并且缓存为默认实现名
                    - loadDirectory 方法加载配置文件
                        - classLoader.getResources(fileName)
                        - ClassLoader.getSystemResources(fileName);获取配置文件
                        - loadResource方法循环遍历urls，解析字符串，得到扩展实现类，并加入缓存
                - injectExtension(instance); 依赖注入扩展类A依赖了扩展类B，类似于Spring Ioc的方式，通过反射获取实例
            - 加载完扩展点之后，在通过反射获得所有的扩展实现类并缓存起来
        - 2 @Adaptive实现：org.apache.dubbo.common.extension.ExtensionLoader.getAdaptiveExtension
        - 3 @Activate实现：org.apache.dubbo.common.extension.ExtensionLoader.getActivateExtension(org.apache.dubbo.common.URL, java.lang.String)
        - 4 **核心方法**： **getExtension方法来获取扩展点的具体实现。**

 7 netty的nio的对于数据包（粘报拆包）的处理
 - dubbo 使用 Netty 还是挺简单的，消费者使用 NettyClient，
 提供者使用 NettyServer，Provider 启动的时候，会开启端口监听，
 使用我们平时启动 Netty 一样的方式。而 Client 在 Spring getBean 的时候，
 会创建 Client，当调用远程方法的时候，将数据通过 dubbo 协议编码发送到 NettyServer，
 然后 NettServer 收到数据后解码，并调用本地方法，并返回数据，完成一次完美的 RPC 调用。
 - dubbo编解码：ExchangeCodec处理编解码，以两个字节的short MAGIC = (short) 0xdabb 为分隔符处理粘报拆包。
 
 8 dubbo序列化
 Hession2实现序列化工作
 9 dubbo的设计模式
 - 模板模式：
 一个抽象类提供公共的业务方法和多个抽象方法，每个子类可以单独实现自己的独立业务
 - 装饰器模式：
 模板模式：一个抽象类：负责公共方法和抽象方法供给子类实现，实现抽象类的子类，可以实现自己的具体定义的抽象方法。


# 客户端调用流程
（路由和负载均衡由客户端实现）
- 1 通过服务调用Proxy来触发Invoker的invoker调用
- 2 我们通过Directory获取所有可以调用的一个Invoker的列表，我们通过用户配置的Router路由规则来筛选一遍Invoker
    - org.apache.dubbo.rpc.cluster.Directory
    - org.apache.dubbo.rpc.Invoker
- 3 我们通过负载均衡策略来找到一个我们要去调用的Invoker。在这个invoker调用之前又会经过一个filter（处理上下文 限流 计数）
-  我们根据cluster集群配置的 容错机制来处理（failover failsafe failfast）
- 4 数据传输：使用nettyClient，
    - 使用Codec接口来做一些私有化协议的构造。
    - 对数据包进行序列化，然后传输到服务提者端
-------------以上是客户端实现-------------
- 5 服务提供者对数据包进行粘报拆包处理，然后在对完整的数据包进行**反序列化**
- 6 然后根据服务端口，接口名字，版本号 分组 构造唯一的key，并且从对应的hashMap中取出对应的**Exporter**
并且通过线程池来进行业务调用
- 7 服务提供者的服务的调用都是通过dispatcher线程池派发器来创建具有线程派发能力的ChannelHandler

- 8 线程模型
    - 1 dubbo.protocol.dispatcher=all dubbo线程池的默认策略：是所有的IO事件交给Dubbo线程池处理
    - 2 MessageOnlyChannelHandler：dubbo业务线程池处理请求和响应时间，其他在IO线程池处理。
    - 3 ConnectionOrderedDispatcher：单独的线程池处理连接断开事件，其他交给dubbo线程池。
        - 如果引入额外的一个线程池的化，则不可避免的导致额外的线程切换 会消耗一定的资源。





# 暴露分类
- 1 本地暴露是暴露在JVM中,不需要网络通信.  **injvm协议。**
- 2 远程暴露是将ip,端口等信息暴露给远程客户端,调用时需要网络通信.


# 服务提供者的暴露
前3项是Spring生成Bean的一个过程
- 1 Spring IOC容器启动加载dubbo的配置的标签，或者扫描@Service注解
- 2 解析标签 Service的Bean标签 生成对应的Bean
- 3 ServiceBean继承了ApplicationListener接口，在Bean初始化之后Spring会发送ContextRefreshedEvent事件，
ServiceBean收到此事件后开始服务暴露
- 4 暴露行为是在ServiceConfig#doExportUrls中实现的
- 真实暴露逻辑 org.apache.dubbo.config.ServiceConfig.doExportUrlsFor1Protocol
# 真正的暴露-代码实现
- org.apache.dubbo.config.ServiceConfig.doExport
    - 中的org.apache.dubbo.config.ServiceConfig.doExportUrls如果配置了多个注册中心，在这里一次暴露
    - 真实暴露逻辑 org.apache.dubbo.config.ServiceConfig.doExportUrlsFor1Protocol
    - *1 通过反射获取配置对象放到map中用于后续构造URL参数（比如应用名）
    - 2 获取全局配置，在默认属性前加 default. 前缀
    - *3 处理本地服务暴露，同一个JVM内的服务
    - 4 追加服务调用信息上报到监控地址
    - *5 **创建 动态代理的Invoker对象**  cglib 和 jdk动态代理
    - *6 服务暴露后想注册中心注册服务信息
    - *7 处理没有注册中心的情况，直接暴露服务。
    
    - 总结：进行本地服务暴露还是远程服务暴露。 创建 动态代理的Invoker对象。
    服务暴露后想注册中心注册服务信息。处理没有注册中心的情况，直接暴露服务。
 

- 1 通过代理类Proxy组件调用具体的协议（Protocol），把服务端要暴露的接口封装成一个INvoker
- 2 然后在转换成一个Exporter，这时候框架会打开一个服务端口等并记录服务实例在内存中
- 3 通过registry把服务元数据（包括服务端暴露的IP和端口）也就是Exporter注册到注册中心。
    - Exportor中会封装一个服务调用的Invoker。
总结：
为真实对象创建代理Invoker（ServiceBean -> Invoker）；
使用相关的协议暴露服务（Invoker -> Exporter）
  
    
## 注册中心控制服务暴露
- 所有暴露的服务都保存在
    - private final ConcurrentMap<String, ExporterChangeableWrapper<?>> bounds = new ConcurrentHashMap<>();

- org.apache.dubbo.registry.integration.RegistryProtocol.export
- 1 创建NettyServer 监听端口和保存服务实例
    - 打开端口，把服务实例存储到Map中
    - final ExporterChangeableWrapper<T> exporter = doLocalExport(originInvoker, providerUrl);
- 2 创建注册中心对象实例,并于注册中心建立TCP连接
    - final Registry registry = getRegistry(originInvoker);
- 3 注册服务元数据到注册中心
    - register(registryUrl, registeredProviderUrl);
- 4 订阅configurators节点，监听服务动态属性变更事件
    - registry.subscribe(overrideSubscribeUrl, overrideSubscribeListener);
- 5 最后unexport() 进行Invoker的端口销毁 和 map中服务实例等资源。
    -
    
    
- 服务消费
- 入口 referenceBean.getObject()方法
- 1 如果实例被销毁了，报错throw new IllegalStateException("The invoker of ReferenceConfig(" + url + ") has already destroyed!");
- 2 如果代理对象org.apache.dubbo.config.ReferenceConfig.ref == null 我们进行初始化
- 3 synchronized 加锁public synchronized void init() 
    - org.apache.dubbo.config.ReferenceConfig.init
 
- 4 org.apache.dubbo.config.ReferenceConfig.createProxy 会创建多注册中心 转换成一个Invoker的代理对象

- 消费：org.apache.dubbo.registry.integration.RegistryProtocol.refer 中 触发拉取数据，订阅和服务Invoker转换
，核心数据结构是RegistryDirectory
    -
    
    
consumer客户端经过路由算法，负载均衡，选出一台server进行RPC调用。
将请求交给底层IO netty线程池来处理，读写 序列化 反序列化。 也可以使用dubbo 的线程池来处理 通过 decode.in.io来配置

注册中心上指定保存的是一个Url，我们通过Url的解析 实现类 方法 参数，通过反射的方式获取实现类实例Invoker。

 