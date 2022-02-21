#springboot + docker
- 1 首先我们需要创建一个springboot项目

- 2 我们直接使用idea自带的工具maven package进行打包
    - 对应的jar会生成在/Users/chenyunbin/Documents/我的idea项目/dockertest/target目录下

- 3 我们创建一个文件夹存放我们生成的jar包和Dockfile文件
```
# Docker image for springboot file run
# VERSION 0.0.1
# Author: eangulee
# 基础镜像使用java
FROM java:8
# 作者
MAINTAINER eangulee <eangulee@gmail.com>
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp 
# 将jar包添加到容器中并更名为app.jar
ADD dockertest-0.0.1-SNAPSHOT.jar app.jar 
# 运行jar包
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

```

- 4 通过Dockerfile来构建我们的镜像
    - docker build -t （指定我们的镜像） 指定Docker的上下文路径（需要copy到docker引擎，默认是Dockfile路径）
    - docker build -t springbootdemo4docker .
    - 注意我们需要在Dockerfile所在的目录执行命令。 
- 5 通过镜像来启动我们的容器
    - docker run -d -p 8080:8080 容器名字
    - docker run -d -p 8080:8080 springbootdemo4docker
    - 这里注意一下： 第一个port是我们外部访问的port，第二个port就是容器使用的port，也就是springboot项目启动的port。
    不要弄混了
    
- 6 然后我们可以通过：localhost:8080/docker/hello 来访问对应的springboot接口,就跟在本地一样。
    