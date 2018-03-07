package com.kince.listvideo.player.state;

/**
 * Created by Kince
 * 播放器播放状态
 *
 */
public final class VideoPlayerState {

    public static final int STATE_NORMAL = 0;

    public static final int STATE_LOADING = 1;

    /**
     * 正在播放
     **/
    public static final int STATE_PLAYING = 2;

    public static final int STATE_PLAYING_BUFFERING_START = 3;

    /**
     * 暂停播放
     **/
    public static final int STATE_PAUSE = 4;

    /**
     * 播放完成
     **/
    public static final int STATE_AUTO_COMPLETE = 5;

    /**
     * 播放错误
     **/
    public static final int STATE_ERROR = 6;

}
