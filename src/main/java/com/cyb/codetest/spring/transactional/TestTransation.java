package com.cyb.codetest.spring.transactional;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cyb
 * @date 2021/1/3 6:09 下午
 */
//propagation 事务传播行为
// isolation 隔离级别
// rollbackFor 抛异常Exception回归
@Transactional(propagation = Propagation.REQUIRED,
        isolation = Isolation.REPEATABLE_READ,
        rollbackFor = Exception.class
)
public class TestTransation {
}
