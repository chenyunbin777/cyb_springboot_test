package com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.decorator;

import com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.Shape;

/**
 * @author cyb
 * @date 2022/9/9 下午4:13
 */
public class BlueShapeDecorator extends ShapeDecorator {


    public BlueShapeDecorator(Shape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        setBlueBorder(decoratedShape);
        decoratedShape.draw();
//        setBlueBorder(decoratedShape);
    }

    /**
     * RedShapeDecorator提供额外的装饰功能
     * @param decoratedShape
     */
    private void setBlueBorder(Shape decoratedShape){

        System.out.println("Border Color: Blue");
    }
}
