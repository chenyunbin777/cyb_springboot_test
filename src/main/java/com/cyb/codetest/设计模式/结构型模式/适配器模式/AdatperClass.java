package com.cyb.codetest.设计模式.结构型模式.适配器模式;

/**
 * 适配器类
 *
 * @author cyb
 * @date 2021/2/15 6:51 下午
 */
public class AdatperClass implements FaceInterface {

    MoreFaceInvokerInterface moreInvokerInterfaceImpl;

    public AdatperClass(String type) {
        if ("see".equalsIgnoreCase(type)) {
            moreInvokerInterfaceImpl = new SeeFaceImpl();
        } else if ("eat".equalsIgnoreCase(type)) {
            moreInvokerInterfaceImpl = new EatFaceImpl();
        }
    }

    @Override
    public void face(String type) {
        if ("see".equalsIgnoreCase(type)) {
            moreInvokerInterfaceImpl.see();
        } else if ("eat".equalsIgnoreCase(type)) {
            moreInvokerInterfaceImpl.eat();
        }
    }
}
