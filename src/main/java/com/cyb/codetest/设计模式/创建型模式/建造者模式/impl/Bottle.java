package com.cyb.codetest.设计模式.创建型模式.建造者模式.impl;

import com.cyb.codetest.设计模式.创建型模式.建造者模式.Packing;

/**
 * @author cyb
 * @date 2022/9/9 下午5:11
 */
public class Bottle implements Packing {

    @Override
    public String pack() {
        return "Bottle";
    }
}
