package com.cyb.codetest.设计模式.结构型模式.外观模式;

/**
 * 创建一个外观类。
 * 这种模式涉及到一个单一的类，该类提供了客户端请求的简化方法和对现有系统类方法的委托调用。
 * @author cyb
 * @date 2022/9/9 下午9:25
 */
public class ShapeMaker {

    private Shape circle;
    private Shape rectangle;
    private Shape square;

    public ShapeMaker() {
        circle = new Circle();
        rectangle = new Rectangle();
        square = new Square();
    }

    public void drawCircle(){
        circle.draw();
    }
    public void drawRectangle(){
        rectangle.draw();
    }
    public void drawSquare(){
        square.draw();
    }
}
