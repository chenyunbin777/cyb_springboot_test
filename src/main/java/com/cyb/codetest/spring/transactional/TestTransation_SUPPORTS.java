package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为-有事务执行 没有事务就按无事务执行
 *
 * @author cyb
 * @date 2020/12/04 4:05 下午
 */
public class TestTransation_SUPPORTS {

    /**
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void methodA() {
        doSomeThingA();
        methodB();
        doSomeThingB();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void methodB() {

    }

    public static void main(String[] args) {

    }

    private void doSomeThingB() {
    }

    private void doSomeThingA() {
    }
}
