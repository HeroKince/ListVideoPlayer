package com.kince.listvideo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kince.listvideo.player.view.AbsVideoPlayerView;

import java.util.List;

/**
 * Created by Kince183 on 2017/11/6.
 */
public class FullVideoAdapter extends RecyclerView.Adapter<FullVideoAdapter.FullVideoViewHolder>{

    private Context mContext;
    private List<VideoBean> mVideoList;
    private int mScreenWidth;
    private int mScreenHeight;

    public FullVideoAdapter(Context context) {
        mContext = context;
        mVideoList = VideoData.getVideoList();
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight =  context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public FullVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item_view, parent, false);
        view.getLayoutParams().width = mScreenWidth;
        view.getLayoutParams().height = mScreenHeight;
        return new FullVideoAdapter.FullVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FullVideoViewHolder holder, int position) {
        VideoBean video = mVideoList.get(position);
        holder.mPlayerView.bind(video.getVideoUrl(), video.getVideoTitle(),true);
        holder.mPlayerView.getThumbImageView().setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mContext).load(video.getVideoThumbUrl()).into(holder.mPlayerView.getThumbImageView());
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    class FullVideoViewHolder extends RecyclerView.ViewHolder {
        AbsVideoPlayerView mPlayerView;

        public FullVideoViewHolder(View itemView) {
            super(itemView);
            mPlayerView = itemView.findViewById(R.id.video_player_view);
        }
    }

}
