package com.cyb.codetest.设计模式.结构型模式.适配器模式.一.advanced;

/**
 * @author cyb
 * @date 2022/9/9 下午3:36
 */
public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {

    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("playMp4 fileName:"+fileName);
    }
}
