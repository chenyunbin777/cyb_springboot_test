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
适用范围：传入传出参数数据包较小（建议小于100K），消费者比提供者个数多，单一消费者无法压满提供者，尽量不要用dubbo协议传输大文件或超大字符串。
适用场景：常规远程服务方法调用
1、dubbo默认采用dubbo协议，dubbo协议采用单一长连接和NIO异步通讯，适合于小数据量大并发的服务调用，以及服务消费者机器数远大于服务提供者机器数的情况 
2、他不适合传送大数据量的服务，比如传文件，传视频等，除非请求量很低。

2、rmi 协议
Java标准的远程调用协议。

连接个数：多连接
连接方式：短连接
传输协议：TCP
传输方式：同步传输
序列化：Java标准二进制序列化
适用范围：传入传出参数数据包大小混合，消费者与提供者个数差不多，可传文件。
适用场景：常规远程服务方法调用，与原生RMI服务互操作

