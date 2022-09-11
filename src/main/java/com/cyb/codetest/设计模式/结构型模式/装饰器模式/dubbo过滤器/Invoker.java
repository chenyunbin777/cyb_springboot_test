package com.cyb.codetest.设计模式.结构型模式.装饰器模式.dubbo过滤器;

/**
 * @author cyb
 * @date 2021/2/15 5:02 下午
 */
public class Invoker implements InvokerInterface {

    @Override
    public void invoke() {
        System.out.println("实现dubbo Invoker调用");
    }
}
