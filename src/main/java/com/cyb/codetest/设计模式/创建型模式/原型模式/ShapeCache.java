package com.cyb.codetest.设计模式.创建型模式.原型模式;

import java.util.HashMap;

/**
 * 创建一个类，从数据库获取实体类，并把它们存储在一个 Hashtable 中。
 *
 * 这种模式是实现了一个原型接口，该接口用于创建当前对象的克隆。当直接创建对象的代价比较大时，则采用这种模式。
 * 例如，一个对象需要在一个高代价的数据库操作之后被创建。我们可以缓存该对象，在下一个请求时返回它的克隆，
 * 在需要的时候更新数据库，以此来减少数据库调用。
 * @author cyb
 * @date 2022/9/9 下午4:45
 */
public class ShapeCache {

    private static HashMap<String, Shape> shapeMap
            = new HashMap<>();

    public static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return (Shape) cachedShape.clone();
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    public static void loadCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(),rectangle);
    }
}
