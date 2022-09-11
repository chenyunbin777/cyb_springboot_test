package com.cyb.codetest.设计模式.结构型模式.适配器模式.一.advanced;

/**
 * @author cyb
 * @date 2022/9/9 下午3:34
 */
public class VlcPlayer implements AdvancedMediaPlayer{

    /**
     * 仅实现vlc播放
     */
    @Override
    public void playVlc(String fileName) {
        System.out.println("playVlc fileName:"+fileName);

    }

    @Override
    public void playMp4(String fileName) {

    }
}
