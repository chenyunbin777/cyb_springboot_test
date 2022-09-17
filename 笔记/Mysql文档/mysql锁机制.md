# mysql锁机制

## 表锁
- 锁定整张表

## 行锁
- 一共分为三种：记录锁 间隙锁 next key lock
- 锁定当前操作行
- InnoDB支持行锁，而MyISAM支持表级锁
  InnoDB的行锁是实现在索引上的，而不是锁在物理行记录上。潜台词是，**如果访问没有命中索引**，也无法使用行锁，将要退化为表锁。
  命中索引：**包括 没有查到数据，或者查询的字段没加索引**
  如：user表 id主键，name建立普通的索引，age 没有索引
      select * from user where name = 'cyb' 如果没有查到，表锁；如果查到了，行锁
      select * from user where age = 18 表锁，因为age没有加索引

- 1 记录锁 Record lock
- 2 间隙锁 gap lock: 锁定区间是前开后开 ()
    - 可重复读级别下才会有间隙锁！
    - 解决了mysql RR（可重复读）级别下是幻读的问题。
    - 锁定一个范围，但不包括记录本身
    - 例如：SELECT * FROM `test` WHERE `id` BETWEEN 5 AND 7 FOR UPDATE;  这里我们只是锁定了 (5,7)不包括记录5 7 本身

- 3 next key lock 临键锁，间隙锁的另一种形态
    - 记录锁 + 间隙锁 = 临键锁
    - 我们统一使用  select ... **for update**; 来加锁。
    - 举例：主键id：  5 7 11
    - 在mysql8.0.22版本下
        - 1 SELECT * FROM `test` WHERE `id` BETWEEN 4 AND 11 FOR UPDATE; 锁定(4 11),**则其他事务所有的insert都不被允许**
        - 2 SELECT * FROM `test` WHERE `id` BETWEEN 5 AND 11 FOR UPDATE; (-无穷,5) 可以插入数据，**其他范围都不允许**
        - 3 SELECT * FROM `test` WHERE `id` BETWEEN 5 AND 8 FOR UPDATE; (-无穷,5) (11,+无穷)都可以插入数据
        - 4 SELECT * FROM `test` WHERE `id` BETWEEN 5 AND 12 FOR UPDATE; (-无穷,5) 可以插入数据，其他范围都不允许
        
        - 总结，如果锁定范围超出了id的范围那么就会锁定所有的超出范围，如1 锁定了id左边的左右范围 （-无穷，5], 2 锁定了所有的右边范围[12,+无穷）
        - 其他正常的 3 就会锁定[5,7),[7,8),[8,11] 
        
    - 如果存在，主键id，辅助索引 b，数据集合 
    - (1,1)
    - (3,1)
    - (5,3)
    - (7,6)
    - (10,8)
    - 锁定语句：where b = 3 for update
        - 对于主键聚蔟索引来说就是在id=5上加了一个 记录锁， 但是对于辅助索引来说**不仅锁定了b=3的记录，也锁定了(1,3) (3,6)的间隙范围。**
    
- 关闭间隙锁 
 将事务的隔离级别设置为READ COMMITTED
   将参数innodb_locks_unsafe_for_binlog设置为1   


- select ** lock in share mode: 共享锁



## 悲观锁
- mysql中的悲观锁的实现是通过mysql的 排它锁 来实现的。也就类似于我们java中的synchronized。
- 那在mysql中我们如何操作呢?
    - 1.开始事务
      begin;/begin work;/start transaction; (三者选一就可以)
      2.查询出商品信息
      select ... **for update**;(这里是使用的行锁的排他锁)
      3.提交事务
      commit;/commit work;

- 排他锁的原理：必须等待上一个线程操作解锁之后下一个线程才可以获取到对应的锁来执行。
- 缺点：可能产生死锁

## 乐观锁
- 锁如其名，我们乐观的认为当我们进行数据更新时，不会有其他线程来操作同一组数据，也就是我们认为不会有多线程竞争的情况发生。
- mysql中的实现的方式
    - 1 使用数据版本号的方式，我们为每一行的数据都增加一个version字段，读取数据时拿到这个值，在更新的时候我们会再次去判断这个值与我们刚才
    拿到的值是否相同，如果相同我们认为该数据没有被其他的线程所修改； 如果不相同，说明有其他的线程进行了数据修改，那我们就进行**事务回滚**
    - 2 
    
- 缺点：
    - 无法解决ABA的问题
- 优点：
    - 不会产生死锁
    
    
## 死锁
- mysql如何解决死锁问题
    - 1 超时机制，通过innodb_lock_wait_timeout来设置超时时间，即等待获取锁到了超时时间之后会进行事务的回滚（undo log）。
    
    - 2 wait-for graph 等待图的方式来进行死锁的检测，在每个事务请求所并且发生等待时都会**判断是否存在回路**，
    若存在则有死锁，通常innodb存储引擎选择回滚**undo量最小的事务**。
 