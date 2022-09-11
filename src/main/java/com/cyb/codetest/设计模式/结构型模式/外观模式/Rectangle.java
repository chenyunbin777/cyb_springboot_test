package com.cyb.codetest.设计模式.结构型模式.外观模式;

/**
 * @author cyb
 * @date 2022/9/9 下午9:24
 */
public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Rectangle::draw()");
    }
}
