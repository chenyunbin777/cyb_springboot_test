package com.cyb.codetest.设计模式.结构型模式.装饰器模式.一;

import com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.decorator.BlueShapeDecorator;
import com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.decorator.RedShapeDecorator;
import com.cyb.codetest.设计模式.结构型模式.装饰器模式.一.decorator.ShapeDecorator;

/**
 * @author cyb
 * @date 2022/9/9 下午4:15
 */
public class DecoratorPatternDemo {


    public static void main(String[] args) {

        Shape circle = new Circle();
        ShapeDecorator redCircle = new RedShapeDecorator(new Circle());
        ShapeDecorator redRectangle = new RedShapeDecorator(new Rectangle());
        //Shape redCircle = new RedShapeDecorator(new Circle());
        //Shape redRectangle = new RedShapeDecorator(new Rectangle());
//        System.out.println("Circle with normal border");
//        circle.draw();
//
//        System.out.println("\nCircle of red border");
//        redCircle.draw();
//
//        System.out.println("\nRectangle of red border");
//        redRectangle.draw();


        //多层装饰，
        ShapeDecorator blueRedCircle = new BlueShapeDecorator(redCircle);
        blueRedCircle.draw();

    }
}
