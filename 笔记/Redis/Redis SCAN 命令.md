# Redis SCAN 命令

## Redis Scan 命令用于迭代数据库中的**数据库键**。
## SCAN 命令是一个基于游标的迭代器，每次被调用之后， 都会向用户返回一个新的游标， 用户在下次迭代时需要使用这个新游标作为 SCAN 命令的游标参数， 以此来延续之前的迭代过程。
## SCAN 返回一个包含两个元素的数组， 第一个元素是用于进行下一次迭代的新游标， 而第二个元素则是一个数组， 这个数组中包含了所有被迭代的元素。如果新游标返回 0 表示迭代已结束。


- 由于KEYS命令一次性返回所有匹配的key，所以，当redis中的key非常多时，对于内存的消耗和redis服务器都是一个隐患
- 但SCAN 每次执行都只会返回少量元素，所以可以用于生产环境，而不会出现像 KEYS 或者 SMEMBERS 命令带来的可能会阻塞服务器的问题。

## 基础语法：
- redis Scan 命令基本语法如下：
    - SCAN cursor [MATCH pattern] [COUNT count]
    - cursor - 游标。
    - pattern - 匹配的模式。：模糊匹配Redis key值
    - count - 指定从数据集里返回多少元素，默认值为 10 。
        - **注意:COUNT选项并不能严格控制返回的key数量，只能说是一个大致的约束。** 并非每次迭代都要使用相同的 COUNT 值，用户可以在每次迭代中按自己的需要随意改变 COUNT 值， 只要记得将上次迭代返回的游标用到下次迭代里面就可以了。
        - 这个可以大致的控制返回多少的元素

#相关命令：
- SSCAN 命令用于迭代集合键中的元素。
- HSCAN 命令用于迭代哈希键中的键值对。
- ZSCAN 命令用于迭代有序集合中的元素（包括元素成员和元素分值）。

# 应用场景：
- 但由于KEYS命令一次性返回所有匹配的key，所以，当redis中的key非常多时，对于内存的消耗和redis服务器都是一个隐患，
- 对于Redis 2.8以上版本给我们提供了一个更好的遍历key的命令 SCAN 。SCAN 每次执行都只会返回少量元素，所以**可以用于生产环境**，而不会出现像 KEYS 或者 SMEMBERS 命令带来的可能会阻塞服务器的问题。

#例子：
- 以 0 作为游标开始一次新的迭代， 一直调用 SCAN 命令， 直到命令返回游标 0 ， 我们称这个过程为一次完整遍历。
    - scan 0 
    1) "0" //返回的下次需要迭代使用的游标，返回0说明所有的元素都已经迭代结束。
    2) 1) "nxtest"
       2) "runoobkey"
       3) "set"
       4) "cybSortedSet"
     
    - scan 0 match *t*
    1) "0"
    2) 1) "nxtest"
       2) "set"
       3) "cybSortedSet"
       
    - scan 0 match *t* count 1   //匹配 *t*  大致返回1个元素(不会严格控制返回的数量)
    - scan 5  count 1  这里 count 1 返回了4个key元素
    1) "3"
    2) 1) "c"
       2) "set"
       3) "runoobkey"
       4) "a"

    
