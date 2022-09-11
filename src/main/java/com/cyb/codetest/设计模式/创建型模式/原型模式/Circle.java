package com.cyb.codetest.设计模式.创建型模式.原型模式;

/**
 * @author cyb
 * @date 2022/9/9 下午4:44
 */
public class Circle extends Shape {

    public Circle(){
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
