package com.kince.listvideo.player.view;

/**
 * Created by Kince
 * 播放器视图控制
 */
public interface IVideoPlayerView {

    /**
     * 设置是否自动播放
     * @param auto true则自动播放。
     */
    public void setAutoPlay(boolean auto);

    /**
     * 是否显示控制栏
     * @param show
     */
    public void setControlBarCanShow(boolean show);

    /**
     * 是否显示标题栏
     * @param show
     */
    public void setTitleBarCanShow(boolean show);

    /**
     * 播放器释放
     */
    public void destroy();

    /**
     * 设置视频封面
     * @param uri
     */
    public void setCoverData(Object uri);

    /**
     * 设置小屏或者全屏
     * @param screenMode
     */
    public void changeScreenMode(int screenMode);

    /**
     * 获取屏幕模式
     * @return
     */
    public int getScreenMode();

    /**
     * 获取缓冲进度
     * @return 缓冲进度，值为0～100。
     */
    public int getBufferPercentage();

    /**
     * 设置屏幕亮度
     * @param brightness 亮度值：0~255.
     */
    public void setScreenBrightness(int brightness);

    /**
     * 获取屏幕亮度
     * @return 亮度值
     */
    public int getScreenBrightness();

    /**
     * 获取缓冲进度
     * @return
     */
    public int getBufferingPosition();

}
