# 实现原理
##  dubbo核心组件
- 1 Service：业务层，开发者的业务代码
- 2 config：配置层，ServerConfig暴露服务配置 ReferenceConfig引用的服务 两个类。
- 3 Proxy代理：无论生产者还是消费者 dubbo都会生成代理类，代理层会自动做远程调用并返回结果，业务层无感知
- 4 register：注册层，负责dubbo框架的服务注册与发现，也就是注册中心 zk Redis等。
- 5 cluster：集群容错层，
    - 1 RPC调用失败的容错（failOver，fastFail）
    - 2 负责均衡
    - 3 路由策略：调用某个IP server某一个接口
- 6 监控层：负责统计调用次数与时间
- 7 protocol：RPC调用层，封装RPC调用具体的过程。暴露于引用服务的主功能入口。
负责Invoker的生命周期，**Invoker是dubbo的核心模型**，本地远程集群的调用都是他发起的。
- 8 exchange：信息交换层：建立Request  Response模型，封装请求响应模式，
如：把同步转换成异步请求。

- 9 transport：网络传输层。NIO框架，对netty，mina进行封装，实现自己的IO传输。
- 10 Serialize：序列化，Hession2为主，二进制字节流更小，传输更快。

# 负载均衡
- org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance抽象类定义通用的功能
    - 1 org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance.getWeight
        - 获取Invoker权重
    - 2 org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance.calculateWarmupWeight
        - 做机器预热，防止dubbo服务刚启动时造成某台server负载过高
    - 3 org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance.select
        - 1 invokers都是空直接返回null
        - 2 如果只有一个invoker直接返回
        - 3 调用org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance.doSelect
        执行子类的实现的负载均衡逻辑。 
- 随机（默认）：根据总权重生成一个随机offset偏移量，如 总权重10，随机在0~10生成一个值offset。
在根据offset去遍历减所有Invoker的权重weights，当offset < 0时就算则那个server进行invoke服务调用。
- 轮询：
- 最少活跃调用：每调用一次将Invoker的服务**计数原子+1**,保存到集合中
- 一致性hash

# 集群容错机制
## cluster容错机制
- 1 failover（dubbo默认容错）：出现失败会重试其他机器。retries = 3设置重试次数。
用在读或者幂等性操作。
- 2 failfast：快速失败，请求失败快速返回异常，不做重试。
    - 用在非幂等性接口
- 3 failsafe：出现异常直接忽略。不关心调用是否成功，并且不想因为异常影响外层调用
    - 一般用在无关紧要的服务，如日志。
- 4 failback：请求失败，记录在失败队列中。使用异步线程池来重试执行。
- 5 Forking：同时调用多个服务，只要其中一个返回，则立即返回结果。
    - 可配置forks = "最大并行调用数"。
    - 一般用在实时性较高的服务
- 6 Broadcast：广播调用所有可用服务，一个报错就报错。 不需要负载均衡
    - 通常是会用在服务状态更新后的广播。
- 7 Available：最简单的方式，遍历所有服务列表，找到一个可用的节点调用直接返回。会有节点返回异常。
## Mock伪造返回数据
- 1 mock = force：return+null：强制服务返回null，不会进行RPC调用
- 2 mock = fail：return+null：调用服务失败后返回null，会进行RPC调用。
- 3 mock = throw：直接跑RpcException，不会RPC调用
## Mergeable
- 会合并多个请求数据一起返回
- <dubbo:reference interface= "XXX"  group = "*" merger = "true">
    - merger = "true" 开启
    - group = "*" 指定哪些分组,*全部分组
- <dubbo:method name = "getXXX" merger = ".addAll" > 
    - 对getXXX方法结果进行合并，指定的合并方法是.addAll方法
    - 

# 扩展点
- ExtensionLoader  [ɪkˈstenʃn] 是整个扩展机制的主要逻辑类
    - 1 @SPI实现：org.apache.dubbo.common.extension.ExtensionLoader.getExtension(java.lang.String)
    - 2 @Adaptive实现：org.apache.dubbo.common.extension.ExtensionLoader.getAdaptiveExtension
    - 3 @Activate实现：org.apache.dubbo.common.extension.ExtensionLoader.getActivateExtension(org.apache.dubbo.common.URL, java.lang.String)
    - 4 **核心方法**： **getExtension方法来获取扩展点的具体实现。**
- 1 @SPI注解：一般注解在接口上指定接口的实现方法
    - 实现接口的默认实现，**优先级最低**
    - **getExtension方法来获取扩展点的具体实现。**
- 2 @Adaptive自适应注解：标记类，枚举类，接口，方法，dubbo一般是标记在方法上，可以通过动态参数获取实现类。
    - 注解在类上，直接作为默认实现类，不会动态生成。
        - 只能有一个实现类加注解，否则会抛异常More than 1 adaptive class found
    - @Adaptive({Constants.SERVER_KEY, Constants.TRANSPORTER_KEY})
        - Constants.SERVER_KEY：server
        - Constants.TRANSPORTER_KEY：transporter
        - 这里匹配了server的实现方法 和 transporter的实现方法，然后依次的去获取实现类
```
@SPI("netty")
public interface Transporter {

    /**
     * Bind a server.
     *
     * @param url     server url
     * @param handler
     * @return server
     * @throws RemotingException
     * @see org.apache.dubbo.remoting.Transporters#bind(URL, ChannelHandler...)
     */
    @Adaptive({Constants.SERVER_KEY, Constants.TRANSPORTER_KEY})
    RemotingServer bind( ChannelHandler handler) throws RemotingException;

    /**
     * Connect to a server.
     *
     * @param url     server url
     * @param handler
     * @return client
     * @throws RemotingException
     * @see org.apache.dubbo.remoting.Transporters#connect(URL, ChannelHandler...)
     */
    @Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
    Client connect(URL url, ChannelHandler handler) throws RemotingException;

}
```

- 3 @Activate 自动激活注解：主要使用在有多个扩展点实现，根据不同的条件被激活的场景中



# 高级特性
- 1 group version
- 2 参数回调：消费方调用服务方时，支持server能够异步回调到当前的client
    - 主要用于stub做热数据缓存
- 隐式参数：消费方通过RpcContext.setAttachmant设置隐式参数，服务方RpcContext.getAttachmant获取
    - 应用：服务运行期动态改变属性值，动态路由，灰度发布场景
- 3 异步调用： dubbo:reference async = "true" 异步消费服务方
- 4 泛化调用：直接创建远程代理进行服务调用
- 5 上下文信息：存在ThreadLocal中
- 6 telnet：登录进行简单的运维操作
# 内部使用的设计模式
- 1 模板模式：抽象类定义通用的实现method，提供抽象方法给子类来具体实现。
- 2 工厂模式：所有的注册中心都是通过工厂创建的 
    - interface RegisterFactory
    - AbstractRegisterFactory
    - 实现类：RedisRegisterFactory，ZookeeperRegisterFactory
#  