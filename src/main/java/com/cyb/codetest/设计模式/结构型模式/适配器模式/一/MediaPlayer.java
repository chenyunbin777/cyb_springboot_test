package com.cyb.codetest.设计模式.结构型模式.适配器模式.一;

/**
 * 原始播放接口 只能播放mp3
 * @author cyb
 * @date 2022/9/9 下午3:32
 */
public interface MediaPlayer {

    public abstract void play(String playType, String fileName);
}
