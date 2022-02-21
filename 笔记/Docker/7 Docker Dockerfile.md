# Docker Dockerfile
## 什么是 Dockerfile？
- Dockerfile 是一个用来构建镜像的文本文件，文本内容包含了一条条构建镜像所需的指令和说明。


## 使用 Dockerfile 定制镜像

- 1、下面以定制一个 nginx 镜像（构建好的镜像内会有一个 /usr/share/nginx/html/index.html 文件）
在一个空目录下，新建一个名为 Dockerfile 文件，并在文件内添加以下内容：
```
FROM nginx
RUN echo '这是一个本地构建的nginx镜像' > /usr/share/nginx/html/index.html

```

- 2、FROM 和 RUN 指令的作用

    - FROM：定制的镜像都是基于 FROM 的镜像，这里的 nginx 就是定制需要的**基础镜像**。后续的操作都是基于 nginx。
    - RUN：用于执行后面跟着的命令行命令。
## 开始构建镜像 
在 Dockerfile 文件的存放目录下，执行构建动作。
以下示例，通过目录下的 Dockerfile 构建一个 nginx:v3（镜像名称:镜像标签）。
注：最后的 . 代表本次执行的上下文路径，下一节会介绍。
- docker build -t nginx:v3 .

## 上下文路径
- 上一节中，有提到指令最后一个 . 是上下文路径，那么什么是上下文路径呢？
    - $ docker build -t nginx:v3 .
    - 上下文路径，是指 docker 在构建镜像，有时候想要使用到本机的文件（比如复制），docker build 命令得知这个路径后，会将路径下的所有内容打包。
    -解析：由于 docker 的运行模式是 C/S。我们本机是 C，docker 引擎是 S。实际的构建过程是在 docker 引擎下完成的，所以这个时候无法用到我们本机的文件。
    这就需要把我们本机的指定目录下的文件一起打包提供给 docker 引擎使用。如果未说明最后一个参数，那么默认上下文路径就是 Dockerfile 所在的位置。
    - 注意：上下文路径下不要放无用的文件，因为会一起打包发送给 docker 引擎，如果文件过多会造成过程缓慢。