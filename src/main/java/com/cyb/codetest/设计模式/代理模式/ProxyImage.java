package com.cyb.codetest.设计模式.代理模式;

/**
 * ProxyImage 是一个代理类，减少 RealImage 对象加载的内存占用
 * 会持有一个Image接口的真实实现类的对象
 *
 * @author cyb
 * @date 2021/1/2 12:24 上午
 */
public class ProxyImage implements Image {

    private RealImage realImage;
    private String fileName;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}
