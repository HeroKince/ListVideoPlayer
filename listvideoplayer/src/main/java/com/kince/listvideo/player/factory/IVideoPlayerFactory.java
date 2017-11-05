package com.kince.listvideo.player.factory;

import com.kince.listvideo.player.player.AbsBaseVideoPlayer;

/**
 * Created by Kince
 */
public interface IVideoPlayerFactory {

    /**
     * 创建播放器实例
     * @return
     */
    AbsBaseVideoPlayer create();

    /**
     * 是否输出日志
     * @param enableLog
     */
    void logEnable(boolean enableLog);

}
