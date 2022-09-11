package com.cyb.codetest.设计模式.结构型模式.桥接模式.燕窝种类与销售渠道;

/**
 * 渠道：抖音 淘宝等
 * @author cyb
 * @date 2022/9/9 下午8:42
 */
public interface Channel {


    void sell(String productName);
}
