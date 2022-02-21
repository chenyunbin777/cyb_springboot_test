# Docker 镜像使用

- 1 列出镜像

chenyunbin@chenyunbindeMacBook-Pro ~ % docker images
REPOSITORY        TAG       IMAGE ID       CREATED        SIZE
ubuntu            latest    c29284518f49   7 days ago     72.8MB
centos            latest    300e315adb2f   7 months ago   209MB
ubuntu            15.10     9b9cb95443b5   4 years ago    137MB
training/webapp   latest    6fae60ef3446   6 years ago    349MB

- 各个选项说明:
    - REPOSITORY：表示镜像的仓库源
    - TAG：镜像的标签
    - IMAGE ID：镜像ID
    - CREATED：镜像创建时间
    - SIZE：镜像大小
    
    
    
- 如果指定镜像的版本就是用该版本，如果不指定就会使用最新的版本
    - docker run -t -i ubuntu:15.10 /bin/bash 
    - docker run -t -i ubuntu: /bin/bash  使用最新版本latest
    
    
- 2 下载镜像
    - 通过 docker pull 对应的镜像:版本号
    
    
- 3 查找镜像
    - docker search 镜像的名字，如httpd centos unbuntu。会列出各个版本的镜像
    - docker search centos
    NAME                              DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
    centos                            The official build of CentOS.                   6645      [OK]       
    ansible/centos7-ansible           Ansible on Centos7                              134                  [OK]
    consol/centos-xfce-vnc            Centos container with "headless" VNC session…   129                  [OK]
    jdeathe/centos-ssh                OpenSSH / Supervisor / EPEL/IUS/SCL Repos - …   118                  [OK]
    centos/systemd                    systemd enabled base container.                 100                  [OK]
   
    - NAME: 镜像仓库源的名称
   DESCRIPTION: 镜像的描述
   OFFICIAL: 是否 docker 官方发布
   stars: 类似 Github 里面的 star，表示点赞、喜欢的意思。
   AUTOMATED: 自动构建。
   
- 4 拖取镜像 docker run httpd
- 5 删除镜像 
    - 镜像删除使用 docker rmi 命令，比如我们删除 hello-world 镜像：
    - $ docker rmi hello-world
- 6 创建镜像
    当我们从 docker 镜像仓库中下载的镜像不能满足我们的需求时，我们可以通过以下两种方式对镜像进行更改。
    - 1、更新镜像：从已经创建的容器中更新镜像，并且提交这个镜像
        - 在运行的容器内使用 apt-get update 命令进行更新。
        - docker commit -m="has update" -a="runoob" e218edb10161 runoob/ubuntu:v2
            - 各个参数说明：
                - -m: 提交的描述信息
                - -a: 指定镜像作者
                - e218edb10161：容器 ID
                - runoob/ubuntu:v2: 指定要创建的目标镜像名
        
    - 2、使用 Dockerfile 指令来创建一个新的镜像
    - docker build -t runoob/centos:6.7 / 
        - -t ：指定要创建的目标镜像名
        - 后面跟着对应的Dockerfile文件的路径 可以使用绝对路径
        
    - docker images 可以查看镜像
    REPOSITORY        TAG       IMAGE ID       CREATED         SIZE
    runoob/centos     6.7       2da7d541165e   5 minutes ago   191MB
    ubuntu            latest    c29284518f49   8 days ago      72.8MB
    centos            latest    300e315adb2f   7 months ago    209MB
    ubuntu            15.10     9b9cb95443b5   5 years ago     137MB
    training/webapp   latest    6fae60ef3446   6 years ago     349MB
    
    - docker run -itd runoob/centos /bin/bash 在后台运行对应的镜像
    
    - docker exec -it ed8fdc274844 /bin/bash   通过exec的方式来进入容器
    - [root@ed8fdc274844 /]# id runoob
        - uid=500(runoob) gid=500(runoob) groups=500(runoob)已经包含了我们创建的用户
    - docker tag 2da7d541165e runoob/centos:dev     
        -  **这里的是image镜像的id 不是容器的id**  这里就创建了一个tag dev
        - REPOSITORY        TAG       IMAGE ID       CREATED          SIZE
          runoob/centos     6.7       2da7d541165e   24 minutes ago   191MB
          runoob/centos     dev       2da7d541165e   24 minutes ago   191MB