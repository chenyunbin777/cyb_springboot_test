package com.cyb.codetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

//去掉数据源
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
////使@Scheduled的定时任务注解， 定时任务生效
//@EnableScheduling
//@AutoConfigurationPackage
@SpringBootApplication
public class CodetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodetestApplication.class, args);
    }

}
