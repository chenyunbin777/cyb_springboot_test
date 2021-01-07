- 1 maxmemory <bytes> 可以设置内存最大使用量，达到根据内存回收策略进行回收

- 2 maxmemory-policy noeviction 设置key的过期策略
- LRU表示最近最少使用
- LFU表示使用频率最低

volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
volatile-lfu：从已设置过期时间的数据集（server.db[i].expires）中挑选使用频率最低
volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
allkeys-lru：从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
allkeys-lfu：从数据集（server.db[i].dict）中挑选使用频率最低
allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
no-enviction（驱逐）：禁止驱逐数据，不进行置换，表示即使内存达到上限也不进行置换，所有能引起内存增加的命令都会返回error


- 3 maxmemory-samples 5
    - Redis 的 LRU 是取出配置的数目的key，然后从中选择一个最近最不经常使用的 key 进行置换，默认的 5