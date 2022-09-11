package com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.decorator;

import com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.Shape;

/**
 * 实现类Shape接口的抽象装饰器类， 只是使用的draw方法的模板而已
 * @author cyb
 * @date 2022/9/9 下午4:09
 */
public abstract class ShapeDecorator implements Shape {

    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape){
        this.decoratedShape = decoratedShape;
    }

    public void draw(){
        decoratedShape.draw();
    }
}
