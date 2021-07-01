package com.cyb.codetest.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 初始化配置文件信息的本质就是创建Configuration对象，将解析的xml数据封装到Configuration内部的属性中。
 * SqlSession是一个接口，它有两个实现类：DefaultSqlSession（默认）和SqlSessionManager（弃用，不做介绍）
 * SqlSession是MyBatis中用于和数据库交互的顶层类，通常将它与ThreadLocal绑定，一个会话使用一个SqlSession，并且在使用完毕后需要close。
 * <p>
 * <p>
 * 一条SQL语句就是一个MappedStatement
 *
 * @author cyb
 * @date 2021/1/21 3:53 下午
 */
public class MybatisTest {

    /**
     * 创建SqlSessionFactoryBuilder对象，调用build(inputstream)方法读取并解析配置文件，返回SqlSessionFactory对象
     * 由SqlSessionFactory创建SqlSession 对象，没有手动设置的话事务默认开启
     * 调用SqlSession中的api，传入Statement Id和参数，内部进行复杂的处理，最后调用jdbc执行SQL语句，封装结果返回。
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = factory.openSession();
        String name = "tom";
        List<String> list = sqlSession.selectList("com.cyb.codetest.mybatis.UserMapper", name);
    }

}
