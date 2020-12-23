# Redis 在线测试
https://try.redis.io/  
# Redis 命令参考
http://doc.redisfans.com/
# 测试环境 redis版本
redis-server --version
**Redis server v=6.0.9** sha=00000000:0 malloc=libc bits=64 build=85c36b8d70a68649

# Redis有哪些数据结构？
## 基础结构
- 字符串String(最常用的数据结构)
    - SETEX key seconds value **（应用于分布式锁的实现）**
      将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。
    - SETNX key value
      只有在 key 不存在时设置 key 的值。
    - 设置key的过期时间
        - EXPIRE key seconds：为给定 key 设置过期时间，**以秒计**。
        - PEXPIRE key milliseconds：设置 key 的过期时间**以毫秒计**。
    
        - EXPIREAT key timestamp：EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)，**单位是：秒**。
        - PEXPIREAT key milliseconds-timestamp：设置 key 过期时间的时间戳(unix timestamp) 以毫秒计
    - 返回key的剩余过期时间
        - TTL key：以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
            - 用法：当 key 不存在时，返回 -2 。 当 key 存在但**没有设置**剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
        - PTTL key：以毫秒为单位返回 key 的剩余的过期时间。
    - PERSIST key：移除 key 的过期时间，key 将持久保持。
    - TYPE key：返回 key 所储存的值的类型。
    
    
- 字典Hash
    - Redis hash 是一个 string 类型的 field（字段） 和 value（值） 的映射表，hash **特别适合用于存储对象**。
    - Redis 中每个 hash 可以存储 232 - 1 键值对（40多亿）。
    
    - HMSET key field1 value1 [field2 value2 ] ：同时将多个 field-value (域-值)对设置到哈希表 key 中。
    - HSET key field value：将哈希表 key 中的字段 field 的值设为 value 。 
    - HGETALL key：获取在哈希表中指定 key 的所有字段和值
    - HGET key field  ：获取hash表key中 字段field的value值 **（field相当于HashMap的key）**
    - HKEYS key：获取所有哈希表中的字段 **（field相当于HashMap的keySet()方法）**
    - HVALS key：获取哈希表中所有值。**（field相当于HashMap的values()方法）**
    - HLEN key：获取哈希表中字段的数量
    - 例子
    ``` 
        - HMSET runoobkey name "redis tutorial" description "redis basic commands for caching" likes 20 visitors 23000
        - hkeys runoobkey
          1) "name"
          2) "description"
          3) "likes"
          4) "visitors"
        - HVALS runoobkey
          1) "redis tutorial"
          2) "redis basic commands for caching"
          3) "20"
          4) "23000"
    ``` 

- 列表List（阻塞队列，优先级队列）
    - 1 LPUSH key value1 [value2] ：将一个或多个值插入到列表头部
    - 2 BRPOP key1 [key2 ] timeout：移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
    - **1 2 组合可以构成优先级队列，FIFO。**
    - LINDEX key index：通过索引获取列表中的元素
    - LLEN key 获取列表长度

- 集合Set：
    - Redis 的 Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。
    - Redis 中集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
    - 集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。
    - 1	SADD key member1 [member2] 向集合添加一个或多个成员
    - 2	SCARD key  获取集合的成员数
   

