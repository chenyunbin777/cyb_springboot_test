package com.cyb.codetest.设计模式.结构型模式.桥接模式.燕窝种类与销售渠道;

/**
 * 商品抽象类
 *
 * @author cyb
 * @date 2022/9/9 下午8:41
 */
public abstract class Product {

    /**
     * 这个channel接口就是与Product的桥梁
     */
    protected Channel channel;

    public Product(Channel channel) {

        this.channel = channel;
    }

    /**
     * 销售抽象方法
     */
    public abstract void sell();


}
