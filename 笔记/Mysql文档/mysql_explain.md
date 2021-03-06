- explain参数：
    - select_type
        - SIMPLE(简单SELECT，不使用UNION或子查询等)
    - type
        - ALL、index、range、 ref、eq_ref、const、system、NULL（从左到右，性能从差到好）
        - ALL：Full Table Scan， MySQL将遍历全表以找到匹配的行
        - index: Full Index Scan，index与ALL区别为
            - index类型只遍历索引树
        - **range:只检索给定范围的行，使用一个索引来选择行**
        - ref: 表示上述表的连接匹配条件，即哪些列或常量被用于查找索引列上的值
        - eq_ref: 类似ref，区别就在使用的索引是唯一索引，对于每个索引键值，表中只有一条记录匹配，简单来说，就是多表连接中使用primary key或者 unique key作为关联条件
        - const、system: 当MySQL对查询某部分进行优化，并转换为一个常量时，使用这些类型访问。如将主键置于where列表中，MySQL就能将该查询转换为一个常量，
            - system是const类型的特例，当查询的表只有一行的情况下，使用system
        - NULL: MySQL在优化过程中分解语句，执行时甚至不用访问表或索引，例如从一个索引列里选取最小值可以通过单独索引查找完成。
           
    - rows:扫描行数特别多，怎么进行优化？
    - 如果发现查询需要大量的数据但值返回少数行，那么通常可以尝试下面的技巧去优化它：
        - https://www.cnblogs.com/beiluowuzheng/p/10119834.html
        - 1 使用索引覆盖扫描，把所有需要用的列都放到索引中，这样存储引擎无需回表获取对应的行就可以返回结果了。
        - 2 **改变库表结构。例如使用单独的汇总表。**
        - 3 重写这个查询，让MySQL优化器能够以更优化的方式执行这个查询。