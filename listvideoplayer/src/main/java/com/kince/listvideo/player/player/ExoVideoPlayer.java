package com.kince.listvideo.player.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.TextureView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.kince.listvideo.player.logger.ExoPlayerLogger;
import com.kince.listvideo.player.state.VideoPlayerState;

import java.util.List;

/**
 * 继承此类时，如果想要获取当前正在播放的视频url，请用如下方式获取：
 * {@link VideoPlayerManager#getVideoUrl()}
 * 因为此处的{@link #mUrl} 在开启缓存时是视频的缓存代理地址，不是
 * 用户播放视频时所传入的地址
 */
public class ExoVideoPlayer extends AbsBaseVideoPlayer implements
        SimpleExoPlayer.VideoListener,
        TextRenderer.Output,
        ExoPlayer.EventListener {

    private static final String TAG = "VideoExoPlayer";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private Context mContext;
    private SimpleExoPlayer mExoPlayer;
    private DataSource.Factory mMediaDataSourceFactory;
    private MappingTrackSelector mTrackSelector;
    private ExoPlayerLogger mExoPlayerLogger;
    private Handler mMainHandler;

    public ExoVideoPlayer(Context context) {
        mContext = context.getApplicationContext();
        initExoPlayer();
    }

    public ExoVideoPlayer(Context context,boolean enableLog) {
        mContext = context.getApplicationContext();
        initExoPlayer();
        this.mEnableLog = enableLog;
    }

    @Override
    public void setTextureView(TextureView textureView) {
        if(textureView == null) {
            mExoPlayer.clearVideoTextureView(mTextureView);
        }
        super.setTextureView(textureView);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if(mSurfaceTexture != null) {
        }
        mExoPlayer.setVideoTextureView(mTextureView);
        super.onSurfaceTextureAvailable(surface, width, height);
    }

    @Override
    protected void prepare() {
        mExoPlayer.stop();
        mExoPlayer.setVideoTextureView(mTextureView);
        MediaSource source = buildMediaSource(Uri.parse(mUrl), null);
        mExoPlayer.prepare(source);
    }

    @Override
    public void start(String url) {
        mUrl = url;
    }

    @Override
    public void play() {
        if(mExoPlayer.getPlaybackState() == ExoPlayer.STATE_READY) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void pause() {
        if(mExoPlayer.getPlaybackState() == ExoPlayer.STATE_READY) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * 恢复播放
     */
    @Override
    public void resume() {
        if(mExoPlayer.getPlaybackState() == ExoPlayer.STATE_READY) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void stop() {
        pause();
        mExoPlayer.stop();
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {

    }

    @Override
    public void release() {
        pause();
        mExoPlayer.release();
    }

    @Override
    public void setPlayerState(int state) {
        mState = state;
    }

    @Override
    public int getPlayerState() {
        return mState;
    }

    @Override
    public int getCurrentPosition() {
        return (int) mExoPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return (int) mExoPlayer.getDuration();
    }

    @Override
    public void seekTo(int position) {
        mExoPlayer.seekTo(position);
    }

    /**
     * 设置音量
     *
     * @param volume
     */
    @Override
    public void setVolume(int volume) {

    }

    /**
     * 获取当前音量
     *
     * @return
     */
    @Override
    public int getVolume() {
        return 0;
    }

    /**--------------------- ExoPlayer.EventListener ----------------------------**/
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == ExoPlayer.STATE_ENDED) {//播放结束
            onCompletion();
        } else if(playbackState == ExoPlayer.STATE_READY) {//准备播放
            if(getPlayerState() == VideoPlayerState.STATE_LOADING) {//加载视频
                onPrepared();
            } else if(getPlayerState() == VideoPlayerState.STATE_PLAYING_BUFFERING_START) {//seek to complete
                onSeekComplete();
            }
        }
    }

    /**
     * video prepared complete call back
     */
    public void onPrepared() {
        if(mPlayCallback != null) {
            mPlayCallback.onDurationChanged((int) mExoPlayer.getDuration());
            mPlayCallback.onPlayStateChanged(VideoPlayerState.STATE_PLAYING);
        }
        play();
    }

    /**
     * play complete call back
     */
    public void onCompletion() {
        if(mPlayCallback != null) {
            mPlayCallback.onComplete();
        }
    }

    /**
     * SeekTo  Complete callback
     */
    public void onSeekComplete() {
        if(mPlayCallback != null) {
            mPlayCallback.onPlayStateChanged(VideoPlayerState.STATE_PLAYING);
        }
        play();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (mPlayCallback != null) {
            mPlayCallback.onError(error.getCause().toString());
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    /**--------------------- TextRenderer.Output ----------------------------**/
    @Override
    public void onCues(List<Cue> cues) {

    }

    /**--------------------- SimpleExoPlayer.VideoListener ----------------------------**/
    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame() {

    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mMediaDataSourceFactory), mMainHandler, mExoPlayerLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mMediaDataSourceFactory), mMainHandler, mExoPlayerLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mMediaDataSourceFactory, mMainHandler, mExoPlayerLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mMediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mMainHandler, mExoPlayerLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    /**--------------------- 创建SimpleExoPlayer ----------------------------**/

    private void initExoPlayer() {
        mMediaDataSourceFactory = buildDataSourceFactory(true);

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mExoPlayerLogger = new ExoPlayerLogger(mTrackSelector);
        mMainHandler = new Handler(Looper.getMainLooper());

        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mContext,
                null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);

        mExoPlayer =  ExoPlayerFactory.newSimpleInstance(renderersFactory, mTrackSelector);
        mExoPlayer.addListener(mExoPlayerLogger);
        mExoPlayer.setAudioDebugListener(mExoPlayerLogger);
        mExoPlayer.setVideoDebugListener(mExoPlayerLogger);
        mExoPlayer.setMetadataOutput(mExoPlayerLogger);

        mExoPlayer.setTextOutput(null);
        mExoPlayer.setVideoListener(null);
        mExoPlayer.removeListener(this);
        mExoPlayer.setVideoTextureView(null);

        mExoPlayer.setVideoListener(this);
        mExoPlayer.addListener(this);
        mExoPlayer.setTextOutput(this);
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(mContext, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(mContext, TAG), bandwidthMeter);
    }

}
