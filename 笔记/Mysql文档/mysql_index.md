雪花算法 UUID 生成唯一ID

# 重新统计索引信息
- explain统计索引使用信息，是一个**随机采样**的过程，并不一定是准确的。 可以使用 mysql analyze table 来重新统计索引信息



# 5.6之后的Multi-Range Read，多范围读取 MRR优化
- 主要解决的是当**二级索引（辅助索引）**取出索引值后再去聚集索引中取行可能会造成大量的**磁盘随机IO的问题**，下面我们来具体进行分析。

- 磁盘随机IO案例
  ``CREATE TABLE `t3`  (
    `id` int(11) NOT NULL,
    `a` int(11) NULL DEFAULT NULL,
    `b` int(11) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `a`(`a`) USING BTREE
  ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;``
  
    - 插入一批数据，id不断递增到1000，a从1000递减到1
    
- MRR优化:MRR优化的目的就是减少磁盘随机IO的产生，其查询优化过程大致如下：
    - 1、先把通过二级索引取出的值缓存在缓冲区中。
    - 2、再把这部分缓冲区中的数据按照ID进行排序。
    - 3、然后再依次根据ID去聚集索引中获取整个数据行。 
    - 这样就可以顺序的获取数据，就可以节省每次遍历聚蔟索引的双向链表的开销，**一次遍历**就可以获取到所有的数据。
    - 可以看出，只需要通过一次排序，就使得随机IO，变为顺序IO，使得数据访问更加高效。
    - read_rnd_buffer_size控制了数据能放入缓冲区的大小，如果一次性不够放就会分多次完成。
        - mysql> select @@read_rnd_buffer_size\G
          *************************** 1. row ***************************
          @@read_rnd_buffer_size: 262144
          1 row in set (0.00 sec)
        - MRR的开关：
            - mysql> select @@optimizer_switch\G
             *************************** 1. row ***************************
             @@optimizer_switch: index_merge=on,index_merge_union=on,index_merge_sort_union=on,index_merge_intersection=on,engine_condition_pushdown=on,index_condition_pushdown=on,mrr=on,mrr_cost_based=on,block_nested_loop=on,batched_key_access=off,materialization=on,semijoin=on,loosescan=on,firstmatch=on,duplicateweedout=on,subquery_materialization_cost_based=on,use_index_extensions=on,condition_fanout_filter=on,derived_merge=on,use_invisible_indexes=off,skip_scan=on,hash_join=on,subquery_to_derived=off,prefer_ordering_index=on,hypergraph_optimizer=off,derived_condition_pushdown=on
             1 row in set (0.00 sec)
        - mrr=on（开启MRR优化）,mrr_cost_based=on  
        - engine_condition_pushdown=on  开启索引下推
# 5.6之后的索引下推
- 根据explain解析结果可以看出Extra的值为Using index condition，表示已经使用了索引下推。
- （name，age） 联合索引
- SELECT * from user where  name like '陈%' and age=20 ，如果有多个姓陈的数据，在索引的内部还会根据age=20来再筛选一遍。

# show profiles;
- https://www.cnblogs.com/developer_chan/p/9231761.html 
- 是mysql提供的可以用来分析当前会话中sql语句执行的资源消耗情况的工具，可用于sql调优的测量。默认情况下处于关闭状态，并保存最近15次的运行结果。查看sql执行的时间
- show profile cpu,block io for query 1;
        - 可以查看cpu，io占用的时间
        - BLOCK IO：显示块IO开销。
        - CPU：显示CPU开销信息。
        - MEMORY：显示内存开销信息。

# b树与b+树的比较
- B树每个节点都保存的数据，b+树是叶子节点保存的数据，这样树的高度就会很低，查询效率就会很高，io次数较少。
- 页结构存储：非叶子节点保存的数据指针，叶子节点保存数据。 一页16k
- 总结：InnoDB中，表数据文件本身就是按B+Tree组织的一个索引结构

## 1 聚簇索引  
- （1）聚簇索引：就是按照每张表的主键构造一颗B+树，同时叶子节点中存放的就是整张表的行记录数据，也将聚集索引的叶子节点称为"数据页"。
这个特性决定了索引组织表中数据也是索引的一部分； 
- 底层的叶子节点是**有序的双向链表结构**，所以排序查找和范围查找速度很快
- （2）一般建表会用一个"自增主键"做聚簇索引，没有的话MySQL会默认创建选择一个唯一且非空的索引替代，如果没有这样的索引，InnoDB会隐式定义一个主键（类似oracle中的RowId）来作为聚簇索引。
    一个表仅有一个聚簇索引，一般都是主键索引。
- （3）**辅助索引**：也是一颗b+树，但是**叶子节点存放的是聚蔟（主键）索引的值**
我们日常工作中，根据实际情况自行添加的索引都是"辅助索引"（非主键索引），也称为二级索引，辅助索引就是一个为了需找主键索引的二级索引，现在找到"主键索引值"再通过主键索引找数据；
    辅助索引检索数据的过程：现在辅助索引中检索到对应的聚簇索引的值，再根据该主键值检索聚簇索引而找到对应的B+ Tree的叶子节点，也就找到了对应的行数据。
    例如：user表 id主键，name建立普通的索引
    select * from user where name = 'cyb'
    我们先通过辅助索引name来在辅助索引的B+tree中检索，找到对应的行的主键值，
    然后在跟进主键值去**聚蔟索引的B+Tree中检索找到对应的叶子节点，也就找到了对应的Mysql的数据行的值**。
- （4）支持行锁
- （5）select count(*) from table 会全部扫描，不会缓存

## 2 使用聚簇索引的优势：
每次使用辅助索引检索都要经过两次B+树查找，看上去聚簇索引的效率明显要低于非聚簇索引，这不是多此一举吗？聚簇索引的优势在哪？
- （1）由于行数据和聚簇索引的叶子节点存储在一起，同一页中会有多条行数据，访问同一数据页不同行记录时，已经把页加载到了Buffer中（缓存器），
再次访问时，会在内存中完成访问，不必访问磁盘。这样主键和行数据是一起被载入内存的，找到叶子节点就可以立刻将行数据返回了，
如果按照主键Id来组织数据，获得数据更快。

- （2）辅助索引的叶子节点，**存储主键值和对应的列数据**，而不是数据的存放地址。好处是当行数据放生变化时，索引树的节点也需要分裂变化；或者是我们需要查找的数据，
在上一次IO读写的缓存中没有，需要发生一次新的IO操作时，可以避免对辅助索引的维护工作，只需要维护聚簇索引树就好了。
另一个好处是，因为辅助索引存放的是主键值，减少了辅助索引占用的存储空间大小。

> 注：我们知道一次io读写，可以获取到16K大小的资源，我们称之为读取到的数据区域为Page。
而我们的B树，B+树的索引结构，叶子节点上存放好多个关键字（索引值）和对应的数据，
都会在一次IO操作中被读取到缓存中，所以在访问同一个页中的不同记录时，会在内存里操作，
而不用再次进行IO操作了。除非发生了页的分裂，即要查询的行数据不在上次IO操作的缓存里，才会触发新的IO操作。
（ 发生页分裂的话，b+tree会重组，需要重新IO来查询）

- （3）因为MyISAM的主索引并非聚簇索引，那么他的数据的物理地址必然是凌乱的，拿到这些物理地址，按照合适的算法进行I/O读取，
于是开始不停的寻道不停的旋转。聚簇索引则只需一次I/O。（强烈的对比）

- （4）不过，如果涉及到大数据量的排序、全表扫描、count之类的操作的话，还是MyISAM占优势些，因为索引所占空间小，这些操作是需要在内存中完成的。

## 3 innodb和myisam的区别

- （1）InnoDB支持事务，MyISAM不支持，这一点是非常之重要。事务是一种高级的处理方式，如在一些列增删改中只要哪个出错还可以回滚还原，而MyISAM就不可以了。
- （9）InnoDB支持行锁，而MyISAM支持表级锁
InnoDB的行锁是实现在索引上的，而不是锁在物理行记录上。潜台词是，如果访问没有命中索引，也无法使用行锁，将要退化为表锁。
命中索引：包括 没有查到数据，或者查询的字段没加索引
如：user表 id主键，name建立普通的索引，age 没有索引
    select * from user where name = 'cyb' 如果没有查到，表锁；如果查到了，行锁
    select * from user where age = 18 表锁，因为age没有加索引

- （2）MyISAM适合查询以及插入为主的应用，InnoDB适合频繁修改以及涉及到安全性较高的应用
- （3）InnoDB支持外键，MyISAM不支持
- （4）InnoDB select count(*) from table 会全部扫描，不会缓存；而MyISAM用一个变量保存了整个表的行数
- （5）InnoDB不支持FULLTEXT类型的索引
- （6）InnoDB中不保存表的行数，如select count() from table时，InnoDB需要扫描一遍整个表来计算有多少行，但是MyISAM只要简单的读出保存好的行数即可。注意的是，当count()语句包含where条件时MyISAM也需要扫描整个表
- （7）对于自增长的字段，InnoDB中必须包含只有该字段的索引，但是在MyISAM表中可以和其他字段一起建立联合索引
- （8）清空整个表时，InnoDB是一行一行的删除，效率非常慢。MyISAM则会重建表


# 二 索引优化
## 1 索引覆盖：
- 1 查询辅助索引的列
    - 如：KEY `d` (`d`),   
    mysql> explain select d from cyb_test where d = 3;
    +----+-------------+----------+------------+------+---------------+------+---------+-------+------+----------+-------------+
    | id | select_type | table    | partitions | type | possible_keys | key  | key_len | ref   | rows | filtered | Extra       |
    +----+-------------+----------+------------+------+---------------+------+---------+-------+------+----------+-------------+
    |  1 | SIMPLE      | cyb_test | NULL       | ref  | d             | d    | 5       | const |    1 |   100.00 | Using index |
    +----+-------------+----------+------------+------+---------------+------+---------+-------+------+----------+-------------+
    1 row in set, 1 warning (0.01 sec)
    
    - KEY `ab` (`a`,`b`)  ab的联合索引
        - explain select a from cyb_test where a = 3;  Extra 为Using index
        - 
     
- 2 统计操作
是指 如果查询的列恰好是索引的一部分，那么查询只需要在索引文件上进行，不需要回行到磁盘在找数据。
如果和整个表的数据差不多，mysql则认为全表扫描代价更小
是查询的数据多了之后，mysql认为还是索引查询更快，所以用了索引。
总结：mysql会自动选择比较快的查询方式来进行查询。

## 2 最左前缀原则
- 最左前缀：mysql 会一直向右匹配直到遇到范围查询（>、<、between 1 and 3、like）就停止匹配。范围列可以用到索引，但是范围列后面的列无法用到索引。
在 MySQL 中，可以指定多个列为索引，即联合索引。
比如 index(name，age) ，最左前缀原则是指查询时精确匹配到从最左边开始的一列或几列（name；name&age），就可以命中索引。
如果所有列都用到了，顺序不同，查询引擎会自动优化为匹配联合索引的顺序，这样是能够命中索引的。
总结：mysql会对联合索引进行优化

## 3 修复空间碎片
 [ˈɑːptɪmaɪz] 
optimize table 表名;

## 4 什么样的sql不走索引
- （1）如果条件中有or，即使其中有条件带索引也不会使用。
- （2）查询条件字段参与了运算：例如where id *1 , 使用 + - * /
- （3）使用函数：'2020-12-03' = DATE_FORMAT(date_create,'%Y-%m-%d')
- （4）字符串与数字比较不使用索引;
- like "%_" 百分号在最前面不走

## 5 索引的弊端
不要盲目的创建索引，只为查询操作频繁的列创建索引，创建索引会使查询操作变得更加快速，
但是会降低增加、删除、更新操作的速度，因为执行这些操作的同时会对索引文件进行重新排序或更新。
但是，在互联网应用中，查询的语句远远大于DML的语句，甚至可以占到80%~90%，所以也不要太在意，只是在大数据导入时，可以先删除索引，再批量插入数据，最后再添加索引。
- 当页中删除的记录达到MERGE_THRESHOLD（默认页体积的50%），InnoDB会开始寻找最靠近的页（前或后）看看是否可以将两个页合并以优化空间使用。
### 页分裂
- 但是如果插入的是不规则的数据，那么每次插入都会改变二叉树之前的数据状态。从而导致了页分裂。
## 6 limit 优化
https://www.jb51.net/article/31868.htm
在mysql中limit可以实现快速分页，但是如果数据到了几百万时我们的limit必须优化才能有效的合理的实现分页了，否则可能卡死你的服务器哦。PERCONA PERFORMANCE CONFERENCE 2009上，来自雅虎的几位工程师带来了一篇”EfficientPagination Using MySQL”的报告: 
limit10000,20的意思扫描满足条件的10020行，扔掉前面的10000行，返回最后的20行，问题就在这里。
###(1)日常分页SQL语句
select id,name,content from users order by id asc limit 100000,20
扫描100020行
###(2)如果记录了上次的最大ID
select id,name,content from users where id>100073 order by id asc limit 20
扫描20行。
###(3)如果id是连续的：
SELECT * FROM table WHERE id >= (SELECT id FROM table LIMIT 1000000, 1) LIMIT 10;
###(4)终极优化:如果需要查询 id 不是连续的一段，最佳的方法就是先找出 id ，然后用 in 查询
-  1 先找出id，通过建立search(vtype,id) 这样的索引
select id from collect where vtype=1 limit 90000,10; 非常快！0.04秒完成！
-  2 然后在 SELECT * FROM table WHERE id IN(10000, 100000, 1000000...);

## Index Condition Pushdown（ICP） 优化
- Mysql数据库会在取出索引的同时，判断是否可以进行where条件的过滤，也就是将Where条件的部分过滤操作放在了存储引擎层。
这样减少了上层SQl层对记录的索取，从而提高了数据库的整体性能.
- **explain 的Extra字段可以看到 Using index condition、**



# mysql：
可重复读隔离级别下：增删改查：其中哪些会用到什么锁？
1 查询（select）：读取不会占用和等待表上的锁，除非加select 。。。 for update（强制性加锁，间隙锁 （】）
2 更新（update set...）:
UPDATE accounts SET level = 100 WHERE id = 5;   如果是唯一索引，那么会加对应的“行锁”
UPDATE accounts SET level = 100 WHERE a = 5;   如果是辅助索引，那么记录锁不仅会加在这个二级索引上，还会加在这个二级索引所对应的聚簇索引上。 如 a = 5 的id有 1 2 3 4 这些记录都会加行锁。
3 插入（insert into）
delect insert update加的锁要看匹配的列是不是有索引决定是不是锁表，不一定是行锁

1 MVCC：多版本并发控制
下面两个采用的都是非锁定的一致性读操作，不会等待delete 或者 update锁释放，会读取一个快照数据。下面对于快照数据的定义也是不同的
Read Commited： 总是读取行的最新版本（提交之后的最新数据），所以违反了事务ACID中的，隔离性（事务之间的处理都是隔离的）
Repeatable read：总是读取事务开始时的行数据

Serialziable：快照读的前提是隔离级别不是串行级别，串行级别下的快照读会退化成当前读
- 之所以出现快照读的情况，是基于提高并发性能的考虑，快照读的实现是基于多版本并发控制，即MVCC,可以认为MVCC是行锁的一个变种，
但它在很多情况下，避免了加锁操作，降低了开销；既然是基于多版本，即快照读可能读到的并不一定是数据的最新版本，而有可能是之前的历史版本
- 说白了MVCC就是为了实现读-写冲突不加锁，而这个读指的就是快照读, 而非当前读，当前读实际上是一种加锁的操作，是**悲观锁**的实现 

- 总之在RC隔离级别下，是每个快照读都会生成并获取最新的Read View；
而在RR隔离级别下，则是同一个事务中的第一个快照读才会创建Read View, 之后的快照读获取的都是同一个Read View。
  
 

2 对于非锁定的一致性读操作，就算是加了独占锁select 。。。 for update，也是可以进行读取的。

3 间隙锁(左开，右开)，next-key-lock（锁定范围包括记录本身，左开右毕）必须在Repeatable Read级别下才生效：  id： 1  2  5  6   a: 1 1 3 6
3.1 唯一索引。主键索引：where id = 5，会锁定。id = 5 的 这条记录
3.2 副主索引：select * from table where b=3 for update 会根据next-key-lock锁定范围 (1,3]，特别注意的是，innodb引擎还会对辅助索引的下一个减值加上 gap lock，即辅助索引的范围(3,6)也会被锁定

4 LOCK_AUTO_INC：自增锁，一种特殊的表锁，自增值必须是索引，同事必须是索引的第一个列。 insert into 时会加表锁


最左前缀：mysql 会一直向右匹配直到遇到范围查询（>、<、between、like）就停止匹配。范围列可以用到索引，但是范围列后面的列无法用到索引。
- 当使用abc会完全使用联合索引的abc三列，使用ab只会使用联合索引中的两列，使用a或者ac只会使用联合索引中的a列，至于其他情况联合索引不会被使用


覆盖索引：
- 就是select的数据列只用从索引中就能够取得，不必从数据表中读取，换句话说查询列要被所使用的索引覆盖。
- 
d 建立了一个索引
explain select d from cyb_test where d like '%1'  可以使用到d索引

a = X and b>xx and c= XX
select a from table  where a like %XXX;



# B+数一层可以有多少数据
- 1 每页存储的数据量是16K mysql默认且可以修改
- 2 命令 ： show VARIABLES like 'innodb_page_size%';
mysql> show VARIABLES like 'innodb_page_size%';
+------------------+-------+
| Variable_name    | Value |
+------------------+-------+
| innodb_page_size | 16384 |
+------------------+-------+
1 row in set (0.02 sec)
16384/1024=16k
5.以bigInt 举例子 它是8个字节 一个指针是6个字节（不要问为什么，你就信了吧）bigInt是mysql 字段类型之一
6.如 图1-1 咱们来计算一下 第一页有多少指向第二层的指针（第一页和第二页只存了索引和指向下一级的指针）
7.**16384/（6+8）≈ 1170**
8.每一个指针都是一个页，那么第二层有多少页？答案是1170个页，那么每个页是不 也有1170个指针指向第三层，那么第二层有多少指针了？1170*1170 = 1368900
9.第三层是存数据的，那么他能存多少条数据呢？
10.每一个页咱们之前说了他能存16k大小的数据，而每一条数据大概是 1k 我们就按 1k 算，第二层有1368900个指针指向第三层，也就是说第三层有1368900个页，每一页可以存16条数据，那么第三层可以存16*1368900 =21902400
两千一百九十多万条数据。
注意：B+树只在最下存储数据

结论：高度为3的b+树可以存储2kw条数据


# 判断一个字段是否适合添加索引的方法
mysql> show index from test_fuzhu;
+------------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| Table      | Non_unique | Key_name | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible | Expression |
+------------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
| test_fuzhu |          0 | PRIMARY  |            1 | id          | A         |           7 |     NULL |   NULL |      | BTREE      |         |               | YES     | NULL       |
| test_fuzhu |          1 | b        |            1 | b           | A         |           4 |     NULL |   NULL | YES  | BTREE      |         |               | YES     | NULL       |
+------------+------------+----------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+------------+
2 rows in set (0.02 sec)

- 查看Cardinality（基数）的值，mysql通过**随机采样**的方式来得到一个 "索引中不重复记录数量的预估值", 
所以Cardinality越接近数据表的总数据数时，表明这个字段越适合加索引。

- 一般经验表明：对于性别、地区、类型、他们取值的范围很小，称为**低选择性** ，尽量不去使用索引

- mysql analyze table、show table status、show index会导致innodb引擎去**重新计算索引的Cardinality的值**。如果数据量巨大，并且表中存在
大量的**辅助索引**时，执行操作可能会非常的慢。

