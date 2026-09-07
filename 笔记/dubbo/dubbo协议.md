#一 传输协议
https://blog.csdn.net/jiang_zf/article/details/85329932
一共7种
1、dubbo 协议 (默认)
缺省协议，使用基于mina1.1.7+hessian3.2.1的tbremoting交互。

连接个数：单连接
连接方式：长连接
传输协议：TCP
传输方式：NIO异步传输
序列化：Hessian 二进制序列化
适用范围：传入传出参数数据包较小（建议小于100K），消费者比提供者个数多，单一消费者无法压满提供者，**尽量不要用dubbo协议传输大文件或超大字符串。**
适用场景：常规远程服务方法调用
1、dubbo默认采用dubbo协议，dubbo协议采用单一长连接和NIO异步通讯，**适合于小数据量大并发的服务调用**，以及服务消费者机器数远大于服务提供者机器数的情况 
2、他不适合传送大数据量的服务，比如传文件，传视频等，除非请求量很低。


```

# dubbo服务提供方：使用dubbo.provider或者dubbo.protocol均可，dubbo.protocol优先级高
#设置dubbo的协议 暴露的端口号
#dubbo.protocol.name="dubbo"
dubbo.protocol.port=20890
#dubbo序列化方式
dubbo.protocol.serialization=hessian2
#dubbo业务线程池方式(默认fixed) 业务最大线程数量（默认200） 核心线程数量 队列数量 io线程数
dubbo.protocol.threadpool=fixed
dubbo.protocol.threads=200
dubbo.protocol.corethreads=10
dubbo.protocol.queues=0

# netty 的nio线程数，默认是cpu核数+1
dubbo.protocol.iothreads=9

配置如下：

<dubbo:protocol name="dubbo" port="20880" />
<!-- Set default protocol: -->
<dubbo:provider protocol="dubbo" />
<~-- Set service protocol -->
<dubbo:service protocol="dubbo" />
<!-- Multi port -->
<dubbo:protocol id="dubbo1" name="dubbo" port="20880" />
<dubbo:protocol id="dubbo2" name="dubbo" port="20881" />.
<!-- Dubbo protocol options: -->
<dubbo:protocol name="dubbo" port="9090" server="netty" client="netty" codec=“dubbo” 
serialization=“hessian2” charset=“UTF-8” threadpool=“fixed” threads=“100” queues=“0” iothreads=“9” 
buffer=“8192” accepts=“1000” payload=“8388608” />

```


2、rmi 协议
Java标准的远程调用协议。

连接个数：多连接
**连接方式：短连接**
传输协议：TCP
传输方式：同步传输
序列化：Java标准二进制序列化
适用范围：传入传出参数数据包大小混合，消费者与提供者个数差不多，可传文件。
适用场景：常规远程服务方法调用，与原生RMI服务互操作

