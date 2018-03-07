package com.kince.listvideo.player.factory;

import com.kince.listvideo.player.player.AbsBaseVideoPlayer;
import com.kince.listvideo.player.player.MediaVideoPlayer;

/**
 * Created by Kince
 * 创建基于MediaPlayer实现的播放器
 */
public class MediaPlayerFactory implements IVideoPlayerFactory {

    private boolean mEnableLog;

    @Override
    public AbsBaseVideoPlayer create() {
        return new MediaVideoPlayer(mEnableLog);
    }

    /**
     * 是否输出日志
     *
     * @param enableLog
     */
    @Override
    public void logEnable(boolean enableLog) {
        mEnableLog = enableLog;
    }

}
