# Docker 容器使用

## 命令的学习
- 可以通过命令 docker command --help 更深入的了解指定的 Docker 命令使用方法。

## 容器的使用
- 获取镜像:docker pull centos/unbuntu 会载入对应的系统的镜像
- 启动容器： docker run -it centos /bin/bash
    - -i: 交互式操作。
    - -t: 终端。
    - ubuntu: ubuntu 镜像。
    - /bin/bash：放在镜像名后的是命令，这里我们希望有个交互式 Shell，因此用的是 /bin/bash。
    
    
- 后台运行
```
docker run -itd --name ubuntu-test ubuntu /bin/bash
docker ps
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS          PORTS     NAMES
a1d0e1a4364d   ubuntu         "/bin/bash"              3 minutes ago    Up 3 minutes              ubuntu-test

docker exec -it  a1d0e1a4364d /bin/bash     进入对应的容器
```

- 在使用 -d 参数时，容器启动后会进入后台。此时想要进入容器，可以通过以下指令进入：
    - docker attach
    - docker exec：推荐大家使用 docker exec 命令，因为此退出容器终端，不会导致容器的停止。

- 删除容器使用 docker rm 命令
    - docker rm f 容器id
    
    
# 使用容器构建一个web应用容器
- 1 下载镜像
    -  docker pull training/webapp
- 2 运行容器
    - docker run -d -P training/webapp python app.py

- 3 查看对应的port映射关系
    -  docker ps
    CONTAINER ID   IMAGE             COMMAND           CREATED         STATUS         PORTS                     NAMES
    ceb670ff0d5c   training/webapp   "python app.py"   7 minutes ago   Up 7 minutes   0.0.0.0:55000->5000/tcp   festive_chatelet
    - Docker 开放了 5000 端口（默认 Python Flask 端口）映射到主机端口 55000 上。
        也就是容器内部的55000端口映射到我们本地主机的5000端口上
        这时我们可以通过浏览器访问WEB应用 : http://localhost:55000/
        
- 4 查看 WEB 应用程序日志
    - docker logs -f ceb670ff0d5c   类似于 tail -f的使用方式

- 5 docker stop 容器name

- 6 docker start 容器name 重启容器
- 7 docker rm 容器name 移除容器