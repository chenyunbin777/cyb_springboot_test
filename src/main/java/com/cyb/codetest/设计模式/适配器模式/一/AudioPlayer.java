package com.cyb.codetest.设计模式.适配器模式.一;

import com.cyb.codetest.设计模式.适配器模式.一.adapter.MediaAdapter;

/**
 * 新的实现类 通过适配器类mediaAdapter实现了MP3 vlc MP4等播放功能。
 * 原来只有mp3的播放功能
 *
 * @author cyb
 * @date 2022/9/9 下午3:33
 */
public class AudioPlayer implements MediaPlayer {

    private MediaAdapter mediaAdapter;

    public AudioPlayer(String playType) {
        mediaAdapter = new MediaAdapter(playType);
    }


    @Override
    public void play(String playType, String fileName) {


        if ("mp3".equals(playType)) {
            System.out.println("playType：" + playType + "   fileName：" + fileName);
        } else if ("vlc".equals(playType) || "mp4".equals(playType)) {

            mediaAdapter.play(playType, fileName);
        } else {
            System.out.println("Invalid media. " +
                    playType + " format not supported");
        }


    }
}
