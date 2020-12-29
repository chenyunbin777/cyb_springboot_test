# Redis Slot概念
在redis集群内部，采用slot槽位的逻辑管理方式， 集群内部共有16384(2的14次方)个Slot，集群内每个Redis Instance负责其中一部分的Slot的读写。一个Key到底属于哪个Slot，由分片算法：
crc16(key) % 16384决定。也正是通过此分片算法，将不同的key以相对均匀的方式分配到不同的slot上。

## 多个键值需要落在一个slot上的问题如何破？
   - 当执行多键值事务操作时，Redis不仅要求这些键值需要落在同一个Redis实例上，还要求落在同一个slot上。如何实现？
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