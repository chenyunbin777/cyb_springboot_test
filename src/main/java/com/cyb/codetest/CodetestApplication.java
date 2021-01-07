package com.cyb.codetest;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//去掉数据源
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDubbo
public class CodetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodetestApplication.class, args);
    }

}
