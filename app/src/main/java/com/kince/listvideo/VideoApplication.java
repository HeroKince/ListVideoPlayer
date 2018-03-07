package com.kince.listvideo;

import android.app.Application;

import com.kince.listvideo.player.factory.ExoPlayerFactory;
import com.kince.listvideo.player.config.VideoPlayerConfig;
import com.kince.listvideo.player.player.VideoPlayerManager;

/**
 * Created by Kince183 on 2017/11/4.
 */
public class VideoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.init(this);
        VideoPlayerManager.loadConfig(
                new VideoPlayerConfig.Builder(this)
                        .buildPlayerFactory(new ExoPlayerFactory(this))
                        .enableSmallWindowPlay()
                        .enableCache(true)
                        .enableLog(true)
                        .build()
        );
    }

}
