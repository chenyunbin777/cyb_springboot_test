# Redis数据结构              
- 在Redis中，键值对（Key-Value Pair）存储方式是由字典（Dict）保存的，
而**字典底层是通过哈希表来实现的**。通过哈希表中的节点保存字典中的键值对。
类似Java中的HashMap，将Key通过哈希函数映射到哈希表节点位置。

- Redis Dict 中定义了两张哈希表，是为了后续字典的扩展作Rehash之用
```
/* 字典结构定义 */
typedef struct dict { 
    dictType *type;  // 字典类型
    void *privdata;  // 私有数据
    dictht ht[2];    // 哈希表[两个]
    long rehashidx;   // 记录rehash 进度的标志，值为-1表示rehash未进行
    int iterators;   //  当前正在迭代的迭代器数
} dict;
一个Dict对应2个Dictht，正常情况只用到ht[0]；ht[1] 在Rehash时使用。

/* hash表结构定义 */
typedef struct dictht { 
    dictEntry **table;   // 哈希表数组
    unsigned long size;  // 哈希表的大小
    unsigned long sizemask; // 哈希表大小掩码
    unsigned long used;  // 哈希表现有节点的数量
} dictht;

/* 哈希桶 */
typedef struct dictEntry { 
    void *key;     // 键定义
    // 值定义
    union { 
        void *val;    // 自定义类型
        uint64_t u64; // 无符号整形
        int64_t s64;  // 有符号整形
        double d;     // 浮点型
    } v;     
    struct dictEntry *next;  //指向下一个哈希表节点
也是链表的结构
} dictEntry;
```
## Redis rehash过程
- 将ht[0]中的数据转移到ht[1]中，在转移的过程中，需要对哈希表节点的数据重新进行哈希值计算
- 将ht[0]释放，然后将ht[1]设置成ht[0]，最后为ht[1]分配一个空白哈希表NULL

- 当Redis 节点中的Key总量到达临界点后（ht[0].used/t[0].size>5时，也就是每个链表平均存储的元素数>5 便对字典进行扩展），Redis就会触发Dict的扩展，进行Rehash。
申请扩展后相应的内存空间大小。但是我们的内存空间不足够达到申请的内存大小，怎么办？
    - 总元素 * 10 < 桶的个数，也就是,填充率必须<10%，进行收缩
    - 所以会导致：Redis Rehash是导致Redis Master和Slave大量触发驱逐淘汰的根本原因。
## **Redis满容驱逐状态下，如何避免因Rehash而导致Redis抖动的这种问题？**
- 我们在Redis Rehash源码实现的逻辑上，加上了一个判断条件，如果现有的剩余内存不够触发Rehash操作所需申请的内存大小，即不进行Resize操作
- 通过提前运营进行规避，比如容量预估时将Rehash占用的内存考虑在内，或者通过监控定时扩容

## 渐进式 rehash
- rehash 操作并不是一次性、集中式完成的，而是分多次、渐进式地完成的