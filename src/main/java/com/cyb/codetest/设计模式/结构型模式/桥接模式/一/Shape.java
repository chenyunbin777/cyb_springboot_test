package com.cyb.codetest.设计模式.结构型模式.桥接模式.一;

/**
 * 使用 DrawAPI 接口创建抽象类 Shape。
 * @author cyb
 * @date 2022/9/9 下午8:00
 */
public abstract class Shape {

    protected DrawAPI drawAPI;
    protected Shape(DrawAPI drawAPI){
        this.drawAPI = drawAPI;
    }

    /**
     * 抽象类抽象具体的公共实现方法，具体的实现交给继承的类
     */
    public abstract void draw();
}