- 有序集合SortedSet(重点，排行榜)
    - Redis 有序集合和集合一样也是 string 类型元素的集合,且不允许重复的成员。
    - 不同的是每个元素都会关联一个 double 类型的分数。**redis 正是通过分数来为集合中的成员进行从小到大的排序。**
    - 有序集合的成员是唯一的,但分数(score)却可以重复。
    - 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
    
    - ZADD key score1 member1 [score2 member2] ：向有序集合添加一个或多个成员，或者更新已存在成员的分数
    - ZRANGE key start stop [WITHSCORES]：通过索引区间返回有序集合指定区间内的成员
    - ZREVRANGE key start stop [WITHSCORES]： 返回有序集中指定区间内的成员，通过索引，分数从高到低（实现排行榜）
    - ZREVRANGEBYSCORE key max min [WITHSCORES] 返回有序集中指定分数区间内的成员，分数从高到低排序 
    - ZREVRANK key member 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序（获取排行榜中的排名,排名从0开始）
    - ZUNIONSTORE destination numkeys key [key ...]
    - ZSCAN key cursor [MATCH pattern] [COUNT count]
    - 练习：
    ``` 
        - zadd cybSortedSet 100 cyb
        - zadd cybSortedSet 200 cyb2
        - zrange cybSortedSet 0 -1  //**-1代表最后一位，-2代表倒数第二个，一次类推**
            1) "cyb"
            2) "cyb2"
        - zrevrange cybSortedSet 0 -1
            1) "cyb2"
            2) "cyb"
        - zrevrange cybSortedSet 0 -1 withscores 
            1) "cyb2"
            2) "200"
            3) "cyb"
            4) "100"  
        - zrevrangebyscore  cybSortedSet 300 100
          1) "cyb2"
          2) "cyb"
        - ZREVRANK cybSortedSet cyb
          (integer) 1
          ZREVRANK cybSortedSet cyb2
          (integer) 0
    ``` 

## 如果你是Redis中高级用户，
还需要加上下面几种数据结构
- HyperLogLog：是用来做基数统计的算法，比如key中存储了 3个a，2个b，4个c，最后计算的结果就是3.
    - 什么是基数?
        比如数据集 {1, 3, 5, 7, 5, 7, 8}， 那么这个数据集的基数集为 {1, 3, 5 ,7, 8}, **基数(不重复元素)为5**。 
        基数估计就是在误差可接受的范围内，快速计算基数。
    - HyperLogLog 的优点是:
        - 在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定 的、并且是很小的。
        - Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定 的、并且是很小的。
        - 在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基 数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。
    - HyperLogLog 的缺点是:
        - 但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。
    - 命令：
        - pfadd key value  添加基数
        - pfcount key  计算基数
- Pub/Sub。
    - Redis 发布订阅 (pub/sub) 是一种**消息通信模式**：发送者 (pub) 发送消息，订阅者 (sub) 接收消息。
    - Redis 客户端可以订阅任意数量的频道。
    - 命令：
        - 订阅频道：subscribe 频道名字1 频道名字2 例如：subscribe cybChannel
        - 向频道发生消息：publish 频道名字 消息内容，例如：publish cybChannel 'hello world'
    
- Geo
    - Redis GEO 主要用于存储地理位置信息，并对存储的信息进行操作，**该功能在 Redis 3.2 版本新增。**
    - geoadd：添加地理位置的坐标。      
    - geopos：获取地理位置的坐标。
    - geodist：计算两个位置之间的距离。
    - georadius：根据用户给定的经纬度坐标来获取指定范围内的地理位置集合。
    - georadiusbymember：根据储存在位置集合里面的某个地点获取指定范围内的地理位置集合。
    - geohash：返回一个或多个位置对象的 geohash 值。
    - 命令：GEOADD key longitude latitude member [longitude latitude member ...]
       - 例子：
       ``` 
        - GEOADD position 116.20 39.56 beijing 120.2 30.3 hangzhou  添加北京，杭州经纬度
        - GEOPOS position beijing hangzhou  北京杭州经纬度
          1) 1) "116.19999736547470093"
             2) "39.56000019952067248"
          2) 1) "120.20000249147415161"
             2) "30.29999970751173777"
        - GEODIST position beijing hangzhou km   北京杭州之间的距离(单位：km，这个是可选的)
            "1092.3022"
        - GEORADIUS position 120 35 1000 km  在某经纬度，半径在1000km范围内有哪些地点
            1) "beijing"
            2) "hangzhou"
        - GEOHASH position beijing hangzhou  求某些地理位置的hash值
        - GEORADIUSBYMEMBER position beijing 1000 km / 2000 km  以北京为中心点 半径1000km/2000km范围内都有哪些城市
            "beijing"                                 1) "hangzhou"
        ```                                               2) "beijing"
