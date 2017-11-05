package com.kince.listvideo.player.state;

/**
 * 屏幕状态
 */
public final class ScreenViewState {

    /**
     * 普通模式
     */
    public static final int SCREEN_STATE_NORMAL = 1;

    /**
     * 全屏模式
     */
    public static final int SCREEN_STATE_FULLSCREEN = 2;

    /**
     * 小窗口模式
     */
    public static final int SCREEN_STATE_SMALL_WINDOW = 3;

    public static boolean isFullScreen(int screenState) {
        return screenState == SCREEN_STATE_FULLSCREEN;
    }

    public static boolean isSmallWindow(int screenState) {
        return screenState == SCREEN_STATE_SMALL_WINDOW;
    }

    public static boolean isNormal(int screenState) {
        return screenState == SCREEN_STATE_NORMAL;
    }

}
