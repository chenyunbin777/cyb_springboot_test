package com.cyb.codetest.设计模式.结构型模式.享元模式_减少对象创建_池;

import java.util.HashMap;

/**
 * @author cyb
 * @date 2022/9/10 下午2:51
 */
public class ShapeFactory {
    private static final HashMap<String, Shape> circleMap = new HashMap<>();

    public static Shape getCircle(String color) {
        Circle circle = (Circle)circleMap.get(color);

        if(circle == null) {
            circle = new Circle(color);
            circleMap.put(color, circle);
            System.out.println("Creating circle of color : " + color);
        }
        return circle;
    }
}