- Redis Stream：Redis Stream 是 Redis 5.0 版本新增加的数据结构。
    - Redis Stream 主要用于消息队列（MQ，Message Queue），Redis 本身是有一个 Redis 发布订阅 (pub/sub) 来实现消息队列的功能，但它有个缺点就是消息无法持久化，如果出现网络断开、Redis 宕机等，消息就会被丢弃。
    - Redis Stream 提供了消息的持久化和主备复制功能，可以让任何客户端访问任何时刻的数据，并且能记住每一个客户端的访问位置，还能保证消息不丢失。
    - Redis Stream 的结构如下所示，它有一个消息链表，将所有加入的消息都串起来，每个消息都有一个唯一的 ID 和对应的内容：
![Image](https://raw.githubusercontent.com/chenyunbin777/cyb_springboot_test/master/Redis/%E5%9B%BE%E7%89%87/Redis-Stream.png)

    - 命令
        - 1 XADD key ID field value [field value ...] 使用 XADD 向队列添加消息，如果指定的队列不存在，则创建一个队列。
            - key ：队列名称，如果不存在就创建
            - ID ：消息 id，我们使用 * 表示由 redis 生成，可以自定义，但是要自己保证递增性。
            - field value ： 记录。
        - 2 XRANGE key start end [COUNT count]  使用 XRANGE 获取消息列表 ID从小到大，会自动过滤已经删除的消息
            - key ：队列名
            - start ：开始值， - 表示最小值
            - end ：结束值， + 表示最大值
            - count ：数量
        - 3 XREVRANGE key end start [COUNT count] 反向获取消息列表，ID 从大到小
        ``` 
        - 127.0.0.1:6379> xadd mystream * name cyb age 18
        "1608709051164-0"
        - 127.0.0.1:6379> xlen mystream
        (integer) 1
        - 127.0.0.1:6379> xrange mystream - + 获取消息列表，会自动过滤已经删除的消息
        1) 1) "1608709051164-0"
           2) 1) "name"
              2) "cyb"
              3) "age"
              4) "18"
        ``` 
              
        - 4 消费组消费
            - 群组消费的主要目的也就是为了分流消息给不同的客户端处理，以更高效的速率处理消息。为达到这一肝功能需求，我们需要做三件事：创建群组，群组读取消息，向服务端确认消息以处理。
            - XREADGROUP GROUP group consumer [COUNT count] [BLOCK milliseconds] [NOACK] STREAMS key [key ...] ID [ID ...]：使用 XREADGROUP GROUP 读取消费组中的消息，语法格式：
                - group ：消费组名
                - consumer ：消费者名
                - count ：**用来指定读取的最大数量**
                - block milliseconds ： 阻塞队列 阻塞毫秒数，如果是0代表一直阻塞
                - key ： 队列名
                - ID ： 消息ID，ID也有个特殊值>，表示还未进行消费的消息
              
            - XGROUP CREATE mystream consumer-group-name 0-0  ：id从小到大消费
            - XGROUP CREATE mystream consumer-group-name $：代表id从大到小消费，也就是从尾部开始消费
            
            - XREADGROUP GROUP consumer-group-name cyb count 1 STREAMS mystream > ：消费群组consumer-group-name中消费者cyb一次一条的消费队列mystream
            - 消费者建立阻塞监听 来消费消息；**如果有多个消费者，那么多个消费者轮询消费，一人消费一次**。
                - XREADGROUP GROUP consumer-group-name cyb block 0 STREAMS mystream >
            - XACK key group ID [ID ...]0 :消息消费后，为避免再次重复消费，这是需要向服务端发送 ACK，确保消息被消费后的标记。
                - 如 一下例子，1608709051164-0 1608711707969-0确认消费之后，在去消息组XREADGROUP中查询就不存在了，避免被重复消费
               ``` 
                -  127.0.0.1:6379> xack mystream consumer-group-name 1608709051164-0
                   (integer) 1
                -  127.0.0.1:6379> xack mystream consumer-group-name 1608711707969-0
                   (integer) 1
                -  127.0.0.1:6379> XREADGROUP GROUP consumer-group-name cyb STREAMS mystream 0
                   
                   1# "mystream" => 1) 1) "1608712864812-0"
                         2) 1) "name"
                            2) "cyb"
                      2) 1) "1608712882219-0"
                         2) 1) "name"
                            2) "cyb"
                      3) 1) "1608713285746-0"
                         2) 1) "name"
                            2) "cyb"
              ```
            
        - 单独消费 
            - XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...]
                - count ：**用来指定读取的最大数量。**
                - block milliseconds ： 阻塞队列 阻塞毫秒数，如果是0代表一直阻塞。
                - key ： 消费队列名。
                - ID ： ID 表示将要读取大于该 ID 的消息，当 ID 值使用 $ 赋予时，表示已存在消息的最大 Id 值。
            - 例子：
            ```
               1 直接消费
               127.0.0.1:6379> xread count 1 streams mystream 0
               1# "mystream" => 1) 1) "1608709051164-0"
                     2) 1) "name"
                        2) "cyb"
                        3) "age"
                        4) "18" 
          
                2 阻塞队列消费，只会消费新消息，ID从大到小消费
                127.0.0.1:6379> xread block 0 streams mystream $
                1# "mystream" => 1) 1) "1608715869472-0"
                      2) 1) "name"
                         2) "cyb2"
                (12.77s) 阻塞的时间
          
                127.0.0.1:6379> xread block 0 streams mystream $
                1# "mystream" => 1) 1) "1608715899411-0"
                      2) 1) "name"
                         2) "cyb3"
                (4.10s) 
            ``` 
                            
