package com.cyb.codetest.设计模式.创建型模式.建造者模式;

/**
 * 鸡肉汉堡
 * @author cyb
 * @date 2022/9/9 下午5:16
 */
public class ChickenBurger  extends Burger {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }
}
