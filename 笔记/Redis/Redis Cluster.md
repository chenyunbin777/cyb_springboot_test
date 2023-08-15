# Redis Slot概念
redis-trib

# 集群部署
- 0 Redis群集TCP端口
每个Redis群集节点都需要打开两个TCP连接。用于服务客户端的常规Redis TCP端口，例如6379，
再加上在数据端口上加上10000所获得的端口，在示例中为16379。
- 1 配置文件
```
port 7000
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
```
- 2 请注意，按预期工作的最小群集至少需要包含三个主节点。对于您的第一个测试，强烈建议启动一个由三个主节点和三个从节点组成的六个节点群集。
为此，输入一个新目录，并创建以下目录，该目录以我们将在任何给定目录中运行的实例的端口号命名。
```
mkdir cluster-test
cd cluster-test
mkdir 7000 7001 7002 7003 7004 7005     集群的总线端口用于集群节点的TCP请求
```
- 3 redis.conf在每个目录（从7000到7005）中创建一个文件。作为配置文件的模板，只需使用上面的小示例，
但请确保7000根据目录名称用正确的端口号替换端口号。

- 4 像这样启动每个实例，每个选项卡一个：
```
cd 7000
../redis-server ./redis.conf
```

- 5 从每个实例的日志中可以看到，由于不nodes.conf存在文件，因此每个节点都会为其分配一个新的ID，称为唯一的Node ID。

[82462] 26 Nov 11:56:55.329 * No cluster configuration found, I'm 97a3a64667477371c4479320d683e4c8db5858b1

- 6 集群启动
- 1 redis-cli只需键入以下内容即可为Redis 5创建集群：
```
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 \
127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 \
--cluster-replicas 1
```
- 2 使用redis-trib.rb用于Redis的4或3型：
    ./redis-trib.rb create --replicas 1 127.0.0.1:7000 127.0.0.1:7001 \
    127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005
- 此处使用的命令是create，因为我们要创建一个新集群。
该选项--cluster-replicas 1意味着我们希望为每个创建的主机都提供一个从机。其他参数是我要用于创建新集群的实例的地址列表。
显然，满足我们要求的唯一设置是创建具有3个主设备和3个从设备的集群。

- 7 登录上述任何一个客户端
    - redis-cli -c -p 7000

## 多个键值需要落在一个slot上的问题如何破？
   - 当执行**多键值事务操作**时，Redis不仅要求这些键值需要落在同一个Redis实例上，还要求落在同一个slot上。如何实现？
   - 解决方法还是从分片技术的原理上找。
   - 为了实现将key分到相同槽位，**就需要相同的hash值**，即相同的key。但key相同是不现实的，因为每个key都有不同的用途。例如user:user1:ids保存用户的ID，user:user1:contacts保存联系人的具体内容，两个key不可能同名。
但仔细观察user:user1:ids和user:user1:contacts，两个key其实有相同的地方，即user1。能不能拿这一部分去计算hash呢？这就是 Hash Tag 。允许用key的部分子串来计算hash。
   - 下文是redis-cloud-cluster网站针对Hash Tag的一段说明(详细内容请参考：https://redislabs.com/kb/redis-cloud-cluster/)
 Keys with a hash tag: a key’s hash tag is any substring between ‘{‘ and ‘}’ in the key’s name. That means that when a key’s name includes the pattern ‘{…}’, the hash tag is used as input for the hashing function. For example, the following key names have the same hash tag and would therefore be mapped to the same slot: foo{bar}, {bar}baz & foo{bar}baz.
 Keys without a hash tag: when a key doesn’t contain the ‘{…}’ pattern, the entire key’s name is used for hashing.
           大致意思：当一个key包含 “{ }” 的时候，就不对整个key做hash，而仅对 “{}” 包括的子串计算hash。假设hash算法为sha1。对ufoo{bar}, {bar}baz，foo{bar}baz，这三个字符串计算hash值，等同于计算sha1(bar)。
        OK，搞清楚具体原委后，故障修复也就是小case了。
    - 解决方案:将剩余令牌个数键remainTokenKey和上次访问时间戳键oldTsKey，统一采用如下前缀进行命名：“{/AppId}/”
即：
[csharp] view plain copy
string remainTokenKey = “{/AppId}/AppIdTokenBucketAvailable”  
string oldTsKey = “{/AppId}/AppIdTokenBucketTs”  
 这样便可确保这两个key落在同一个slot内部。

## 延伸
   通过Hash Tag可以解决多键值落在一个slot上的问题，但也不要一味的使用Hash Tag，将本不需要放在一个slot上的键值对人为的加上Hash tag而导致分片在同一个slot上，这样会导致redis集群键值在各个slot上分配不均衡。
还是引用redis-cloud-cluster网站的原文加以说明吧：
You can use the ‘{…}’ pattern to direct related keys to the same hash slot, so that multi-key operations are supported on them. On the other hand, not using a hash tag in the key’s name results in a (statistically) even distribution of keys across the keyspace’s shards. If your application does not perform multi-key operations, you don’t need to construct key names with hash tags.


# 哨兵 Redis Sentinel
-1 所有的客户端都通过Sentinel程序获取Redis的Master服务。首先Sentinel是集群部署的，
Client可以链接任何一个Sentinel服务所获的结果都是一致的。
-2 所有的Sentinel服务都会对Redis的主从服务进行监控，当监控到Master服务无响应的时候，Sentinel内部进行仲裁，
从所有的 Slave选举出一个做为新的Master。并且把其他的slave作为新的Master的Slave。最后通知所有的客户端新的Master服务地址。
如果旧的Master服务地址重新启动，这个时候，它将被设置为Slave服务。

-3 Redis Sentinel 集群模式的 “仲裁会” 
集群中有5个sentinel，票数被设置为2，当2个sentinel认为一个master已经不可用了以后，将会触发failover，
但是，进行failover的那个sentinel必须先获得至少3个sentinel的授权（超过半数）才可以实行failover。

  