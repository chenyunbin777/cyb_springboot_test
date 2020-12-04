package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为-不支持事务 有事务A则抛异常
 * @author cyb
 * @date 2020/12/04 4:05 下午
 */
public class TestTransation_NEVER {

    /**

     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void methodA() {
        doSomeThingA();
        methodB();
        doSomeThingB();
    }
    @Transactional(propagation = Propagation.NEVER)
    public void methodB() {

    }

    public static void main(String[] args) {

    }

    private void doSomeThingB() {
    }

    private void doSomeThingA() {
    }
}
