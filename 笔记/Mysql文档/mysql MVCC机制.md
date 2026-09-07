# MVCC是什么
MVCC（Multi-Version Concurrency Control，多版本并发控制）是InnoDB引擎实现高并发和隔离性的核心技术。
它的精髓可以概括为：“读不加锁，读写不冲突”，通过保存数据的历史版本来实现非阻塞读。
下面我为你拆解MVCC的实现原理，它主要依赖三个隐藏字段、一个Undo日志链表和一个Read View（读视图）。

## 1. 三大基石：隐藏字段 + Undo链
InnoDB为每行数据隐式增加了以下字段（主要是前两个）：

DB_TRX_ID：最近修改该行的事务ID（如插入或最后更新的事务）。

DB_ROLL_PTR：回滚指针，指向该行在undo log中上一个版本的位置。

DB_ROW_ID：隐含的自增ID（如果表没有主键，InnoDB会用此生成聚簇索引）。

### 版本链的形成：
当执行UPDATE时，InnoDB不会直接覆盖原数据。它会：

1、将修改前的行数据复制到undo log中（存入旧版本）。

2、修改当前行数据，将DB_TRX_ID更新为当前事务ID。

3、将DB_ROLL_PTR指向undo log中的旧版本。

这样，新旧数据通过指针串成了一个单向链表（按时间从新到旧排列），这就是版本链（Version Chain）。最新的行记录在链表头，历史版本依次向后排列。


## 2. 核心算法：Read View（读视图）
Read View 是 MVCC 实现可见性判断的“快照”依据。当事务执行快照读（普通的 SELECT）时，InnoDB 会生成一个 Read View，它相当于记录了当前数据库系统的“快照边界”。

Read View 中几个关键属性（均基于事务ID）：

1、m_ids：生成 Read View 时，系统中所有未提交的活跃事务ID列表。

2、min_trx_id：m_ids 中的最小值（低水位）。

3、max_trx_id：生成 Read View 时系统尚未分配的下一个事务ID（高水位，即 m_ids 最大值 + 1）。

4、creator_trx_id：当前执行查询的事务自身的ID。


## 3. 可见性判断规则（灵魂所在）
当事务读取一行数据时，InnoDB会拿着这行数据当前版本的 DB_TRX_ID（记为 trx_id），与 Read View 进行比对。判断逻辑如下：

1、如果 trx_id < min_trx_id（低水位）：说明这个版本的事务在快照生成前就已提交，数据可见。

2、如果 trx_id >= max_trx_id（高水位）：说明这个版本是快照生成后才开启的事务，数据不可见。

3、如果 min_trx_id <= trx_id < max_trx_id：需要进一步判断：

（1）如果 trx_id 在 m_ids 列表中（即当前未提交），数据不可见。
（2）如果 trx_id 不在 m_ids 列表中（说明生成快照时它已提交），数据可见。

若当前版本不可见，就顺着 DB_ROLL_PTR 回滚指针，去到 undo log 中的上一个版本，再用同样的规则判断，直到找到一个可见的版本为止。

## 4. RC 与 RR 隔离级别的关键差异
MVCC 在不同的隔离级别下，生成 Read View 的时机完全不同，这直接决定了“可重复读”和“读已提交”的区别：

隔离级别	Read View 生成时机	效果
1、READ COMMITTED (RC)	每次执行 SELECT 语句时，都会重新生成一个新的 Read View。	每次读都能看到已提交的最新数据，但会导致不可重复读。
2、REPEATABLE READ (RR)	事务内第一次执行 SELECT 时生成一个 Read View，整个事务期间复用这一个旧快照。事务后续多次查询，永远读的是第一次查询时的数据快照，从而实现可重复读。

# 总结核心思想
MVCC 的本质是用“空间（版本链）”换“时间（锁等待）”。它让读操作在无锁的情况下，通过版本链和快照判断，找到属于自己时间点的正确数据，完美实现了读写并行，是InnoDB高性能的基石之一。




