# Inner join
select XXX from table1 inner join table2 on tables1.colum1 = tables2.clolum2;

INNER JOIN（内连接,或等值连接）：获取两个表中字段匹配关系的记录。

LEFT JOIN（左连接）：获取左表所有记录，即使右表没有对应匹配的记录。 **（常用）**

RIGHT JOIN（右连接）： 与 LEFT JOIN 相反，用于获取右表所有记录，即使左表没有对应匹配的记录。

> 注意：select * from a left join b on a.id = b.id where a.buyer = 'cyb'
> on：是两个表连接的条件，从而生成一张临时表
> where：是对临时表的筛选条件，此时不会在意（a left join b：返回a的所有数据），而是直接筛选掉不符合条件的数据

# ALTER 
https://www.runoob.com/mysql/mysql-alter.html
## 1 添加字段
ALTER TABLE 表名 ADD(
order_create_date varchar(20) NOT NULL DEFAULT ''  COMMENT '订单创建时间， yyyy-MM-dd',
customer_owner varchar(100) NOT NULL COMMENT 'customer表的owner：用于：创建订单客户数的统计',
customer_owner_name varchar(100) DEFAULT NULL COMMENT 'customer表的owner的名字'
)

## 2 修改字段
ALTER TABLE 表名 MODIFY 
customer_owner varchar(100) DEFAULT NULL COMMENT 'customer表的owner：用于：创建订单客户数的统计'

## 3 删除字段
ALTER TABLE testalter_tbl DROP 字段名;

## 4 添加索引
- 唯一索引
ALTER TABLE 表名 ADD UNIQUE INDEX uniq_usr_credit_pass (`customer_Id`,`shop_code`,`datestr`); 
- 普通索引
ALTER TABLE `table_name` ADD INDEX index_name (`column` ) 
ALTER TABLE `table_name` ADD INDEX index_name (`column1`, `column2`, `column3`)
- 添加主键索引 
ALTER TABLE 表名 ADD PRIMARY KEY (`column`) 

## 5 修改字段默认值
ALTER TABLE 表名 ALTER 字段名 SET DEFAULT 1000;
例子：ALTER TABLE deal_order_cp ALTER status_code set default 0
## 6 修改表名
ALTER TABLE 老表名 RENAME TO 新表名;
ALTER TABLE deal_order_cp RENAME TO deal_order_c;


# 5 分组：group by
https://segmentfault.com/a/1190000006821331
group by a,b,c... 多个字段时：是将具有相同a b c...的记录放到一个分组里

product buyer spending
pd1	   cyb	7
pd2	   cyb1	77
pd1	   cyb	7
pd2	   cyb1	77
pd3	   cyb2	777

统计每个用户在每种商品上花多少钱。
select product , buyer, sum(spending) from order_cyb group by product,buyer
结果：
product buyer spending
pd1	   cyb	   14
pd2	   cyb1	   154
pd3    cyb2	   777

# 6 分组：order by
多个字段排序：按字段先后顺序排优先级，第一个优先级最大，
- order by a asc,b desc  现根据a升序排序之后，**a相同的情况下**，在根据b降序排序(asc默认可以不写)
- order by a desc,b desc desc降序排序必须写
- order by a desc,b desc,c desc...  先根据a升序降序之后，a在相同的值的情况下b再根据降序排序，b在相同的值的情况下c再根据降序排序
  以此类推...
  
# 7 like 
- like '%匹配语句%'  ，  %为通配符
- like 可以代替 = 使用，条件是主键时不会使用索引 如：id like 1（不会使用索引）  和 id = 1 
- 如果where条件字段设置了索引，是可以使用索引的，如：buyer设置了索引
explain select * from order_cyb where buyer like 'cyb'

# 8 NULL的处理
- IS NULL: 当列的值是 NULL,此运算符返回 true。
- IS NOT NULL: 当列的值不为 NULL, 运算符返回 true。
- <=>: 比较操作符（不同于 = 运算符），当比较的的两个值相等或者都为 NULL 时返回 true。
- 关于 NULL 的条件比较运算是比较特殊的。你不能使用 = NULL 或 != NULL 在列中查找 NULL 值 ,不会起作用

# 9 UNION 和 UNION ALL
- UNION会删除重复数据，但是效率更低，因为mysql会进行去重操作
- 不会删除重复数据，但是效率更高

# 10 正则表达式
https://www.runoob.com/mysql/mysql-regexp.html
- 查找name字段中以'st'为开头的所有数据：
mysql> SELECT name FROM person_tbl WHERE name REGEXP '^st';

- 查找name字段中以'ok'为结尾的所有数据：
mysql> SELECT name FROM person_tbl WHERE name REGEXP 'ok$';

- 查找name字段中包含'mar'字符串的所有数据：
mysql> SELECT name FROM person_tbl WHERE name REGEXP 'mar';

- 查找name字段中以元音字符开头或以'ok'字符串结尾的所有数据：
mysql> SELECT name FROM person_tbl WHERE name REGEXP '^[aeiou]|ok$';

