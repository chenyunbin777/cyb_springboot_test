package com.cyb.codetest.设计模式.装饰器模式.dubbo过滤器;

import org.springframework.stereotype.Service;

/**
 * 创建扩展了 AbstractInvokerDecorator 类的实体装饰类。
 *
 * @author cyb
 * @date 2021/3/16 9:09 下午
 */
@Service
public class DecoratorFilter1 extends AbstractInvokerDecorator implements FilterInterface {

    public DecoratorFilter1(InvokerInterface invoker) {
        super(invoker);
    }

    @Override
    public void invoke() {
        filter();
        invoker.invoke();
    }

    @Override
    public void filter() {
        System.out.println("实现Filter1过滤器");
    }
}
