package com.kince.listvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.kince.listvideo.player.player.VideoPlayerManager;
import com.kince.listvideo.player.view.VideoPlayerView;

public class MainActivity extends AppCompatActivity {

    VideoPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayerView = findViewById(R.id.video_player_view);

        VideoBean videoBean = VideoData.getVideo();
        mPlayerView.bind(videoBean.getVideoUrl(), videoBean.getVideoTitle());
        mPlayerView.getThumbImageView().setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(videoBean.getVideoThumbUrl()).into(mPlayerView.getThumbImageView());
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.video_in_recyclerView:
                startActivity(new Intent(this, RecyclerViewVideoActivity.class));
                break;
            case R.id.video_in_viewpager_fragment:
                startActivity(new Intent(this, ViewPagerFragmentActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoPlayerManager.getInstance().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayerManager.getInstance().pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoPlayerManager.getInstance().release();
    }

}
