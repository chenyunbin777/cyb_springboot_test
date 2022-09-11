package com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.decorator;

import com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.Shape;

/**
 * @author cyb
 * @date 2022/9/9 下午4:13
 */
public class RedShapeDecorator extends ShapeDecorator {


    public RedShapeDecorator(Shape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }

    /**
     * RedShapeDecorator提供额外的装饰功能
     * @param decoratedShape
     */
    private void setRedBorder(Shape decoratedShape){
        System.out.println("Border Color: Red");
    }
}
