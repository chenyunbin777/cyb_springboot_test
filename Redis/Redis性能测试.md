#Redis 性能测试
 通过同时执行多个命令实现的。

## 语法
- redis 性能测试的基本命令如下：
redis-benchmark [option] [option value]
> 注意：该命令是在 redis 的目录下执行的，而不是 redis 客户端的内部指令。
- 可选参数：
序号	选项	描述	默认值
1	-h	指定服务器主机名	127.0.0.1
2	-p	指定服务器端口	6379
3	-s	指定服务器socket	 
4	-c	指定并发连接数	50
5	-n	指定请求数	10000
6	-d	以字节的形式指定SET/GET值的数据大小	2
7	-k	1=keep alive 0=reconnect	1
8	-r	SET/GET/INCR使用随机key,SADD使用随机值	 
9	-p	通过管道传输<numreq>请求	1
10	-q	强制退出redis。仅显示query/sec值(吞吐率)	 
11	--csv	以CSV格式输出	 
12	-l	生成循环，永久执行测试	 
13	-t	仅运行以逗号分隔的测试命令列表	 
14	-l	Idle模式。仅打开N个idle连接等待	 

## 实例
- 以下实例同时执行 10000 个请求来检测性能：
吞吐率：
https://blog.csdn.net/beiniao520/article/details/80240974

$ redis-benchmark -n 10000  -q   10000个请求下，各个命令的吞吐率
PING_INLINE: 55865.92 requests per second
PING_BULK: 33222.59 requests per second
SET: 37735.85 requests per second
GET: 44052.86 requests per second
INCR: 56179.77 requests per second
LPUSH: 49261.09 requests per second
RPUSH: 61349.69 requests per second
LPOP: 52631.58 requests per second
RPOP: 62500.00 requests per second
SADD: 62500.00 requests per second
HSET: 67114.09 requests per second
SPOP: 70921.98 requests per second
ZADD: 69930.07 requests per second
ZPOPMIN: 69444.45 requests per second
LPUSH (needed to benchmark LRANGE): 70921.98 requests per second
LRANGE_100 (first 100 elements): 19379.85 requests per second
LRANGE_300 (first 300 elements): 7451.56 requests per second
LRANGE_500 (first 450 elements): 5120.33 requests per second
LRANGE_600 (first 600 elements): 4627.49 requests per second
MSET (10 keys): 60975.61 requests per second


- 例如测试100个客户端一共请求20000次
    - 命令：redis-benchmark -c 100 -n 20000
    - redis-benchmark会对各类数据结构的命令进行测试，并给出性能指标
        - GET 100个并发客户端在0.24s完成了2w次请求，每个请求数据量是3个字节
        ====== GET ======
          20000 requests completed in 0.24 seconds
          100 parallel clients
          3 bytes payload
          keep alive: 1  //保持连接
          host configuration "save": 900 1 300 10 60 10000 //rds持久化配置：表示如果900秒内至少1个key发生变化（新增、修改和删除），则重写rdb文件；
          host configuration "appendonly": no //是否开启aof持久化模式 ：no
          multi-thread: no
        
        98.09% <= 1 milliseconds //98.09%的命令执行时间小于1ms，一次类推
        99.82% <= 2 milliseconds
        100.00% <= 2 milliseconds
        82304.52 requests per second  //吞吐率是82304.52，每秒可以处理 82304.52次GET请求。
        ====== MSET (10 keys) ======
          20000 requests completed in 0.28 seconds
          100 parallel clients
          3 bytes payload
          keep alive: 1
          host configuration "save": 900 1 300 10 60 10000
          host configuration "appendonly": no
          multi-thread: no
        
        58.21% <= 1 milliseconds
        98.07% <= 2 milliseconds
        99.80% <= 3 milliseconds
        100.00% <= 3 milliseconds
        70921.98 requests per second
