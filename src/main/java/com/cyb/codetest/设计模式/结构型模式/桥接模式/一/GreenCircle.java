package com.cyb.codetest.设计模式.结构型模式.桥接模式.一;

/**
 * @author cyb
 * @date 2022/9/9 下午7:59
 */
public class GreenCircle implements DrawAPI {
    @Override
    public void drawCircle(int radius, int x, int y) {
        System.out.println("Drawing Circle[ color: green, radius: "
                + radius +", x: " +x+", "+ y +"]");
    }
}
