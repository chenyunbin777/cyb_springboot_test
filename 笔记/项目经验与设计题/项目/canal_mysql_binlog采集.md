# canal
- 管道，寓意数据的流转，通过监听mysql 的binlog增量日志来实现数据库的镜像，增量数据的消费，数据备份。
- 实现原理：将自己伪装成一个Mysql Slave，不断的向Master发送dump请求，mysql Master收到dump请求之后
就会向canal推送bin log。canal解析出对应的binlog之后就可以进行二次消费了。

- canal的主备切换：基于zookeeper来实现的主备切换。
    - 1 会有多个canal server，但是每次启动只能有一台server处于running状态，实现的原理就是：启动时，多个server都会向zk
    中去注册一个对应的**临时节点 :otter/canal/destinations/example/running**，注册成功的那个canal server就为running状态，其他的server是standby状态。
    并且同时会去running节点注册一个Watcher监听。
    - 2 当running节点失效或者宕机时，其他standby节点会收到对应的通知。
    - 3 之后会重复1步骤来完成主备切换。
- 主备切换的 "假死"问题的解决。
    - 防止因为网络问题而并非running server失效导致主备的切换。这时running节点正常运行的，而主备切换
    会导致一定的资源消耗。
    - 解决办法：主备切换的时候会有一定的延时时间，并不是standby server收到running server失效通知之后就立即
    执行抢占 running虚拟节点的操作。  默认的延时时间是5s。
    
- canal Client：为了避免数据的重复消费和消费的顺序，会实时将当前client 消费的点位实时记录在zookeeper上。 
    