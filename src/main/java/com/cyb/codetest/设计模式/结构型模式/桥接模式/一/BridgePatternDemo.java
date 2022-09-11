package com.cyb.codetest.设计模式.结构型模式.桥接模式.一;

/**
 *
 * 实体：圆，颜色，具体画什么颜色由具体的Circle参数决定
 * 增加颜色只需要新增DrawAPI的实现类即可
 *
 *
 * @author cyb
 * @date 2022/9/9 下午8:06
 */
public class BridgePatternDemo {

    public static void main(String[] args) {
        Shape redCircle = new Circle(100,100, 10, new RedCircle());
        Shape greenCircle = new Circle(100,100, 10, new GreenCircle());

        redCircle.draw();
        greenCircle.draw();
    }
}
