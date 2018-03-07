package com.kince.listvideo.player.message;

/**
 * Created by Kince
 * 全屏状态点击返回键时的消息
 */
public class BackPressedMessage extends Message {

    private int mScreenState;

    public BackPressedMessage(int screenState, int hash, String videoUrl) {
        super(hash, videoUrl);
        mScreenState = screenState;
    }

    public int getScreenState() {
        return mScreenState;
    }

}
