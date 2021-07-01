# Redis安装
https://blog.csdn.net/realize_dream/article/details/106227622
## 目录
/usr/local/bin:下放至Redis的命令
/usr/local/etc：下放Redis的配置文件


Mac安装Redis
使用Homebrew安装Redis
1、没有安装Homebrew，首先安装npm国内的吧，快一些。
打开终端输入以下命令：

/bin/zsh -c "$(curl -fsSL https://gitee.com/cunkai/HomebrewCN/raw/master/Homebrew.sh)"

2、使用Homebrew安装命令

brew install redis

执行上述命令后出现以下，则成功安装：

==> Downloading https://mirrors.ustc.edu.cn/homebrew-bottles/bottles/redis-6.0.1
######################################################################## 100.0%
==> Pouring redis-6.0.1.mojave.bottle.tar.gz
==> Caveats
To have launchd start redis now and restart at login:
  brew services start redis
Or, if you don't want/need a background service you can just run:
  redis-server /usr/local/etc/redis.conf
==> Summary
🍺  /usr/local/Cellar/redis/6.0.1: 13 files, 3.7MB

3、 查看安装及配置文件位置

Homebrew安装的软件会默认在/usr/local/Cellar/路径下

redis的配置文件redis.conf存放在/usr/local/etc路径下

4、启动redis服务

//方式一：使用brew帮助我们启动软件
brew services start redis
//方式二
redis-server /usr/local/etc/redis.conf

//执行以下命令
redis-server


5、查看redis服务进程

我们可以通过下面命令查看redis是否正在运行

ps axu | grep redis
ps -ef | grep redis

6、redis-cli连接redis服务

redis默认端口号6379，默认auth为空，输入以下命令即可连接  密码：123456

redis-cli -h 127.0.0.1 -p 6379 -a 123456

7、启动 redis 客户端，打开终端并输入命令 redis-cli。该命令会连接本地的 redis 服务。

$redis-cli
redis 127.0.0.1:6379>
redis 127.0.0.1:6379> PING
PONG

在以上实例中我们连接到本地的 redis 服务并执行 PING 命令，该命令用于检测 redis 服务是否启动。

8、关闭redis服务

正确停止Redis的方式应该是向Redis发送SHUTDOWN命令
redis-cli shutdown
redis-cli -a 123456 shutdown  

强行终止redis
sudo pkill redis-server
1
9、redis.conf 配置文件详解

redis默认是前台启动，如果我们想以守护进程的方式运行（后台运行），可以在redis.conf中将daemonize no,修改成yes即可。