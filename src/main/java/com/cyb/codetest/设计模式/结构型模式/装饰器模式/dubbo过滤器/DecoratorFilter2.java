//package com.cyb.codetest.设计模式.结构型模式.装饰器模式.dubbo过滤器;
//
///**
// * @author cyb
// * @date 2021/2/15 4:56 下午
// */
//public class DecoratorFilter2 extends AbstractInvokerDecorator implements FilterInterface {
//
//    public DecoratorFilter2(InvokerInterface invoker) {
//        super(invoker);
//    }
//
//    @Override
//    public void invoke() {
//        filter();
//        invoker.invoke();
//
//    }
//
//    @Override
//    public void filter() {
//        System.out.println("实现Filter2过滤器");
//    }
//}
