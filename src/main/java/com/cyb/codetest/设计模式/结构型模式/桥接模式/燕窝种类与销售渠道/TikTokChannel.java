package com.cyb.codetest.设计模式.结构型模式.桥接模式.燕窝种类与销售渠道;

/**
 * @author cyb
 * @date 2022/9/9 下午8:44
 */
public class TikTokChannel implements Channel {


    @Override
    public void sell(String productName) {
        System.out.println("抖音专卖："+productName);
    }
}
