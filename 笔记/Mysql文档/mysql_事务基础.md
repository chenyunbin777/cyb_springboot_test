# 一 事务隔离级别
## read uncommitted：读未提交，不可避免重复读，幻读
## read committed：读已提交，不可避免重复读，幻读
## repeatable read：可重复读，不可避免幻读（Mysql默认级别）
## seaializable：串行化，读用读锁，写用写锁，读锁和写锁互斥，这么做可以有效的避免幻读、不可重复读、脏读等问题
## 引起的问题汇总
- 脏读：就是一个事务读到了另个事务还没有提交的数据
- 不可重复读：两个事务同时对一样的数据进行操作，其中一个事务更新了数据，另一个事务读到的数据与之前的不一样了。
  这样就发生了在一个事务内两次读到的数据是不一样的，因此称为是不可重复读。
  **不可重复读的**重点是 **修改**，同样的条件，你读取过的数据，再次读取出来发现值不一样了
- 幻读：**主要是针对插入的数据**
  一个事务(同一个read view)在前后两次查询同一范围的时候，后一次查询看到了前一次查询没有看到的行。
  就好像发生了幻觉一样！
  - RR 级别下通过next key lock来解决幻读的问题
  
    
  **插入**
> 注意：
> 1 在可重复读中，该sql第一次读取到数据后，就将这些数据加锁（悲观锁），其它事务无法修改(update)这些数据，就可以实现可重复读了。
> 2 但是，这样还是无法阻止插入数据，这样就会产生幻读，**不能通过行锁来避免** 
> 3 需要Serializable隔离级别 ，读用读锁，写用写锁，读锁和写锁互斥，这么做可以有效的避免幻读、不可重复读、脏读等问题，但会极大的降低数据库的并发能力。


# 二 Mysql如何解决的上述问题
为了解决上述问题，数据库通过锁机制解决并发访问的问题。
根据锁定对象不同：分为行级锁和表级锁；根据并发事务锁定的关系上看：分为共享锁定和独占锁定，
- 共享锁定：会防止独占锁定但允许其他的共享锁定。
- 而独占锁：定既防止共享锁定也防止其他独占锁定。
- 为了更改数据，数据库必须在进行更改的行上施加行独占锁定，insert、update、delete和selsct for update语句都会隐式采用必要的行锁定。
- 但是直接使用锁机制管理是很复杂的，基于锁机制，数据库给用户提供了不同的事务隔离级别，只要设置了事务隔离级别，数据库就会分析事务中的sql语句然后自动选择合适的锁。 

# 三 锁
https://zhuanlan.zhihu.com/p/143866444
select for update 必须在 begin; commit; 事务中执行

## 总结：
- 1、InnoDB行锁是通过给索引上的索引项加锁来实现的，只有通过索引条件检索数据，InnoDB才使用行级锁，否则，InnoDB将使用表锁。
- 2、由于MySQL的行锁是针对索引加的锁，不是针对记录加的锁，所以虽然是访问不同行的记录，但是如果是使用相同的索引键，是会出现锁冲突的。应用设计的时候要注意这一点。
表user user_id为主键索引  name没有加索引
如：select * from user where user_id = 'a' name = 'aname' for update
select * from user where user_id = 'a' name = 'bname' for update
这两个查询是会冲突了，应该使用了相同的索引

- 3、当表有多个索引的时候，不同的事务可以使用不同的索引锁定不同的行，另外，不论是使用主键索引、唯一索引或普通索引，InnoDB都会使用行锁来对数据加锁。
- 4、即便在条件中使用了索引字段，但是否使用索引来检索数据是由MySQL通过判断不同执行计划的代价来决定的，如果MySQL认为全表扫描效率更高，比如对一些很小的表，它就不会使用索引，这种情况下InnoDB将使用表锁，而不是行锁。因此，在分析锁冲突时，别忘了检查SQL的执行计划，以确认是否真正使用了索引。
- 5、检索值的数据类型与索引字段不同，虽然MySQL能够进行数据类型转换，但却不会使用索引，从而导致InnoDB使用表锁。通过用explain检查两条SQL的执行计划，我们可以清楚地看到了这一点。

# 三 事务传播行为（传递属性更好理解）
https://blog.csdn.net/weixin_39625809/article/details/80707695

- MANDATORY：支持事务 如果没后事务则会抛出异常throw new IllegalTransactionStateException(“Transaction propagation ‘mandatory’ but no existing transaction found”);
- NESTED：嵌套事务：一个非常重要的概念就是内层事务依赖于外层事务。外层事务失败时，会回滚内层事务所做的动作。而内层事务操作失败并不会引起外层事务的回滚。
- REQUIRED：不存在则创建事务，如果存在则使用当前事务REQUIRED
- REQUIRES_NEW：新创建一个事务B，如果存在事务A则**挂起当前事务A**，当B执行完成之后 才会去继续执行A
- SUPPORTS:有事务执行 没有事务就按无事务执行
- NOT_SUPPORT:不支持事务 有事务A则挂起，然后以非事务模式执行，完事后继续执行事务A
- NEVER：不支持事务，有事务则抛异常

> 注意 PROPAGATION_NESTED 与PROPAGATION_REQUIRES_NEW的区别：
> * 由此可见, PROPAGATION_REQUIRES_NEW 和 PROPAGATION_NESTED 的最大区别在于, PROPAGATION_REQUIRES_NEW 完全是一个新的事务,
> * 而 PROPAGATION_NESTED 则是外部事务的子事务, 如果外部事务 commit, 嵌套事务也会被 commit, 这个规则同样适用于 roll back.


# 事务的保存点，回滚
- savepoint a;
- rollback to savepoint a;