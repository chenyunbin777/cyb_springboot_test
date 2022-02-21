# Docker 容器连接

## 网络端口映射
- 1 将本地的端口映射到容器的端口上，然后我们就可以在本地通过浏览器来访问到对应的容器
- docker run -itd -p 5007:5000 training/webapp python app.py    指定本地端口=》容器内部端口映射关系  指定镜像   执行的命令
    - -P 大屁:是容器内部端口随机映射到主机的高端口。
    - -p 小屁: 是容器内部端口绑定到指定的主机端口。
    
    - 通过 http://127.0.0.1:5007/访问到容器
    
- 2 绑定ip与端口：
    - docker run -itd -p 127.0.0.1:5007:5000 training/webapp python app.py 
- 3 默认都是绑定的tcp 端口，想要绑定udp的话，可以在端口后面加上 /udp。
- 4 查询端口的映射关系：docker port deff63bc9c72  

## 容器互联
端口映射并不是唯一把 docker 连接到另一个容器的方法。
docker 有一个连接系统允许将多个容器连接在一起，共享连接信息。
docker 连接会创建一个父子关系，其中父容器可以看到子容器的信息。

- 1 容器命名
    - docker run -dit -P --name cyb centos /bin/bash
    - 58723b741eeaeafb9f5c759b08296b65ba6a7f5a924ba186ab5e1c0aec0e022b

- 2 新建网络：下面先创建一个新的 Docker 网络。
    - 1 docker network create -d bridge test-net
    - 2 运行一下两个容器
        - docker run -itd --name test1 --network test-net ubuntu /bin/bash   
        - docker run -itd --name test2 --network test-net ubuntu /bin/bash   
    - 3 通过ping命令测试test1和test2是否可以连通，如果不连通可以通过在容器内安装ping命令 然后更新容器再提交
    到镜像内的操作来实现将ping加入镜像。
        - apt install iputils-ping
        - apt-get update 更新容器
        - 退出容器执行一下命令：提交更新到新的镜像
            - docker commit -m="install ping cmd" -a="cyb" af27bd1a10f9 ubuntu:cyb
        - docker rm 容器id  来删除容器
    - 4 运行新的容器
        - docker run -itd --name test1 --network test-net ubuntu:cyb /bin/bash
        - docker run -itd --name test2 --network test-net ubuntu:cyb /bin/bash
        
    - 5 ping 
        - root@0cacc9157679:/# ping test2
          PING test2 (172.18.0.3) 56(84) bytes of data.
          64 bytes from test2.test-net (172.18.0.3): icmp_seq=1 ttl=64 time=0.365 ms
          64 bytes from test2.test-net (172.18.0.3): icmp_seq=2 ttl=64 time=0.237 ms
          64 bytes from test2.test-net (172.18.0.3): icmp_seq=3 ttl=64 time=0.218 ms
          64 bytes from test2.test-net (172.18.0.3): icmp_seq=4 ttl=64 time=0.181 ms
        - root@d442abefa6a5:/# ping test1 
          PING test1 (172.18.0.2) 56(84) bytes of data.
          64 bytes from test1.test-net (172.18.0.2): icmp_seq=1 ttl=64 time=0.172 ms
          64 bytes from test1.test-net (172.18.0.2): icmp_seq=2 ttl=64 time=0.119 ms

    - 6 如果你有多个容器之间需要互相连接，推荐使用 Docker Compose

## 配置DNS
- 在mac上的docker app上的启动参数上加入如下的dns来设置全部容器的DNS
```
{
  "registry-mirrors": [
    "https://hub-mirror.c.163.com"
  ],
  "dns" : [
    "114.114.114.114",
    "8.8.8.8"
  ],
  "experimental": false,
  "features": {
    "buildkit": true
  }
}
```

- 在容器内执行 cat etc/resolv.conf 观察对应的DNS配置
    - docker run -it --rm  ubuntu  cat etc/resolv.conf
    
 
- 手动指定容器的配置
    - docker run -it --rm -h host_ubuntu  --dns=114.114.114.114 --dns-search=test.com ubuntu
        - --rm：容器退出时自动清理容器内部的文件系统。
        - -h HOSTNAME 或者 --hostname=HOSTNAME： 设定容器的主机名，它会被写到容器内的 /etc/hostname 和 /etc/hosts。
        - --dns=IP_ADDRESS： 添加 DNS 服务器到容器的 /etc/resolv.conf 中，让容器用这个服务器来解析所有不在 /etc/hosts 中的主机名。
        - --dns-search=DOMAIN： 设定容器的搜索域，当设定搜索域为 .example.com 时，在搜索一个名为 host 的主机时，DNS 不仅搜索 host，还会搜索 host.example.com。       