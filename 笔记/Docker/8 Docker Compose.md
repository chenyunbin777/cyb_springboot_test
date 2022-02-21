## Compose
- 用来定义和运行多容器DOcker的应用程序工具通过Compose 可以使用YML文件来进行配置应用程序需要的所有的服务。
然后 使用一个命令就可以从YML文件中创建并且启动所有的服务
- Compose 使用的三个步骤：
    - 使用 Dockerfile 定义应用程序的环境。
    - 使用 docker-compose.yml 定义构成应用程序的服务，这样它们可以在隔离环境中一起运行。
    - 最后，执行 docker-compose up 命令来启动并运行整个应用程序。