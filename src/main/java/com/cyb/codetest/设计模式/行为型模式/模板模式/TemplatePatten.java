package com.cyb.codetest.设计模式.行为型模式.模板模式;

/**
 * 优点： 1、封装不变部分，扩展可变部分。 2、提取公共代码，便于维护。 3、行为由父类控制，子类实现。
 * 缺点：每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大。
 * 使用场景： 1、有多个子类共有的方法，且逻辑相同。 2、重要的、复杂的方法，可以考虑作为模板方法。
 * 注意事项：为防止恶意操作，一般模板方法都加上 final 关键词。
 * 模板类
 *
 * @author cyb
 * @date 2021/2/15 6:40 下午
 */
public abstract class TemplatePatten {

    abstract void init();

    abstract void start();

    abstract void end();


    //模板：模板方法设置为 final，这样它就不会被重写
    public final void template() {
        init();
        start();
        end();

    }
}
