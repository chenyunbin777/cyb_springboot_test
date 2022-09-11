package com.cyb.codetest.设计模式.创建型模式.建造者模式;

/**
 * 蔬菜汉堡
 * @author cyb
 * @date 2022/9/9 下午5:15
 */
public class VegBurger extends Burger {
    @Override
    public String name() {
        return "Veg Burger";
    }

    @Override
    public float price() {
        return 25.0f;
    }
}
