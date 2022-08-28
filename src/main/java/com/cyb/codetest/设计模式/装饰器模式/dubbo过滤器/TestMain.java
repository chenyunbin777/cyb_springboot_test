package com.cyb.codetest.设计模式.装饰器模式.dubbo过滤器;

/**
 * 意图：动态地给一个对象添加一些额外的职责。就增加功能来说，装饰器模式相比生成子类更为灵活。
 * 主要解决：一般的，我们为了扩展一个类经常使用继承方式实现，由于继承为类引入静态特征，并且随着扩展功能的增多，子类会很膨胀。
 * 何时使用：在不想增加很多子类的情况下扩展类。
 * 1、扩展一个类的功能。 2、动态增加功能，动态撤销。
 * <p>
 * 与代理模式主要的区别
 * 1、和适配器模式的区别：适配器模式主要改变所考虑对象的接口，而代理模式不能改变所代理类的接口。
 * 2、和装饰器模式的区别：装饰器模式为了增强功能，而代理模式是为了加以控制。
 *
 * @author cyb
 * @date 2021/2/15 4:57 下午
 */
public class TestMain {

    public static void main(String[] args) {
        InvokerInterface invoker = new Invoker();
        AbstractInvokerDecorator abstractDecorator1 = new DecoratorFilter1(invoker);
        abstractDecorator1.invoke();
        System.out.println("---------------");
        AbstractInvokerDecorator abstractInvokerDecorator2 = new DecoratorFilter2(new DecoratorFilter1(new Invoker()));
        abstractInvokerDecorator2.invoke();

        System.out.println("---------------");
        AbstractInvokerDecorator abstractInvokerDecorator3 = new DecoratorFilter3(new DecoratorFilter2(new DecoratorFilter1(new Invoker())));
        abstractInvokerDecorator3.invoke();


    }
}
