package com.cyb.codetest.设计模式.创建型模式.原型模式;

/**
 * @author cyb
 * @date 2022/9/9 下午4:43
 */
public class Square  extends Shape{
    public Square(){
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}
