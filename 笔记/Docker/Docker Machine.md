# Docker Machine
## 简介
- Docker Machine 是一种可以让您在虚拟主机上安装 Docker 的工具，并可以使用 docker-machine 命令来管理主机。
- Docker Machine 也可以集中管理所有的 docker 主机，比如快速的给 100 台服务器安装上 docker。


## 创建Docker Machine
- 1 需要安装docker-machine
```
MacOS
base=https://github.com/docker/machine/releases/download/v0.16.0 &&
  curl -L $base/docker-machine-$(uname -s)-$(uname -m) >/usr/local/bin/docker-machine &&
  chmod +x /usr/local/bin/docker-machine

```
- 2 在创建时报错，需要在安装virtualbox
    - --driver指定了创建机器的驱动类型
```
docker-machine create --driver virtualbox test
  Creating CA: /Users/chenyunbin/.docker/machine/certs/ca.pem
  Creating client certificate: /Users/chenyunbin/.docker/machine/certs/cert.pem
  Running pre-create checks...
  Error with pre-create check: "VBoxManage not found. Make sure VirtualBox is installed and VBoxManage is in the path"
  
```

- 3 之后执行创建还会报错
    - 解决办法 https://gist.github.com/leannenorthrop/ef484e65c864ee2e4aa4de71c0639489
    - curl -Lo ~/.docker/machine/cache/boot2docker.iso https://github.com/boot2docker/boot2docker/releases/download/v1.9.1/boot2docker.iso
    - 创建一个default 虚拟机： 
        - docker-machine create --driver virtualbox default
```
chenyunbin@chenyunbindeMacBook-Pro springboottest % docker-machine create --driver virtualbox test
Running pre-create checks...
(test) Image cache directory does not exist, creating it at /Users/chenyunbin/.docker/machine/cache...
(test) No default Boot2Docker ISO found locally, downloading the latest release...
Error with pre-create check: "failure getting a version tag from the Github API response (are you getting rate limited by Github?)"

```
    
    
  