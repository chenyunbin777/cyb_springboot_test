# SpringBoot的数据源
- Spring Boot默认的数据源是org.apache.tomcat.jdbc.pool.DataSource
- 通常使用com.alibaba.druid.pool.DruidDataSource
- 好处
1）配置 web.xml，加载 Spring 和 Spring mvc
2）配置数据库连接、配置 Spring 事务
3）配置加载配置文件的读取，开启注解
4）配置日志文件


使用 Spring Boot 可以非常方便、快速搭建项目，使我们不用关心框架之间的兼容性，适用版本等各种问题，
我们想使用任何东西，仅仅添加一个配置就可以，所以使用 Spring Boot 非常适合构建微服务。