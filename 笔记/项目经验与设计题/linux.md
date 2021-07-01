# 一 Awk 对于文件进行格式化出自己想要的数据
- AWK 是一种处理文本文件的语言，是一个强大的文本分析工具。
更适合格式化文本，对文本进行较复杂格式处理
```
2 this is a test
3 Are you like awk
This's a test
10 There are orange,apple,mongo
```
- 1 每行按空格或TAB分割，输出文本中的1、4项
    - $ awk '{print $1,$4}' log.txt # 行匹配语句 awk '' 只能用单引号
- 2 使用,分割 打印出第一列和第二列  ，**-F相当于内置变量FS, 指定分割字符**
awk -F, '{print $1,$2}'   log.txt
    2 this is a test 
    3 Are you like awk 
    This's a test 
    10 There are orange apple
    
    - 也可以使用内置函数 

- 3 使用多个分隔符： awk -F '[在这里写多个分隔符]'，如 -F ',-/' ，如果要以[]为分隔符，就使用 -F '[][]'
awk -F '[][]' '{print $2;}' localhost_access_log

- 4 统计对应请求的qps

日志文件如下
```
117.136.80.55 - - [19/Jul/2019:23:58:56 +0800] "POST /XxxApiService/order/list HTTP/1.1" 200 1038
117.136.80.55 - - [19/Jul/2019:23:58:56 +0800] "POST /XxxApiService/order/list HTTP/1.1" 200 1038
117.136.80.55 - - [19/Jul/2019:23:58:56 +0800] "POST /XxxApiService/order/list HTTP/1.1" 200 1038
223.104.108.242 - - [19/Jul/2019:23:59:49 +0800] "POST /XxxApiService/control/signIn HTTP/1.1" 200 63
223.104.108.242 - - [19/Jul/2019:23:59:51 +0800] "POST /XxxApiService/card/needRecord HTTP/1.1" 200 61
223.104.108.242 - - [19/Jul/2019:23:59:51 +0800] "POST /XxxApiService/card/list HTTP/1.1" 200 335
117.136.71.250 - - [19/Jul/2019:23:59:51 +0800] "POST /XxxApiService/card/needRecord HTTP/1.1" 200 61
42.90.58.117 - - [19/Jul/2019:23:59:51 +0800] "POST /XxxApiService/order/apply HTTP/1.1" 200 411
117.136.71.250 - - [19/Jul/2019:23:59:51 +0800] "POST /XxxApiService/card/list HTTP/1.1" 200 197
117.136.80.55 - - [19/Jul/2019:23:59:56 +0800] "POST /XxxApiService/order/list HTTP/1.1" 200 1038
117.136.80.55 - - [19/Jul/2019:23:59:56 +0800] "POST /YxxApiService/order/list HTTP/1.1" 200 1038
117.136.80.55 - - [19/Jul/2019:23:59:56 +0800] "POST /YxxApiService/order/list HTTP/1.1" 200 1038
117.136.80.55 - - [19/Jul/2019:23:59:56 +0800] "POST /YxxApiService/order/list HTTP/1.1" 200 1038

```

//把23:58:56秒的 XxxApiService接口的请求取出来计算执行次数，使用wc -l 统计行数
grep '19/Jul/2019:23:58:56' localhost_access_log | grep  'XxxApiService'  | wc -l

//另一种编写方式：输出23:58分:56秒的 XxxApiService接口的请求  可以使用wc -l 输出qps
//1 先根据[]截取：[19/Jul/2019:23:59:56 +0800]， 2 在根据 : 截取 56 +0800 3 最后截取 56
cat localhost_access_log | grep 'XxxApiService' | awk -F '[][]' '{print $2;}' | awk -F ':' '{if($3=='58'){print $4}}' | awk '{if($1 == '56'){print $1}}'

- 5 awk 执行awk脚本， 跟AOP一个样，都是在执行方法的前后增加功能
关于 awk 脚本，我们需要注意两个关键词 BEGIN 和 END。
BEGIN{ 这里面放的是执行前的语句 }   开始
{这里面放的是处理每一行时要执行的语句}  执行
END {这里面放的是处理完所有的行后要执行的语句 }  结束，收尾工作

     - awk -f 脚本文件 需要awk执行的文件


- 6 awk内置函数
$n	当前记录的第n个字段，字段间由FS分隔
$0	完整的输入记录
NF	一条记录的字段的数目
NR	已经读出的记录数，就是行号，从1开始


