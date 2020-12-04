package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为-新创建一个事务B，如果存在事务A则挂起当前事务A，当B执行完成之后 才会去继续执行A
 * @author cyb
 * @date 2020/12/04 4:05 下午
 */
public class TestTransation_REQUIRES_NEW {

    /**
        在methodA中执行methodB  methodA开启了事务 那么执行methodB时挂起当前事务A，新创建一个事务B，执行完B之后再执行A
        注意 A B事务互不影响，如果A事务执行失败并不影响事务B的执行

     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void methodA() {
        doSomeThingA();
        methodB();
        doSomeThingB();
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void methodB() {

    }

    public static void main(String[] args) {

    }

    private void doSomeThingB() {
    }

    private void doSomeThingA() {
    }
}
