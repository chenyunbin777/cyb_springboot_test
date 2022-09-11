package com.cyb.codetest.设计模式.创建型模式.建造者模式;

import com.cyb.codetest.设计模式.创建型模式.建造者模式.impl.Wrapper;

/**
 *
 * 汉堡实现食物接口
 * @author cyb
 * @date 2022/9/9 下午5:12
 */
public abstract class Burger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();
}
