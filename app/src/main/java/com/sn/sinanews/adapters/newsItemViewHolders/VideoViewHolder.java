package com.sn.sinanews.adapters.newsItemViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;

/**
 * Created by Ming on 2016/1/15.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {


    private ProgressBar mFooterProgressBar;
    private TextView mVideoTitle;
    private  SurfaceView mVideoSv;
    private  SimpleDraweeView mVideoPic;
    // 正常显示的
    private  LinearLayout mLinearLayoutCorrect;
    // 显示错误的
    private  LinearLayout mLinearLayoutError;
    private  ImageButton mVideoPlayError;
    // 最上层
    private  ImageView mVideoPlay;

    // footer
    private  LinearLayout mLinearLayoutFooter;
    private  ImageView mImageFooterPlay;
    private  TextView mVideoProgressTime;
    private  SeekBar mVideoSeekBar;
    private  ImageButton mVideoFull;
    private  TextView mVideoComment;


    public VideoViewHolder(View itemView) {
        super(itemView);
        mVideoTitle = (TextView) itemView.findViewById(R.id.video_title);
        mVideoSv = (SurfaceView) itemView.findViewById(R.id.video_sv);
        mVideoPic = (SimpleDraweeView) itemView.findViewById(R.id.video_pic_url);
        mLinearLayoutCorrect = (LinearLayout) itemView.findViewById(R.id.video_body_play_correct);
        mLinearLayoutError = (LinearLayout) itemView.findViewById(R.id.video_body_play_error);
        mVideoPlayError = (ImageButton) itemView.findViewById(R.id.video_error_play);
        mVideoPlay = (ImageView) itemView.findViewById(R.id.video_item_play);
        mLinearLayoutFooter = (LinearLayout) itemView.findViewById(R.id.video_footer_item);
        mImageFooterPlay = (ImageView) itemView.findViewById(R.id.video_footer_item_play);
        mVideoProgressTime = (TextView) itemView.findViewById(R.id.video_footer_time_pro);

        mVideoSeekBar = (SeekBar) itemView.findViewById(R.id.video_footer_seekBar);
        mVideoFull = (ImageButton) itemView.findViewById(R.id.video_footer_full);
        mVideoComment = (TextView) itemView.findViewById(R.id.video_text_comments);
        mFooterProgressBar = (ProgressBar) itemView.findViewById(R.id.footerLinear_progressBar);
    }

    public TextView getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(TextView videoTitle) {
        mVideoTitle = videoTitle;
    }

    public SurfaceView getVideoSv() {
        return mVideoSv;
    }

    public void setVideoSv(SurfaceView videoSv) {
        mVideoSv = videoSv;
    }

    public SimpleDraweeView getVideoPic() {
        return mVideoPic;
    }

    public void setVideoPic(SimpleDraweeView videoPic) {
        mVideoPic = videoPic;
    }

    public LinearLayout getLinearLayoutCorrect() {
        return mLinearLayoutCorrect;
    }

    public void setLinearLayoutCorrect(LinearLayout linearLayoutCorrect) {
        mLinearLayoutCorrect = linearLayoutCorrect;
    }

    public LinearLayout getLinearLayoutError() {
        return mLinearLayoutError;
    }

    public void setLinearLayoutError(LinearLayout linearLayoutError) {
        mLinearLayoutError = linearLayoutError;
    }

    public ImageButton getVideoPlayError() {
        return mVideoPlayError;
    }

    public void setVideoPlayError(ImageButton videoPlayError) {
        mVideoPlayError = videoPlayError;
    }

    public ImageView getVideoPlay() {
        return mVideoPlay;
    }

    public void setVideoPlay(ImageView videoPlay) {
        mVideoPlay = videoPlay;
    }

    public LinearLayout getLinearLayoutFooter() {
        return mLinearLayoutFooter;
    }

    public void setLinearLayoutFooter(LinearLayout linearLayoutFooter) {
        mLinearLayoutFooter = linearLayoutFooter;
    }

    public ImageView getImageFooterPlay() {
        return mImageFooterPlay;
    }

    public void setImageFooterPlay(ImageView imageFooterPlay) {
        mImageFooterPlay = imageFooterPlay;
    }

    public TextView getVideoProgressTime() {
        return mVideoProgressTime;
    }

    public void setVideoProgressTime(TextView videoProgressTime) {
        mVideoProgressTime = videoProgressTime;
    }

    public SeekBar getVideoSeekBar() {
        return mVideoSeekBar;
    }

    public void setVideoSeekBar(SeekBar videoSeekBar) {
        mVideoSeekBar = videoSeekBar;
    }

    public ImageButton getVideoFull() {
        return mVideoFull;
    }

    public void setVideoFull(ImageButton videoFull) {
        mVideoFull = videoFull;
    }

    public TextView getVideoComment() {
        return mVideoComment;
    }

    public void setVideoComment(TextView videoComment) {
        mVideoComment = videoComment;
    }

    public ProgressBar getFooterProgressBar() {
        return mFooterProgressBar;
    }

    public void setFooterProgressBar(ProgressBar footerProgressBar) {
        mFooterProgressBar = footerProgressBar;
    }
}
