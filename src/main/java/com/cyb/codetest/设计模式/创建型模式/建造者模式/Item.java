package com.cyb.codetest.设计模式.创建型模式.建造者模式;

/**
 * 食物条目
 *
 * @author cyb
 * @date 2022/9/9 下午5:09
 */
public interface Item {
    /**
     * 食物的名字
     * @return
     */
    String name();

    /**
     * 食物的包装
     *
     * @return
     */
    Packing packing();

    /**
     * 食物的价格
     *
     * @return
     */
    float price();
}
