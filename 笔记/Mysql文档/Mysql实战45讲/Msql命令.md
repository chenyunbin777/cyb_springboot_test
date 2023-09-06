# 一 innodb_thread_concurrency 并发线程数
# 二 SELECT * FROM information_schema.innodb_trx 可以查看正在执行的事务信息
- 字段解析innodb_trx表：
  *    trx_id：事务ID。
  *    trx_state：事务状态，有以下几种状态：RUNNING、LOCK WAIT、ROLLING BACK 和 COMMITTING。
  *    trx_started：事务开始时间。
  *    trx_requested_lock_id：事务当前正在等待锁的标识，可以和 INNODB_LOCKS 表 JOIN 以得到更多详细信息。
  *    trx_wait_started：事务开始等待的时间。
  *    trx_weight：事务的权重。
  *    trx_mysql_thread_id：事务线程 ID，可以和 PROCESSLIST 表 JOIN。
  *    trx_query：事务正在执行的 SQL 语句。
  *    trx_operation_state：事务当前操作状态。
  *    trx_tables_in_use：当前事务执行的 SQL 中使用的表的个数。
  *    trx_tables_locked：当前执行 SQL 的行锁数量。
  *    trx_lock_structs：事务保留的锁数量。
  *    trx_lock_memory_bytes：事务锁住的内存大小，单位为 BYTES。
  *    trx_rows_locked：事务锁住的记录数。包含标记为 DELETED，并且已经保存到磁盘但对事务不可见的行。
  *    trx_rows_modified：事务更改的行数。
  *    trx_concurrency_tickets：事务并发票数。
  *    trx_isolation_level：当前事务的隔离级别。
  *    trx_unique_checks：是否打开唯一性检查的标识。
  *    trx_foreign_key_checks：是否打开外键检查的标识。
  *    trx_last_foreign_key_error：最后一次的外键错误信息。
  *    trx_adaptive_hash_latched：自适应散列索引是否被当前事务锁住的标识。
  *    trx_adaptive_hash_timeout：是否立刻放弃为自适应散列索引搜索 LATCH 的标识。innodb_locks表:
  *    lock_id：锁 ID。
  *    lock_trx_id：拥有锁的事务 ID。可以和 INNODB_TRX 表 JOIN 得到事务的详细信息。
  *    lock_mode：锁的模式。有如下锁类型：行级锁包括：S、X、IS、IX，分别代表：共享锁、排它锁、意向共享锁、意向排它锁。表级锁包括：S_GAP、X_GAP、IS_GAP、IX_GAP 和 AUTO_INC，分别代表共享间隙锁、排它间隙锁、意向共享间隙锁、意向排它间隙锁和自动递增锁。
  *    lock_type：锁的类型。RECORD 代表行级锁，TABLE 代表表级锁。
  *    lock_table：被锁定的或者包含锁定记录的表的名称。
  *    lock_index：当 LOCK_TYPE=’RECORD’ 时，表示索引的名称；否则为 NULL。
  *    lock_space：当 LOCK_TYPE=’RECORD’ 时，表示锁定行的表空间 ID；否则为 NULL。
  *    lock_page：当 LOCK_TYPE=’RECORD’ 时，表示锁定行的页号；否则为 NULL。
  *    lock_rec：当 LOCK_TYPE=’RECORD’ 时，表示一堆页面中锁定行的数量，亦即被锁定的记录号；否则为 NULL。
  *    lock_data：当 LOCK_TYPE=’RECORD’ 时，表示锁定行的主键；否则为NULL。innodb_lock_waits表:
  *    requesting_trx_id：请求事务的 ID。
  *    requested_lock_id：事务所等待的锁定的 ID。可以和 INNODB_LOCKS 表 JOIN。
  *    blocking_trx_id：阻塞事务的 ID。
  *    blocking_lock_id：某一事务的锁的 ID，该事务阻塞了另一事务的运行。可以和 INNODB_LOCKS 表 JOIN。
- 查看正在锁的事务 SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCKS;
- 查看等待锁的事务 SELECT * FROM INFORMATION_SCHEMA.INNODB_LOCK_WAITS;
- 首先查询是否锁表 SHOW OPEN TABLES WHERE In_use > 0;
- 查询进程 SHOW PROCESSLIST;

# 三 