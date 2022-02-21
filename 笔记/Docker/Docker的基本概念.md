# 


- 镜像地址：https://hub.docker.com/
账号
dockerchen777 密码：正常

https://www.cnblogs.com/misswangxing/p/10669444.html
Docker本身并不是容器，它是创建容器的工具，是应用容器引擎。
- 想要搞懂Docker，其实看它的两句口号就行。
    - 第一句，是“Build, Ship and Run”。也就是，“搭建、发送、运行”，三板斧。
        - 个人理解：就是你可以把建造好的容器搬到任何的地方去运行，也就是通常说的docker的一个镜像的容器。
    - Docker的第二句口号就是：“Build once，Run anywhere（搭建一次，到处能用）”。也就是说我只需要搭建一次容器就可以搬到任何地方去运行。
- Docker技术的三大核心概念，分别是：  
    - 容器（Container）：容器就是原始文件
    - 镜像（Image）：就是一个容器的镜像
    - 仓库（Repository）：保存镜像的仓库
    
- 就在Docker容器技术被炒得热火朝天之时，大家发现，如果想要将Docker应用于具体的业务实现，是存在困难的——
编排、管理和调度等各个方面，都不容易。于是，人们迫切需要一套管理系统，对Docker及容器进行更高级更灵活的管理。


- k8s是用来管理容器的，管理docker的。
    