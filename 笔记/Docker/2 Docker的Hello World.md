# Docker Hello World

## 1 docker run 
```         
docker run ubuntu:15.10 /bin/echo "Hello world"

docker run centos:7.0 /bin/echo "Hello world"
```
- 各个参数解析：

    - docker: Docker 的二进制执行文件。

    - run: 与前面的 docker 组合来运行一个容器。

    - ubuntu:15.10 指定要运行的镜像，Docker 首先从本地主机上查找镜像是否存在，如果不存在，Docker 就会从镜像仓库 Docker Hub 下载公共镜像。

    - /bin/echo "Hello world": 在启动的容器里执行的命令

    - 以上命令完整的意思可以解释为：Docker 以 ubuntu15.10 镜像创建一个新容器，然后在容器里执行 bin/echo "Hello world"，然后输出结果。
    
## 2 创建一个虚拟的容器
```
chenyunbin@chenyunbindeMacBook-Pro ~ % docker run -it ubuntu:15.10 /bin/bash
root@8ebd21927ed8:/# cat /proc/version
Linux version 4.19.121-linuxkit (root@buildkitsandbox) (gcc version 9.2.0 (Alpine 9.2.0)) #1 SMP Tue Dec 1 17:50:32 UTC 2020
root@8ebd21927ed8:/# ls
bin  boot  dev  etc  home  lib  lib64  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
root@8ebd21927ed8:/#
```
- -t：在新容器内指定一个伪终端或终端
- -i：允许你对容器内的标准输入 (STDIN) 进行交互
- 直接可以将it放在一起， -it就行了
- 我们可以通过运行 exit 命令或者使用 CTRL+D 来退出容器。


## 3 后台模式运行docker容器
```
docker run -d ubuntu:15.10 /bin/sh -c "while true; do echo hello world; sleep 1; done"
b23840348e0f74ac0b353cd68450c3c3222d19764de2a184301dc59f7b7d1c8a
```

- 运行docker容器并且声称一个容器id
- 通过 docker ps来查看对应的容器，我们看到对应的容器ID缩写，我们使用缩写和完整的ID都可以查询对应的容器的信息
```
CONTAINER ID   IMAGE          COMMAND                  CREATED              STATUS              PORTS     NAMES
b23840348e0f   ubuntu:15.10   "/bin/sh -c 'while t…"   About a minute ago   Up About a minute             funny_easley
```
- STATUS状态有7种,也就是Docker容器的生命周期：
  
  created（已创建）
  restarting（重启中）
  running 或 Up（运行中）
  removing（迁移中）
  paused（暂停）
  exited（停止）
  dead（死亡）
- docker logs 容器ID/amazing_cori： 可以查看对应的容器日志
- docker stop 容器ID/amazing_cori：停止容器