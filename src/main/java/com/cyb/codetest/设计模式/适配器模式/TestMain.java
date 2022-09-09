package com.cyb.codetest.设计模式.适配器模式;

/**
 * @author cyb
 * @date 2021/2/15 9:26 下午
 */
public class TestMain {

    public static void main(String[] args) {
        FaceInterface FaceInterface = new FaceInterfaceImpl();
        FaceInterface.face("see");
        FaceInterface.face("eat");
    }
}