# 11 事务
- **BEGIN** 或 START TRANSACTION 显式地开启一个事务
- **COMMIT** 会提交事务，并使已对数据库进行的所有修改成为永久性的
- **ROLLBACK** 回滚会结束用户的事务，并撤销正在进行的所有未提交的修改
- SAVEPOINT identifier 允许在事务中创建一个保存点，一个事务中可以有多个 SAVEPOINT
- ROLLBACK TO identifier 把事务回滚到标记点； RELEASE SAVEPOINT savepoint_name 释放一个保存点
- SET TRANSACTION 用来设置事务的隔离级别。InnoDB 存储引擎提供事务的隔离级别有READ UNCOMMITTED、READ COMMITTED、REPEATABLE READ 和 SERIALIZABLE。
- 直接用 SET 来改变 MySQL 的自动提交模式:
  SET AUTOCOMMIT=0 禁止自动提交，这时必须手动开启事务才可以
  SET AUTOCOMMIT=1 开启自动提交（mysql默认开启），可以不用**显示的**开启事务
- 不管autocommit 是1还是0 
  START TRANSACTION 后，只有当commit数据才会生效，ROLLBACK后就会回滚。

# 12 MySQL 临时表
- MySQL 临时表在我们需要保存一些临时数据时是非常有用的。临时表只在当前连接可见，当关闭连接时，Mysql会自动删除表并释放所有空间。
- CREATE TEMPORARY TABLE SalesSummary

# 13 复制表结构和数据
- 建复制表： CREATE TABLE targetTable LIKE sourceTable;
- 将原表数据插入复制表中： INSERT INTO targetTable SELECT * FROM sourceTable;
- 例如：
create table order_cyb_cp like order_cyb
insert into order_cyb_cp select * from order_cyb

# 14 mysql 表数据的迁移（导入，导出数据）
https://www.runoob.com/mysql/mysql-database-export.html
## 导出
- 使用 SELECT ... INTO OUTFILE 语句导出数据
- mysqldump 是 mysql 用于转存储数据库的实用程序。它主要产生一个 SQL 脚本，其中包含从头重新创建数据库所必需的命令 CREATE TABLE INSERT 等。
- - 导出整个数据库的数据：mysqldump -u root -p RUNOOB > database_dump.txt
- - 备份所有数据库：mysqldump -u root -p --all-databases > database_dump.txt
## 导入
- mysql -u用户名 -p密码 <  要导入的数据库数据(runoob.sql)
  将备份的整个数据库 runoob.sql 导入：mysql -uroot -p123456 < runoob.sql
- source 命令导入
 mysql> create database abc;      # 创建数据库
 mysql> use abc;                  # 使用已创建的数据库 
 mysql> set names utf8;           # 设置编码
 mysql> source /home/abc/abc.sql  # 导入备份数据库



# 15 查看元数据
- SHOW VARIABLES 所有元数据(服务器配置变量)
- SHOW VARIABLES like 'AUTOCOMMIT' 单个元数据
- 例如SET AUTOCOMMIT=1 来修改

# **16 MySQL 处理重复数据**
https://blog.csdn.net/junmoxi/article/details/96462066
- 建立唯一索引
- 查询重复数据：原理：先按照要查询出现重复数据的列，进行分组查询。count > 1 代表出现 2 次或 2 次以上
SELECT COUNT(*) as repetitions, last_name, first_name
             FROM person_tbl
             GROUP BY last_name, first_name
             HAVING repetitions > 1;
             
- 过滤重复数据：distinct
- **INSERT ignore**：插入时忽略重复数据，如果有**主键，唯一索引冲突时**，不会进行插入
例如：INSERT IGNORE INTO `order_cyb` (`id`, `product`, `buyer`, `spending`, `order_by_count`, `order_by_count2`)
VALUES(6, 'pd1', 'cyb', 7, 10, 8);

- **REPLACE INTO**：当出现主键或唯一索引重复之后，**会删除原先的数据**，并将这个新的记录插入进去
例如：REPLACE INTO `order_cyb` (`id`, `product`, `buyer`, `spending`, `order_by_count`, `order_by_count2`)
VALUES(6, 'pd1', 'cyb77777', 7, 10, 8);

- **on duplicate key update** ：当出现主键或唯一索引重复之后，会执行**更新**操作
 [ˈduːplɪkeɪt , ˈduːplɪkət]  
 INSERT INTO `order_cyb` (`id`, `product`, `buyer`, `spending`, `order_by_count`, `order_by_count2`)
 VALUES(6, 'pd1', 'cyb77777', 7, 10, 8) on duplicate key update buyer = 'on duplicate key update cyb'  
 主键冲突，更新buyer = 'on duplicate key update cyb'
 
   

# 17 常用函数
- sum(表达式或值) MAX(字段) MIN(字段) AVG(字段)
- DATE_FORMAT(now(),'%Y-%m-%d %H-%m-%s')
- - select DATE_FORMAT(now(),'%Y-%m-%d %H-%m-%s')
- - select DATE_FORMAT(now(),'%Y-%m-%d %H:%m:%s')
    h：不分24小时，他是从0-11
    H：分24小时，从0-23点

- now()：年月日 时分秒：2020-12-14 18:17:38
- CURDATE()：年月日：2020-12-14
- CURTIME()：时分秒：18:17:15
- DATE('2020-12-14 18:17:38')：从日期或日期时间表达式中提取日期值select DATE('2020-12-14 18:17:38')
  返回：2020-12-14

- 函数从日期减去指定的时间间隔： DATE_SUB(date,INTERVAL expr type)
  例如：select DATE_SUB('2020-12-14 18:17:38',INTERVAL 2 DAY)
  
- 计算起始日期 d 加上一个时间段后的日期：DATE_ADD(date，INTERVAL expr type)
  例如：select DATE_ADD('2020-12-14 18:17:38',INTERVAL 2 DAY)
  
# 18 MySQL 运算符
- mod