# 如果你说还玩过Redis Module，像BloomFilter，RedisSearch，Redis-ML

## Redis Module
- redis在4.0版本中，推出了一个非常吸引的特性，可以通过编写插件的模式，来动态扩展redis的能力。
    在4.0之前，如果用户想拥有一个带TTL的INCRBY 命令，那么用户只能自己去改代码，重新编译了。在4.0版本推出之后，想实现一个自定义的命令就简单的多了。
   
- 任何C/C++程序现在都可以运行在Redis上
- Modules是用一种本地的方式来扩展Redis的新用例和功能
- 使用现有的或者添加新的数据结构
- 享受简单，无限可扩展性和高可用性的同时保持着redis的本机的速度
- 可以由任何人创建
 
## BloomFilter
- 布隆过滤器：通过多个hash函数来计算一个value的hash值，将这多个hash值所在**bit数组 bitArr**中的位置设置为true或者1，
   下次再进行查找bitArr中是否存在这个value的时候就可以通过这种方式
- 优点：判断速度快，存储空间小（空间效率和查询效率高）
- 缺点：有一定的几率不准确，
    - 有可能把不属于这个集合的元素误认为属于这个集合（False Positive），
    - 但不会把属于这个集合的元素误认为不属于这个集合（False Negative）。
    - 在增加了错误率这个因素之后，Bloom Filter通过允许少量的错误来节省大量的存储空间。
    - value通过不同的hash函数算出的所有hash值，这些位置上有可能都已经为true或者1，因为不同的value，有可能会有相同的hash值。
- 核心：布隆过滤器（Bloom Filter）的核心实现是一个**超大的位数组和几个哈希函数**。假设位数组的长度为m，哈希函数的个数为k
- 实际应用场景：
    - 字处理软件中，需要检查一个英语单词是否拼写正确
    - 在 FBI，一个嫌疑人的名字是否已经在嫌疑名单上
    - 在网络爬虫里，一个网址是否被访问过
    - **yahoo, gmail等邮箱垃圾邮件过滤功能**
    - **以上这些场景有个共同的问题：如何查看一个东西是否在有大量数据的池子里面。** 考验检索某一数据的速度问题
        
   
## RedisSearch
- RediSearch是一个高性能的全文搜索引擎，可作为一个Redis Module 运行在Redis上，是由RedisLabs团队开发的。
- 项目地址：https://github.com/RedisLabsModules/RediSearch
            https://github.com/RediSearch/RediSearch
    安装参考：https://anoyi.com/p/458319b4e47e


- Redis-ML：machine learning 机器学习
- https://github.com/RedisLabsModules/redisml