package com.kince.listvideo.player.factory;

import android.content.Context;

import com.kince.listvideo.player.player.AbsBaseVideoPlayer;
import com.kince.listvideo.player.player.ExoVideoPlayer;

/**
 * Created by Kince
 * 创建基于ExoPlayer实现的播放器
 */
public class ExoPlayerFactory implements IVideoPlayerFactory {

    private Context mContext;
    private boolean mEnableLog;

    public ExoPlayerFactory(Context context) {
        mContext = context;
    }

    @Override
    public AbsBaseVideoPlayer create() {
        return new ExoVideoPlayer(mContext, mEnableLog);
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
