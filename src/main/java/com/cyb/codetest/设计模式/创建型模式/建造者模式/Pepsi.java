package com.cyb.codetest.设计模式.创建型模式.建造者模式;

/**
 * 冷-百世可乐
 * @author cyb
 * @date 2022/9/9 下午5:18
 */
public class Pepsi extends ColdDrink {

    @Override
    public float price() {
        return 35.0f;
    }

    @Override
    public String name() {
        return "Pepsi";
    }
}