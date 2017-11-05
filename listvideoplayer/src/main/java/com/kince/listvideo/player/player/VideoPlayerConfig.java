package com.kince.listvideo.player.player;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.ProxyCacheUtils;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.kince.listvideo.player.factory.IVideoPlayerFactory;
import com.kince.listvideo.player.factory.MediaPlayerFactory;
import com.kince.listvideo.player.utils.Utils;

import java.io.File;

/**
 * 播放器相关功能开关配置
 */
public final class VideoPlayerConfig {

    private static final int DEFAULT_VIDEO_CACHE_COUNT = 10;

    /**
     * 播放器工厂
     * <p>
     * 通过配置工厂可以实现自定义播放器，不管是用MediaPlayer还是ExoPlayer，还是其他的视频播放库均可以自行定义
     */
    private IVideoPlayerFactory mPlayerFactory;
    /**
     * 是否开启自动小窗口播放功能
     */
    private boolean mSmallWindowPlayEnable;
    /**
     * 缓存功能是否开启
     */
    private boolean mCacheEnable;
    /**
     * 日志输出功能是否开启
     */
    private boolean mLogEnable;
    /**
     * 缓存代理实现，{@link #mCacheEnable}必须为true才能生效
     */
    private HttpProxyCacheServer mCacheProxy;

    private VideoPlayerConfig(Builder builder) {
        this.mPlayerFactory = builder.playerFactory;
        this.mSmallWindowPlayEnable = builder.smallWindowPlayEnable;
        this.mCacheEnable = builder.cacheEnable;
        this.mCacheProxy = builder.proxy;
        this.mLogEnable = builder.logEnable;
    }

    public IVideoPlayerFactory getPlayerFactory() {
        return mPlayerFactory;
    }

    public boolean isSmallWindowPlayEnable() {
        return mSmallWindowPlayEnable;
    }

    public boolean isCacheEnable() {
        return mCacheEnable;
    }

    public HttpProxyCacheServer getCacheProxy() {
        return mCacheProxy;
    }

    public final static class Builder {
        private Context context;
        private IVideoPlayerFactory playerFactory;
        private boolean smallWindowPlayEnable = false;
        /**
         * 是否开启缓存，默认不开启
         */
        private boolean cacheEnable = false;
        /**
         * 日志输出功能是否开启
         */
        private boolean logEnable;
        /**
         * 缓存代理实现
         */
        private HttpProxyCacheServer proxy;

        /**
         * @param ctx context.getApplicationContext()
         */
        public Builder(Context ctx) {
            this.context = ctx;
        }

        protected Builder() {

        }

        /**
         * 配置Player工厂，用于创建播放器
         * 通过配置工厂可以实现自定义播放器，不管是用MediaPlayer还是ExoPlayer，还是其他的视频播放库均可以自行定义
         *
         * @param factory
         * @return
         */
        public Builder buildPlayerFactory(IVideoPlayerFactory factory) {
            this.playerFactory = factory;
            return this;
        }

        /**
         * 开启小窗口播放功能，默认不开启
         *
         * @return
         */
        public Builder enableSmallWindowPlay() {
            this.smallWindowPlayEnable = true;
            return this;
        }

        /**
         * 是否开启视频缓存功能
         *
         * @param cacheEnable true：会使用默认缓存配置进行视频缓存
         * @return
         */
        public Builder enableCache(boolean cacheEnable) {
            this.cacheEnable = cacheEnable;
            return this;
        }

        public Builder enableLog(boolean logEnable) {
            this.logEnable = logEnable;
            return this;
        }

        /**
         * 设置缓存代理实现(请先通过{@link #enableCache(boolean)}开启缓存功能，否则此设置无效
         *
         * @param cacheProxy
         * @return
         */
        public Builder cacheProxy(HttpProxyCacheServer cacheProxy) {
            this.proxy = cacheProxy;
            return this;
        }

        public VideoPlayerConfig build() {
            playerFactory.logEnable(logEnable);
            if (playerFactory == null) {
                playerFactory = new MediaPlayerFactory();
            }
            if (cacheEnable && proxy == null) {
                proxy = buildCacheProxy();
            }
            return new VideoPlayerConfig(this);
        }

        private HttpProxyCacheServer buildCacheProxy() {
            return new HttpProxyCacheServer
                    .Builder(context.getApplicationContext())
                    .cacheDirectory(new File(Utils.getCacheDir(context)))
                    .fileNameGenerator(new Md5FileNameGenerator() {
                        @Override
                        public String generate(String url) {
                            return ProxyCacheUtils.computeMD5(url);
                        }
                    })
                    .maxCacheFilesCount(DEFAULT_VIDEO_CACHE_COUNT)
                    .build();
        }
    }

}