# 二 sed 处理文本工具，行编辑器，Stream Editor
可以批量的修改文件（非交互式），相比于vim一个文件更快（交互式）
擅长在中间添加文件指令
-n：不输出模式空间内容到屏幕，既不自动打印
-e：输出命令执行后的结果，多点编辑，可以执行多个 -e 命令
    -如：sed -e '3,$d' -e 's/bash/blueshell/' 一条sed命令，删除/etc/passwd第三行到末尾的数据，并把bash替换为blueshell

-f 脚本文件：指定脚本文件执行
-i：直接修改文件，并且增加备份文件 testfile.bak，保证安全性


sed -i.bak 's/Linux/cyb/g' testfile 直接修改文件，并且增加备份文件 testfile.bak，保证安全性



- sed -n '2p' testfile 打印最后一行
- sed -n '$p' testfile 打印最后一行   $命令表示最后

- sed -n '/Li/p' testfile  正则表达式匹配 Li字符串的行，与grep很相似
- sed -n '1,2p' testfile  打印1-2行
 sed -n '1,+2p' testfile


- s/// 搜索替代:  's/查找的内容（支持正则表达式）/替换成的内容/替换标记'
    - 替换标记： 1 g：行内全局替换  2 p：显示替换成功的行
sed 's/Linux/cyb/g' testfile

- 5 追加文件  a
chenyunbin@chenyunbindeMacBook-Pro linux命令练习 % sed -e "4a\\
123" testfile   向4a表示第四行，追加一行123
HELLO LINUX!  
Linux is a free unix-type opterating system.  
This is a linux testfile!  
Linux test 
123

- 6 插入文件。在第二行前插入一句话   
- linux的方式：sed -e "2i hahahhah insert" testfile

- mac的方式
chenyunbin@chenyunbindeMacBook-Pro linux命令练习 % sed -e "2i\\                        
hahahhah insert" testfile
HELLO LINUX!  
hahahhah insert
Linux is a free unix-type opterating system.  
This is a linux testfile!  
Linux test 

- 7 替换文件， 2~4替换为hahaha
    - Mac方式：
    chenyunbin@chenyunbindeMacBook-Pro linux命令练习 % sed "2,4c\\
    hahaha" testfile
    HELLO LINUX!  
    hahaha%
    - linux方式： sed -n '2,4c hahaha' testfile     

# grep：

- 1 或操作
    - grep -E '19/Jul/2019:23:58:56|XxxApiService' localhost_access_log
    
    
- 2 与操作
    - grep '条件1' filename | grep '条件2'
    - 如 grep '19/Jul/2019:23:58:56' localhost_access_log | grep  'XxxApiService'  | wc -l 来计算qps。
    
    
    




## CPU占用过高排查 对应的进程-》对应的线程-》对应的代码程序
https://blog.csdn.net/chenjunan888/article/details/80447800
- 1. 使用top命令定位异常进程。可以看见12836的CPU和内存占用率都非常高
- 2. 此时可以再执行ps -ef | grep java，查看所有的java进程，在结果中找到进程号为12836的进程，即可查看是哪个应用占用的该进程。
- 3. 使用**top -H -p 进程号** 查看异常线程
- 4. 使用printf "%x\n" 线程号将异常线程号转化为16进制
- 5. jstack 进程号 | grep 324b -A90（最后的-A90是日志行数，也可以输出为文本文件或使用其他数字）
    - 打印出堆栈信息，查找对应的16进制的线程号


## linux如何查找大文件，并且安全删除
- 1 Linux df（英文全拼：disk free） 命令用于显示目前在 Linux 系统上的文件系统磁盘使用情况统计。 
    - du -ah 查看所有文件占用内存大小
- 2 Linux find 命令用来在指定目录下查找文件
    - find . -type f -size +1M  查找当前目录下的文件大于1M的， -1M小于，没有等于
        - -type 文件类型，d：目录 f：文件
    - find . -name "*png"  查找png结尾的文件

- 3 清除文件内容：
    - echo > /var/log/big.log
    - cat > /var/log/big.log
    - Linux环境下，如果删除一个很大的单文件， 直接使用rm 等命令删除，会引起IO陡增， CPU陡增的情况，为平缓删除大文件带来的影响，使用truncate辅助，通过逐步的缩小文件，达到平滑删除的目的。
      

## free 查看内存占用大小
- https://www.runoob.com/linux/linux-comm-free.html
- 项目开发过程中，线程的不合理使用或者集合的不合理使用，通常会导致内存oom情况，对于内存瓶颈一般通过top命令查看，或者free命令查看内存使用情况;更详细可以通过vmstat命令查看
- 1 ps -ef | grep java 找到对应的pid
- 2 top -p pid  可以看到对应的 %CPU MEM