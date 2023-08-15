# 1 慢查询日志
mysql> show variables like '%slow%';
+---------------------------+--------------------------------------------------------+
| Variable_name             | Value                                                  |
+---------------------------+--------------------------------------------------------+
| log_slow_admin_statements | OFF                                                    |
| log_slow_extra            | OFF                                                    |
| log_slow_slave_statements | OFF                                                    |
| slow_launch_time          | 2                                                      |
| slow_query_log            | OFF                                                    |
| slow_query_log_file       | /usr/local/mysql/data/chenyunbindeMacBook-Pro-slow.log |
+---------------------------+--------------------------------------------------------+
6 rows in set (0.01 sec)
- set global slow_query_log=1 开启slow log

# 2 重做日志（redo log）
- 在mysql的存储引擎层产生
- 记录的是物理格式日志，记录的是mysql页数据的变化
- 重做日志是在**事务进行中**不断的被写入的，这表现为日志并不是随事务提交的顺序进行写入的。
- 作用：确保事务的持久性。
    - redo日志记录**事务执行后**的状态，用来恢复未写入data file的已成功事务更新的数据。
    防止在发生故障的时间点，尚有脏页未写入磁盘，在重启mysql服务的时候，根据redo log进行重做，从而达到事务的持久性这一特性。
- 很重要一点，redo log是什么时候写盘的？前面说了是**在事物开始之后逐步写盘的**。
    - 在事务中，Innodb存储引擎先将重做日志写入innodb_log_buffer中，在提交的时候会进行一次数据同步（fsync），将缓冲区中的数据同步到
重做日志文件中。   

mysql> show variables like 'Innodb_log_buffer_size';
+------------------------+----------+
| Variable_name          | Value    |
+------------------------+----------+
| innodb_log_buffer_size | 16777216 |
+------------------------+----------+
1 row in set (0.00 sec)

# 回滚日志（undo log）
- 1 作用：保证数据的原子性，保存了事务发生之前的数据的一个版本，可以用于回滚，同时可以提供多版本并发控制下的读（MVCC），也即非锁定读
- 2 也就是MVCC下的快照是通过undo log实现的
- 3 undo log也会产生redo log，因为undo log也需要进行持久化
- 4 在事务提交时
    - undo log 放入一个**链表中**
    - 不会立即删除undo log，这时因为可能还有其他的事务需要undo log来得到之前的数据版本
    - undo log的删除都是会交给清除线程来处理，purge 线程来判断。
    
- 5 undo loghe格式
    - insert undo log：在 RC RR 隔离级别下是事务隔离的，只对当前事务可见，所以执行之后可以删除对应的undo log。
    - update undo log：delete、update操作产生的undo log，需要提供MVCC机制（RR可重复读需要读取到与事务开始的时候一致的数据），**提交时放入undo log链表**，交给purge线程进行
    最后的删除。
mysql> show variables like '%undo%';
+--------------------------+------------+
| Variable_name            | Value      |
+--------------------------+------------+
| innodb_max_undo_log_size | 1073741824 |
| innodb_undo_directory    | ./         |
| innodb_undo_log_encrypt  | OFF        |
| innodb_undo_log_truncate | ON         |
| innodb_undo_tablespaces  | 2          |
+--------------------------+------------+
5 rows in set (0.00 sec)
- innodb_undo_directory 指定redo log 位置
- innodb_undo_log_encrypt 增加两个变量用于undo redo加密,默认关闭
- innodb_undo_log_truncate参数设置为1，即开启在线回收（收缩）undo log日志文件，支持动态设置。
- innodb_undo_tablespaces参数必须大于或等于2，即回收（收缩）一个undo log日志文件时，要保证另一个undo log是可用的。   
    
# binlog
- 在Mysql数据库上层产生的，也就是mysql的Server层产生，所有的mysql存储引擎都可以实现
- 写入磁盘时间：在事务提交完成后进行写入。
- 用于复制，在主从复制中，从库利用主库上的binlog进行重播，实现主从同步。用于数据库的基于时间点的还原。
- binlog是逻辑日志，可以简单认为**记录的就是sql语句**
- binlog的默认是保持时间由参数expire_logs_days配置，也就是说对于非活动的日志文件，在生成时间超过expire_logs_days配置的天数之后，会被自动删除。
- canal：监听binlog 与 kafka结合 实时监听mysql数据的变化


# 错误日志
- 错误日志记录着mysqld启动和停止,以及服务器在运行过程中发生的错误的相关信息。在默认情况下，系统记录错误日志的功能是关闭的，错误信息被输出到标准错误输出。
- 指定日志路径两种方法:
  　　　　编辑my.cnf 写入 log-error=[path]
  　　　　通过命令参数错误日志 mysqld_safe –user=mysql –log-error=[path] &
  
  
## MVCC
- 多版本并发控制，一致性非锁定读
- 总之在 RC 隔离级别下，是每个快照读都会生成并获取最新的 Read View；
- 而在 RR 隔离级别下，则是同一个事务中的第一个快照读才会创建 Read View, 之后的快照读获取的都是同一个 Read View。