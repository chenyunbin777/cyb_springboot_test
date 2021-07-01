package com.cyb.codetest.设计模式.模板模式;

/**
 * @author cyb
 * @date 2021/2/15 6:43 下午
 */
public class InstanceClass extends TemplatePatten {
    @Override
    void init() {
        System.out.println("初始化");
    }

    @Override
    void start() {
        System.out.println("开始");
    }

    @Override
    void end() {
        System.out.println("结束");
    }
}
