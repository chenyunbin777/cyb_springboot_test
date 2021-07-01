package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事务传播行为-嵌套事务NESTED
 *
 * @author cyb
 * @date 2020/12/04 4:05 下午
 */
public class TestTransation_NESTED {

    /**
     * 如果一个活动的事务存在，则运行在一个嵌套的事务中。 如果没有活动事务, 则按TransactionDefinition.PROPAGATION_REQUIRED 属性执行。
     * 这是一个嵌套事务,使用JDBC 3.0驱动时,仅仅支持DataSourceTransactionManager作为事务管理器。
     * 需要JDBC 驱动的java.sql.Savepoint类。使用PROPAGATION_NESTED，还需要把PlatformTransactionManager的nestedTransactionAllowed属性设为true(属性值默认为false)。
     * <p>
     * 这里关键是嵌套执行。
     * <p>
     * 嵌套事务一个非常重要的概念就是内层事务依赖于外层事务。外层事务失败时，会回滚内层事务所做的动作。而内层事务操作失败并不会引起外层事务的回滚。
     * <p>
     * PROPAGATION_NESTED 与PROPAGATION_REQUIRES_NEW的区别：
     * 由此可见, PROPAGATION_REQUIRES_NEW 和 PROPAGATION_NESTED 的最大区别在于, PROPAGATION_REQUIRES_NEW 完全是一个新的事务,
     * 而 PROPAGATION_NESTED 则是外部事务的子事务, 如果外部事务 commit, 嵌套事务也会被 commit, 这个规则同样适用于 roll back.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void methodA() {
        doSomeThingA();
        methodB();
        doSomeThingB();
    }

    @Transactional(propagation = Propagation.NESTED)
    public void methodB() {

    }

    public static void main(String[] args) {

    }

    private void doSomeThingB() {
    }

    private void doSomeThingA() {
    }
}
