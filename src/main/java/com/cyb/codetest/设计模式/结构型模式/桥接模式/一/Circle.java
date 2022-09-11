package com.cyb.codetest.设计模式.结构型模式.桥接模式.一;

/**
 * 创建实现了 Shape 抽象类的实体类。 想要化什么样的圆都可以调用
 * 桥接接口DrawAPI的变化并不会影响Circle，这样实现了接口与实现类的分离与解耦
 *
 * @author cyb
 * @date 2022/9/9 下午8:01
 */
public class Circle extends Shape  {


    private int x, y, radius;

    public Circle(int x, int y, int radius, DrawAPI drawAPI) {
        super(drawAPI);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
     * 重新Shape draw方法实现自己的逻辑，调用具体桥接接口的实现类的逻辑
     */
    public void draw() {
        drawAPI.drawCircle(radius,x,y);
    }
}
