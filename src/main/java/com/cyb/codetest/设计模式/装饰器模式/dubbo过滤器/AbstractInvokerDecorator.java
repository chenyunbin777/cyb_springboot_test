package com.cyb.codetest.设计模式.装饰器模式.dubbo过滤器;

/**
 * @author cyb
 * @date 2021/2/15 4:53 下午
 */
public abstract class AbstractInvokerDecorator implements InvokerInterface {

    protected InvokerInterface invoker;

    public AbstractInvokerDecorator(InvokerInterface invoker) {
        this.invoker = invoker;
    }

    @Override
    public void invoke() {
        invoker.invoke();
    }
}
