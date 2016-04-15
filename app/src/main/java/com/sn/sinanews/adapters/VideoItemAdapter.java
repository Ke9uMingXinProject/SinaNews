package com.sn.sinanews.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;
import com.sn.sinanews.entities.VideoEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 卜令壮
 * on 2016/1/13
 * E-mail q137617549@qq.com
 */
public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.VideoItemViewHolder> implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, SurfaceHolder.Callback {
    private static final String TAG = VideoItemAdapter.class.getSimpleName();
    private Context context;
    private List<VideoEntity.DataEntity.ListEntity> list;
    private MediaPlayer mediaPlayer;
    private RecyclerView mRecyclerView;

    public VideoItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.adapter_video_item, parent, false);
        VideoItemViewHolder holder = new VideoItemViewHolder(inflate);
        holder.videoImg.setOnClickListener(this);
        holder.videoImg.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, int position) {
        resetHolder(holder);
        VideoEntity.DataEntity.ListEntity listEntity = list.get(position);
        holder.title.setText(listEntity.getTitle());
        int playNumber = listEntity.getVideo_info().getPlaynumber();
        int WNumber;
        if ((WNumber = playNumber / 10000) >= 1) {
            holder.comments.setText(WNumber + "万播放");
        } else {
            holder.comments.setText(playNumber + "播放");
        }

        holder.videoImg.setImageURI(Uri.parse(listEntity.getVideo_info().getKpic()));
    }

    private void resetHolder(VideoItemViewHolder holder) {
        if (holder.videoImg != null) {
            holder.videoImg.setVisibility(View.VISIBLE);
        }
        holder.videoPlayImg.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addAll(Collection<? extends VideoEntity.DataEntity.ListEntity> collection, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
        }
        list.addAll(collection);
        notifyDataSetChanged();
    }

    //recyclerView中特有的方法，判断控件划出屏幕
    @Override
    public void onViewDetachedFromWindow(VideoItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (lastHolder != null && lastHolder.equals(holder)) {
            mediaPlayer.reset();
            resetHolder(lastHolder);
            lastHolder = null;
        }
    }
    private VideoItemViewHolder lastHolder;

    //默认静态图片的点击事件监听
    @Override
    public void onClick(View v) {
        if (lastHolder != null) {
            resetHolder(lastHolder);
        }
        VideoItemViewHolder tagHolder = (VideoItemViewHolder) v.getTag();
        try {
            mediaPlayer.reset();
            String url = list.get(tagHolder.getAdapterPosition()).getVideo_info().getUrl();
            SurfaceHolder holder = tagHolder.video.getHolder();
            holder.addCallback(this);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            tagHolder.videoPlayImg.setVisibility(View.INVISIBLE);
            tagHolder.progressBar.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastHolder = tagHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    //mediaPlayer准备的监听
    @Override
    public void onPrepared(MediaPlayer mp) {
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        int surfaceViewHeight = lastHolder.video.getHeight();
        int w = surfaceViewHeight * videoWidth / videoHeight;
        ViewGroup.LayoutParams layoutParams = lastHolder.video.getLayoutParams();
        layoutParams.width = w;
//        layoutParams.height 高度不变
        lastHolder.video.setLayoutParams(layoutParams);
        mp.start();
        lastHolder.videoImg.setVisibility(View.INVISIBLE);
        lastHolder.progressBar.setVisibility(View.INVISIBLE);
    }

    //mediaPlayer播放结束的监听
    @Override
    public void onCompletion(MediaPlayer mp) {
        resetHolder(lastHolder);
        lastHolder = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "换页了");
    }


    public static class VideoItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView comments;
        private SurfaceView video;
        private SimpleDraweeView videoImg;
        private final ProgressBar progressBar;
        private final ImageView videoPlayImg;

        public VideoItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_video_item_title);
            comments = (TextView) itemView.findViewById(R.id.adapter_video_item_comment);
            video = (SurfaceView) itemView.findViewById(R.id.adapter_video_item_video);
            videoImg = (SimpleDraweeView) itemView.findViewById(R.id.adapter_video_item_videoImg);
            progressBar = (ProgressBar) itemView.findViewById(R.id.adapter_video_item_videoProgressBar);
            videoPlayImg = (ImageView) itemView.findViewById(R.id.adapter_video_item_videoPlayImg);
            videoImg.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            videoImg.setAspectRatio(1.8f);
        }
    }
}
