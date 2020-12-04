package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为-不存在则创建事务，如果存在则使用当前事务REQUIRED
 * @author cyb
 * @date 2020/12/04 4:05 下午
 */
public class TestTransation_REQUIRED {

    /**
        在methodA中执行methodB  methodA开启了事务 那么执行methodB时也就加入进了methodA的事务

     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void methodA() {
        doSomeThingA();
        methodB();
        doSomeThingB();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void methodB() {

    }

    public static void main(String[] args) {

    }

    private void doSomeThingB() {
    }

    private void doSomeThingA() {
    }
}
