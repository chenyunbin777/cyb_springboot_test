package com.cyb.codetest.设计模式.结构型模式.桥接模式.燕窝种类与销售渠道;

/**
 * @author cyb
 * @date 2022/9/9 下午8:47
 */
public class ProductAppleWatch extends Product{


    public ProductAppleWatch(Channel channel) {
        super(channel);
    }

    @Override
    public void sell() {
        channel.sell("appleWatch");
    }
}
