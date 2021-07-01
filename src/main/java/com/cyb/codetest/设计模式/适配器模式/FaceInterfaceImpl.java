package com.cyb.codetest.设计模式.适配器模式;

/**
 * @author cyb
 * @date 2021/2/15 9:22 下午
 */
public class FaceInterfaceImpl implements FaceInterface {

    AdatperClass adatperClass;

    @Override
    public void face(String type) {
        if ("face".equalsIgnoreCase(type)) {
            System.out.println("face功能");
        } else if ("see".equalsIgnoreCase(type) || "eat".equalsIgnoreCase(type)) {
            adatperClass = new AdatperClass(type);
            adatperClass.face(type);
        }


    }
}
