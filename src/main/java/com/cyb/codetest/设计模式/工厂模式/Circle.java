package com.cyb.codetest.设计模式.工厂模式;

/**
 * @author cyb
 * @date 2021/1/1 11:44 下午
 */
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
