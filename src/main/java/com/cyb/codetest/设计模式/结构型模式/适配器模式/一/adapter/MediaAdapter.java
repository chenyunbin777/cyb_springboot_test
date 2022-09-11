package com.cyb.codetest.设计模式.结构型模式.适配器模式.一.adapter;

import com.cyb.codetest.设计模式.结构型模式.适配器模式.一.MediaPlayer;
import com.cyb.codetest.设计模式.结构型模式.适配器模式.一.advanced.AdvancedMediaPlayer;
import com.cyb.codetest.设计模式.结构型模式.适配器模式.一.advanced.Mp4Player;
import com.cyb.codetest.设计模式.结构型模式.适配器模式.一.advanced.VlcPlayer;
import lombok.Data;

/**
 * 适配器模式（Adapter Pattern）是作为两个不兼容的接口之间的桥梁。这种类型的设计模式属于结构型模式，它结合了两个独立接口的功能。
 *
 * 适配器来，扩展MediaPlayer接口功能，可以播放vlc和MP4
 * @author cyb
 * @date 2022/9/9 下午3:38
 */
@Data
public class MediaAdapter implements MediaPlayer {

    private AdvancedMediaPlayer advancedMediaPlayer;

    public MediaAdapter(String playType){
        if("vlc".equals(playType)){
            advancedMediaPlayer = new VlcPlayer();
        }else if("mp4".equals(playType)){
            advancedMediaPlayer = new Mp4Player();
        }

    }


    @Override
    public void play(String playType, String fileName) {
        if("vlc".equals(playType)){
            advancedMediaPlayer.playVlc(fileName);
        }else if("mp4".equals(playType)){
            advancedMediaPlayer.playMp4(fileName);
        }
    }
}
