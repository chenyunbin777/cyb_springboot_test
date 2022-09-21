# explain参数：
- select_type
    - SIMPLE(简单SELECT，不使用UNION或子查询等)
- type
    - ALL、index、range、 ref、eq_ref、const、system、NULL（从左到右，性能从差到好）
    - ALL：Full Table Scan， MySQL将遍历全表以找到匹配的行
    - index: Full Index Scan，index与ALL区别为
        - index类型只遍历索引树
    - **range:只检索给定范围的行，使用一个索引来选择行**
        - in、between、大于、小于等
    - ref: 表示上述表的连接匹配条件，即哪些列或常量被用于查找索引列上的值
    - eq_ref: 类似ref，区别就在使用的索引是唯一索引，对于每个索引键值，表中只有一条记录匹配，
    简单来说，就是**多表连接**中使用primary key或者 unique key作为关联条件，简单的select查询不会出现这种type
        - left join  right join等
    - const、system: 当MySQL对查询某部分进行优化，并转换为一个常量时，使用这些类型访问。如将主键置于where列表中，MySQL就能将该查询转换为一个常量，
        - system是const类型的特例，当查询的表只有一行的情况下，使用system
    - NULL: MySQL在优化过程中分解语句，执行时甚至不用访问表或索引，例如从一个索引列里选取最小值可以通过单独索引查找完成。

- possible_keys列
    - 此列显示在查询中可能用到的索引。如果该列为NULL，则表示没有相关索引，可以通过检查where子句看是否可以添加一个适当的索引来提高性能。

- key列
    - 此列显示MySQL在查询时实际用到的索引。在执行计划中可能出现possible_keys列有值，而key列为null，这种情况可能是表中数据不多，MySQL认为索引对当前查询帮助不大而选择了全表查询。如果想强制MySQL使用或忽视possible_keys列中的索引，在查询时可使用force index、ignore index。

- Extra列：此列是一些额外信息。常见的重要值如下：
1）Using index：使用覆盖索引（如果select后面查询的字段都可以从这个索引的树中获取，不需要通过辅助索引树找到主键，再通过主键去主键索引树里获取其它字段值，这种情况一般可以说是用到了覆盖索引）。
2）Using where：使用 where 语句来处理结果，并且查询的列未被索引覆盖。
3）Using index condition：查询的列不完全被索引覆盖，where条件中是一个查询的范围。
    - 该优化支持的type类型有：range  ref  eq_ref  null
4）Using temporary：MySQL需要创建一张临时表来处理查询。出现这种情况一般是要进行优化的。
5）Using filesort：将使用外部排序而不是索引排序，数据较小时从内存排序，否则需要在磁盘完成排序。
6）Select tables optimized away：使用某些聚合函数（比如 max、min）来访问存在索引的某个字段时。
         
- rows:扫描行数特别多，怎么进行优化？
- 如果发现查询需要大量的数据但值返回少数行，那么通常可以尝试下面的技巧去优化它：
        - https://www.cnblogs.com/beiluowuzheng/p/10119834.html
        - 1 使用索引覆盖扫描，把所有需要用的列都放到索引中，这样存储引擎无需回表获取对应的行就可以返回结果了。
        - 2 **改变库表结构。例如使用单独的汇总表。**
        - 3 重写这个查询，让MySQL优化器能够以更优化的方式执行这个查询。