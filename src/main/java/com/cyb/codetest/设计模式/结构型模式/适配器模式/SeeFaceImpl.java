package com.cyb.codetest.设计模式.结构型模式.适配器模式;

/**
 * @author cyb
 * @date 2021/2/15 6:54 下午
 */
public class SeeFaceImpl implements MoreFaceInvokerInterface {

    @Override
    public void eat() {

    }

    @Override
    public void see() {
        System.out.println("只实现see");
    }

}
