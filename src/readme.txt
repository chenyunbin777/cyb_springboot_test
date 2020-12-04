IDEA中创建springboot项目，在java中右键没有创建class的选项,修改后注解报错
问题1：在IDEA中右键没有创建class类的选项
        解决方法：右键java目录，mark directory as，选择sources

问题2：在执行了步骤1以后，发现启动类注解报错，无法引入包

       解决方法：右键pom文件，run maven，选择reimport