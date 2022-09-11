package com.cyb.codetest.设计模式.结构型模式.适配器模式.一;

import com.cyb.codetest.设计模式.结构型模式.适配器模式.一.adapter.MediaAdapter;

/**
 * @author cyb
 * @date 2022/9/10 下午10:12
 */
public class AdapterPatternDemo {
    public static void main(String[] args) {
//        AudioPlayer audioPlayer = new AudioPlayer();
//        audioPlayer.play("mp3", "beyond the horizon.mp3");
//        audioPlayer.play("mp4", "alone.mp4");
//        audioPlayer.play("vlc", "far far away.vlc");
//        audioPlayer.play("avi", "mind me.avi");

        //直接可以通过适配器去调用mp4的播放，这样就适配了播放mp4的功能
        new MediaAdapter("mp4").play("mp4","alone.mp4");
        new MediaAdapter("vlc").play("vlc","far far away.vlc");
    }
}