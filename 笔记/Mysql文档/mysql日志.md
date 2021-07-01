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
- 作用：确保事务的持久性。
    - redo日志记录事务执行后的状态，用来恢复未写入data file的已成功事务更新的数据。
    防止在发生故障的时间点，尚有脏页未写入磁盘，在重启mysql服务的时候，根据redo log进行重做，从而达到事务的持久性这一特性。
- 很重要一点，redo log是什么时候写盘的？前面说了是在事物开始之后逐步写盘的。
之所以说重做日志是在事务开始之后逐步写入重做日志文件，而不一定是事务提交才写入重做日志缓存，
原因就是，重做日志有一个缓存区Innodb_log_buffer，Innodb_log_buffer的默认大小为8M(这里设置的16M),
Innodb存储引擎先将重做日志写入innodb_log_buffer中。   

mysql> show variables like 'Innodb_log_buffer_size';
+------------------------+----------+
| Variable_name          | Value    |
+------------------------+----------+
| innodb_log_buffer_size | 16777216 |
+------------------------+----------+
1 row in set (0.00 sec)

# 回滚日志（undo log）
- 1 作用：保证数据的原子性，保存了事务发生之前的数据的一个版本，可以用于回滚，同时可以提供多版本并发控制下的读（MVCC），也即非锁定读
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
- 用于复制，在主从复制中，从库利用主库上的binlog进行重播，实现主从同步。用于数据库的基于时间点的还原。
- binlog是逻辑日志，可以简单认为记录的就是sql语句
- binlog的默认是保持时间由参数expire_logs_days配置，也就是说对于非活动的日志文件，在生成时间超过expire_logs_days配置的天数之后，会被自动删除。
- canal：监听binlog 与 kafka结合 实时监听mysql数据的变化


# 错误日志
- 错误日志记录着mysqld启动和停止,以及服务器在运行过程中发生的错误的相关信息。在默认情况下，系统记录错误日志的功能是关闭的，错误信息被输出到标准错误输出。
- 指定日志路径两种方法:
  　　　　编辑my.cnf 写入 log-error=[path]
  　　　　通过命令参数错误日志 mysqld_safe –user=mysql –log-error=[path] &