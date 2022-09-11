package com.cyb.codetest.设计模式.创建型模式.建造者模式;

import com.cyb.codetest.设计模式.创建型模式.建造者模式.impl.Bottle;

/**
 * 冷饮
 * @author cyb
 * @date 2022/9/9 下午5:14
 */
public abstract class ColdDrink implements Item {

    @Override
    public Packing packing() {
        return new Bottle();
    }

    @Override
    public abstract float price();
}