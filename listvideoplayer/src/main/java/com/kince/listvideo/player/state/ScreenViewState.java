package com.kince.listvideo.player.state;

/**
 * 屏幕状态
 */
public final class ScreenViewState {

    /**
     * 普通列表滑动模式（宽大于高）
     */
    public static final int SCREEN_STATE_NORMAL = 1;

    /**
     * 全屏列表滑动模式（高大于宽）
     */
    public static final int SCREEN_STATE_LIST_FULLSCREEN = 2;

    /**
     * 单个视频全屏模式
     */
    public static final int SCREEN_STATE_FULLSCREEN = 3;

    /**
     * 单个视频小窗口模式
     */
    public static final int SCREEN_STATE_SMALL_WINDOW = 4;

    public static boolean isFullScreen(int screenState) {
        return screenState == SCREEN_STATE_FULLSCREEN;
    }

    public static boolean isSmallWindow(int screenState) {
        return screenState == SCREEN_STATE_SMALL_WINDOW;
    }

    public static boolean isNormal(int screenState) {
        return screenState == SCREEN_STATE_NORMAL;
    }

    public static boolean isFullScreenList(int screenState) {
        return screenState == SCREEN_STATE_LIST_FULLSCREEN;
    }

}
