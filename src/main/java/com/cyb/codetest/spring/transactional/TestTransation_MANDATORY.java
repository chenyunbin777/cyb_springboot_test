package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为-支持事务 如果没后事务则抛异常
 * 则会抛出异常throw new IllegalTransactionStateException(“Transaction propagation ‘mandatory’ but no existing transaction found”);
 *
 * @author cyb
 * @date 2020/12/04 4:05 下午
 */
public class TestTransation_MANDATORY {

    /**
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void methodA() {
        doSomeThingA();
        methodB();
        doSomeThingB();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void methodB() {

    }

    public static void main(String[] args) {

    }

    private void doSomeThingB() {
    }

    private void doSomeThingA() {
    }
}
