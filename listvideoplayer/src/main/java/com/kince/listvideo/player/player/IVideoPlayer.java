package com.kince.listvideo.player.player;

import android.view.TextureView;

import com.kince.listvideo.player.state.VideoPlayerState;

/**
 * 播放器接口
 *
 */
public interface IVideoPlayer {

    /**
     * 开始播放指定Url 的视频
     * @param url
     */
    void start(String url);

    /**
     * 播放当前视频
     */
    void play();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * 停止播放,即结束当前视频的播放操作，但不释放资源
     */
    void stop();

    /**
     * 重置播放器
     */
    public void reset();

    /**
     * 释放资源
     */
    void release();

    /**
     * 设置播放状态
     *
     * @param state {@link VideoPlayerState}
     */
    void setPlayerState(int state);

    /**
     * 获取当前的播放状态
     *
     * @return {@link VideoPlayerState}
     */
    int getPlayerState();

    /**
     * 当前是否正在播放
     *
     * @return boolean 播放器处于播放或暂停时返回true，否则为false
     */
    boolean isPlaying();

    /**
     * 获取当前的播放进度
     *
     * @return pos 播放进度，默认为0
     */
    int getCurrentPosition();

    /**
     * 获取视频时长
     * @return
     */
    int getDuration();

    /**
     * 跳跃到position位置开始播放
     *
     * @param position
     */
    void seekTo(int position);

    /**
     * 设置播放回调函数
     * @param playCallback
     */
    void setPlayCallback(PlayCallback playCallback);

    /**
     * 设置音量
     * @param volume
     */
    public void setVolume(int volume);

    /**
     * 获取当前音量
     * @return
     */
    public int getVolume();

    /**
     * @param textureView
     */
    void setTextureView(TextureView textureView);

    interface PlayCallback {

        void onError(String error);

        void onComplete();

        void onPlayStateChanged(int state);

        void onDurationChanged(int duration);

    }

}
