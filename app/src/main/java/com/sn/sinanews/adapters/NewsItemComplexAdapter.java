package com.sn.sinanews.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Observable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;
import com.sn.sinanews.adapters.newsItemViewHolders.ImageGroupBigViewHolder;
import com.sn.sinanews.adapters.newsItemViewHolders.ImageMultipleViewHolder;
import com.sn.sinanews.adapters.newsItemViewHolders.ImageTextViewHolder;
import com.sn.sinanews.adapters.newsItemViewHolders.ImageTurnViewHolder;
import com.sn.sinanews.adapters.newsItemViewHolders.RecyclerViewSimpleTextViewHolder;
import com.sn.sinanews.adapters.newsItemViewHolders.VideoViewHolder;
import com.sn.sinanews.entities.NewsEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ming on 2016/1/10.
 */
public class NewsItemComplexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ViewPager.OnPageChangeListener {

    private static final String TAG = "NewsItemComplexAdapter";


    private Context context;
    private List<NewsEntity.DataEntity.ListEntity> items;

    private final int IMAGE_TURN = 0;
    private final int IMAGE_MULTIPLE = 1;
    private final int IMAGE_GROUP = 2;
    private final int IMAGE_TEXT = 3;
    private final int VIDEO_MEDIA_PLAYER = 4;
    private List<NewsEntity.DataEntity.ListEntity> turnItems;
    private LinearLayout dot_layout;
    private TextView tv_intro;
    private ViewPager viewPagerTurn;
    private MediaPlayer mediaPlayer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPagerTurn.setCurrentItem(viewPagerTurn.getCurrentItem() + 1);
            handler.sendEmptyMessageDelayed(0, 40000);
        }
    };
    private RadioButton newsItem_radio_text;

    //-------视频播放
    private ProgressBar mFooterProgressBar;
    private SeekBar mSeekBar;
    private Timer timer;
    private TextView mVideoProgressTime;
    private int currentPosition;
    private VideoViewHolder lastHolder;

    public Handler handlerVideo = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            // 刷新进度条的进度
            mSeekBar.setMax(duration);
            mSeekBar.setProgress(currentPosition);
            mVideoProgressTime.setText(turnTime(currentPosition) + "/" + turnTime(duration));
            mFooterProgressBar.setMax(duration);
            mFooterProgressBar.setProgress(currentPosition);
            super.handleMessage(msg);
        }
    };
    private NewsEntity.DataEntity.ListEntity mVideoEntity;


    public NewsItemComplexAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
        turnItems = new ArrayList<>();
        mediaPlayer = new MediaPlayer();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case IMAGE_TURN:
                View imageTurnView = inflater.inflate(R.layout.news_item_imgturn, parent, false);
                viewHolder = new ImageTurnViewHolder(imageTurnView);
                break;

            case IMAGE_MULTIPLE:
                View imageMulView = inflater.inflate(R.layout.news_item_imgmultiple, parent, false);
                viewHolder = new ImageMultipleViewHolder(imageMulView);
                break;

            case IMAGE_GROUP:
                View imageGroupView = inflater.inflate(R.layout.news_item_imggroup, parent, false);
                viewHolder = new ImageGroupBigViewHolder(imageGroupView);
                break;

            case IMAGE_TEXT:
                View imageTextView = inflater.inflate(R.layout.news_item_imgtext, parent, false);
                viewHolder = new ImageTextViewHolder(imageTextView);
                break;

            case VIDEO_MEDIA_PLAYER:
                View videoView = inflater.inflate(R.layout.news_item_video, parent, false);
                viewHolder = new VideoViewHolder(videoView);
                ((VideoViewHolder) viewHolder).getVideoPic().setTag(viewHolder);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case IMAGE_TURN:
                ImageTurnViewHolder imageTurnViewHolder = (ImageTurnViewHolder) holder;
                configureImageTurnViewHolder(imageTurnViewHolder, position);
                break;

            case IMAGE_MULTIPLE:
                ImageMultipleViewHolder imageMultipleViewHolder = (ImageMultipleViewHolder) holder;
                configureImageMulViewHolder(imageMultipleViewHolder, position);
                break;

            case IMAGE_GROUP:
                ImageGroupBigViewHolder imageGroupBigViewHolder = (ImageGroupBigViewHolder) holder;
                configureImageGroupBigViewHolder(imageGroupBigViewHolder, position);
                break;

            case IMAGE_TEXT:
                ImageTextViewHolder imageTextViewHolder = (ImageTextViewHolder) holder;
                configureImageTextViewHolder(imageTextViewHolder, position);
                break;
            case VIDEO_MEDIA_PLAYER:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                configureVideoViewHolder(videoViewHolder, position);
                break;

            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) holder;
                configureDefaultViewHolder(vh, position);
                break;
        }

    }

    // 处理视频
    private void configureVideoViewHolder(VideoViewHolder vh, int position) {

        mVideoEntity = items.get(position);
        TextView videoTitle = vh.getVideoTitle();
        videoTitle.setText(mVideoEntity.getTitle());

        // TODO: 2016/1/15 处理视频
        // 1. 获取播放按钮 设置显示和隐藏
        final ImageView videoPlay = vh.getVideoPlay();
        // 2. 获取 真正加载 设置显示和隐藏
        final LinearLayout linearLayoutCorrect = vh.getLinearLayoutCorrect();
        // 3. 获取网络无法加载的时候，显示和隐藏
        LinearLayout linearLayoutError = vh.getLinearLayoutError();
        // 获取底部 linearLayoutFooter
        final LinearLayout linearLayoutFooter = vh.getLinearLayoutFooter();
        // 获取底部 progressBar
        mFooterProgressBar = vh.getFooterProgressBar();
        // 获取底部 seekBar
        mSeekBar = vh.getVideoSeekBar();
        // 获取底部 时间 textView
        mVideoProgressTime = vh.getVideoProgressTime();

        // 4. 获取surfaceView
        final SurfaceView videoSv = vh.getVideoSv();
        final SurfaceHolder holder = videoSv.getHolder();
//        holder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                Log.d(TAG, "surfaceCreated: 进入videoSv.getHolder()");
//                mediaPlayer.setDisplay(holder);
//               /* if (mediaPlayer==null){
//                    mediaPlayer.start();
//                    mediaPlayer.seekTo(currentPosition);
//                }*/
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                if (mediaPlayer != null) {
//                    currentPosition = mediaPlayer.getCurrentPosition();
//                    mediaPlayer.stop();
//                }
//            }
//        });
        // 5. 获取视频播放的之前加载的图片
        final SimpleDraweeView videoPic = vh.getVideoPic();
        videoPic.setImageURI(Uri.parse(mVideoEntity.getKpic()));
        // 获取图片的进度
        //videoPic.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        // 设置缩放比例
        videoPic.setAspectRatio(1.7f);
        videoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastHolder != null) {
                    resetHolder(lastHolder);
                }
                VideoViewHolder currentVideoHolder = (VideoViewHolder) v.getTag();
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(mVideoEntity.getVideo_info().getUrl());
                    videoPlay.setVisibility(View.INVISIBLE);
                    videoPic.setVisibility(View.INVISIBLE);
                    mediaPlayer.setDisplay(holder);
                    mediaPlayer.prepareAsync();
                    linearLayoutCorrect.setVisibility(View.VISIBLE);

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            int w = mp.getVideoWidth();
                            int h = mp.getVideoHeight();
                            holder.setFixedSize(w, h); // Or setup a proportional sizes
                            mp.start();
                            addTimer();
                            linearLayoutCorrect.setVisibility(View.INVISIBLE);
                            mFooterProgressBar.setVisibility(View.VISIBLE);
                        }
                    });

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.seekTo(0);
                            mFooterProgressBar.setProgress(0);
                            // TODO: 2016/1/16 当播放之后再显示 播放按钮
/*                            videoPic.setVisibility(View.VISIBLE);
                            videoPlay.setVisibility(View.VISIBLE);
                            mFooterProgressBar.setVisibility(View.INVISIBLE);
                            linearLayoutCorrect.setVisibility(View.INVISIBLE);
                            linearLayoutFooter.setVisibility(View.INVISIBLE);*/
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }

                lastHolder = currentVideoHolder;

            }
        });


        videoSv.setOnClickListener(new View.OnClickListener() {

            AnimatorSet set = null;

            @Override
            public void onClick(View v) {
                if (linearLayoutFooter.getVisibility() == View.VISIBLE) {
                    set.cancel();
                } else if (mFooterProgressBar.getVisibility() == View.VISIBLE) {
                    linearLayoutFooter.setVisibility(View.VISIBLE);
                    set = new AnimatorSet();
                    ObjectAnimator oaGo = ObjectAnimator.ofFloat(linearLayoutFooter, "translationY", 80, 0);
                    oaGo.setDuration(1000);
                    ObjectAnimator oaBack = ObjectAnimator.ofFloat(linearLayoutFooter, "translationY", 0, 100);
                    oaBack.setStartDelay(4000);
                    oaBack.setDuration(1000);
                    set.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            linearLayoutFooter.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    set.playSequentially(oaGo, oaBack);
                    set.start();
                }

            }
        });

        final ImageView imageFooterPlay = vh.getImageFooterPlay();
        imageFooterPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    Log.d(TAG, "onClick: 页脚播放按钮");
                    mediaPlayer.pause();
                    imageFooterPlay.setImageResource(R.drawable.selector_video_footer_pasue);
                } else {
                    mediaPlayer.start();
                    imageFooterPlay.setImageResource(R.drawable.selector_video_footer_play);
                }
            }
        });

        // 拖动
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();
                //改变播放进度
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });

        TextView videoComment = vh.getVideoComment();
        videoComment.setText(mVideoEntity.getComment() + "评论");

    }


    // 图片 与 文字
    private void configureImageTextViewHolder(ImageTextViewHolder vh, int position) {
        NewsEntity.DataEntity.ListEntity entity = items.get(position);
        SimpleDraweeView image_text_image = vh.getImage_text_image();
        image_text_image.setImageURI(Uri.parse(entity.getKpic()));
        TextView image_text_title = vh.getImage_text_title();
        image_text_title.setText(entity.getTitle());
        TextView image_text_content = vh.getImage_text_content();
        image_text_content.setText(entity.getIntro());
        TextView image_text_comments = vh.getImage_text_comments();
        image_text_comments.setText(entity.getComment() + "评论");
        TextView image_text_img = vh.getImage_text_img();

        ImageButton image_text_video = vh.getImage_text_video();

        if (entity.getCategory().equals("cms") || entity.getCategory().equals("url")) {
            image_text_img.setVisibility(View.GONE);
            image_text_video.setVisibility(View.GONE);
            image_text_comments.setVisibility(View.VISIBLE);
            return;
        }

        if (entity.getCategory().equals("plan")) {
            image_text_comments.setVisibility(View.VISIBLE);
            image_text_img.setVisibility(View.VISIBLE);
            image_text_video.setVisibility(View.GONE);
            image_text_img.setText("策划");
            image_text_img.setBackgroundResource(R.mipmap.ic_list_item_bg_red);
            image_text_img.setTextColor(context.getResources().getColor(R.color.SinaNewsCommonColor));
            return;
        }
        if (entity.getCategory().equals("subject")) {
            image_text_comments.setVisibility(View.VISIBLE);
            image_text_img.setVisibility(View.VISIBLE);
            image_text_video.setVisibility(View.GONE);
            image_text_img.setBackgroundResource(R.mipmap.ic_list_item_bg_red);
            image_text_img.setTextColor(context.getResources().getColor(R.color.SinaNewsCommonColor));
            image_text_img.setText("专题");
            return;
        }

        if (entity.getCategory().equals("consice")) {
            image_text_comments.setVisibility(View.VISIBLE);
            image_text_video.setVisibility(View.GONE);
            image_text_img.setVisibility(View.VISIBLE);
            image_text_img.setBackgroundResource(R.mipmap.ic_list_item_bg_red);
            image_text_img.setTextColor(context.getResources().getColor(R.color.SinaNewsCommonColor));
            image_text_img.setText("精读");
            return;
        }

        if (entity.getCategory().equals("video")) {
            // 显示视频小图片
            image_text_comments.setVisibility(View.VISIBLE);
            image_text_img.setVisibility(View.GONE);
            image_text_video.setVisibility(View.VISIBLE);
            return;
        }

        if (entity.getCategory().equals("sponsor")) {
            // 赞助
            image_text_comments.setVisibility(View.GONE);
            image_text_video.setVisibility(View.GONE);
            image_text_img.setVisibility(View.VISIBLE);
            image_text_img.setBackgroundResource(R.mipmap.ic_list_item_bg_yellow);
            image_text_img.setTextColor(context.getResources().getColor(R.color.SinaNewsSponsorColor));
            image_text_img.setText("赞助");
            return;
        }


    }

    // 大图
    private void configureImageGroupBigViewHolder(ImageGroupBigViewHolder vh, int position) {
        NewsEntity.DataEntity.ListEntity entity = items.get(position);
        TextView image_group_title = vh.getImage_group_title();
        image_group_title.setText(entity.getTitle());
        SimpleDraweeView image_group_image = vh.getImage_group_image();
        if (entity.getBpic() != null) {
            image_group_image.setAspectRatio(1.3f);
            image_group_image.setImageURI(Uri.parse(entity.getBpic()));
        } else {
            image_group_image.setAspectRatio(2.1f);
            image_group_image.setImageURI(Uri.parse(entity.getKpic()));
        }

        TextView image_group_comments = vh.getImage_group_comments();
        image_group_comments.setText(entity.getComment() + "评论");

        TextView image_group_img = vh.getImage_group_img();

        if (entity.getCategory().equals("hdpic")) {
            RadioButton image_group_radio_text = vh.getImage_group_radio_text();
            image_group_radio_text.setVisibility(View.VISIBLE);
            image_group_radio_text.setText(entity.getPics().getTotal() + "图");
        }

        if (entity.getCategory().equals("plan")) {
            image_group_img.setVisibility(View.VISIBLE);
            image_group_img.setText("策划");
        }

        if (entity.getCategory().equals("subject")) {
            image_group_img.setVisibility(View.VISIBLE);
            image_group_img.setText("专题");
        }
    }

    // 图片 平铺 3张
    private void configureImageMulViewHolder(ImageMultipleViewHolder vh, int position) {
        NewsEntity.DataEntity.ListEntity entity = items.get(position);
        TextView image_multiple_title = vh.getImage_multiple_title();
        image_multiple_title.setText(entity.getTitle());
        RadioButton image_multiple_radio_text = vh.getImage_multiple_radio_text();
        image_multiple_radio_text.setText(entity.getPics().getTotal() + "图");
        TextView image_multiple_comments = vh.getImage_multiple_comments();
        image_multiple_comments.setText(entity.getComment() + "评论");


        // 处理图片
        SimpleDraweeView image_multiple_left = vh.getImage_multiple_left();
        SimpleDraweeView image_multiple_center = vh.getImage_multiple_center();
        SimpleDraweeView image_multiple_right = vh.getImage_multiple_right();
        List<NewsEntity.DataEntity.ListEntity.PicsEntity.ListEntityChild> list = entity.getPics().getList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    image_multiple_left.setAspectRatio(1.5f);
                    image_multiple_left.setImageURI(Uri.parse(list.get(i).getKpic()));
                    continue;
                }
                if (i == 1) {
                    image_multiple_center.setAspectRatio(1.5f);
                    image_multiple_center.setImageURI(Uri.parse(list.get(i).getKpic()));
                    continue;
                }
                if (i == 2) {
                    image_multiple_right.setAspectRatio(1.5f);
                    image_multiple_right.setImageURI(Uri.parse(list.get(i).getKpic()));
                    return;
                }
            }
        }


    }

    // 默认
    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
        vh.getLabel().setText(items.get(position).getTitle());
    }

    // 配置 图片轮播的 ViewHolder
    private void configureImageTurnViewHolder(ImageTurnViewHolder vh, int position) {

        // TODO: 2016/1/12 这边获取 viewPager 同时要处理图片轮播
        viewPagerTurn = vh.getNewsItem_Image_viewPager();
        dot_layout = vh.getNewsItem_Image_dot_layout();
        tv_intro = vh.getNewsItem_Image_tv_intro();

        newsItem_radio_text = vh.getNewsItem_radio_text();
        viewPagerTurn.addOnPageChangeListener(this);

        initDots();
        viewPagerTurn.setAdapter(new NewsImageTurnPagerAdapter(context, turnItems));
        // 设置 显示为中间
        int centerValue = Integer.MAX_VALUE / 2;
        int value = centerValue % turnItems.size();
        viewPagerTurn.setCurrentItem(centerValue - value);
        updateIntroAndDot();
        handler.sendEmptyMessageDelayed(0, 4000);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        /**
         * 1.只要有 is_focus 是图片轮播
         *  1.1  feedShowStyle: "common",
         *  1.2 category: "cms","url" 没有小图片
         *  1.2 category: "video" "hdpic" "ad"上面有 小图标  ad 的 feedShowStyle 为 null
         *
         * 2. feedShowStyle: "big_img_show", 就是大图  news_item_imggroup.xml
         *  2.1 图组  category: "hdpic"
         *  2.2 一长图片 category: "url"
         *  2.3 一张图片 category: "plan" 有 策划小按钮
         *  2.4 一张图片 category: "subject" 有专题小按钮
         *
         * 3 feedShowStyle: "common" 且 没有is_focus
         *  3.1 category: "hdpic" 3长图片 图片平铺  news_item_imgmultiple.xml
         *
         *  news_item_imgtext.xml
         *  3.2 category: "plan" 策划图片 显示评论 红色框框
         *  3.3 category: "subject"  专题图片 显示评论  红色框框
         *  3.4 category: "video"  视频 +显示评论  这边判断 有问题
         *  3.5 category: "cms", 显示评论
         *  3.6 category："blog"  博客 不显示评论
         *  3.7 category: "consice" 精读  红色框框
         *
         * 4 feedShowStyle: "null"  news_item_imgtext.xml
         *  4.1 category: "sponsor"  图片文字  赞助  黄色框框
         *
         * 5. video_info 不为null 就显示 news_item_video.xml 视频下有 评论 有按钮 视频
         */

        //  使用 news_item_imgturn.xml
        NewsEntity.DataEntity.ListEntity entity = items.get(position);
        if (entity.isIs_focus()) {
            return IMAGE_TURN;
        } else if (entity.getFeedShowStyle() != null &&
                entity.getFeedShowStyle().equals("common") && entity.getCategory().equals("hdpic")) {
            return IMAGE_MULTIPLE;
        } else if (entity.getFeedShowStyle() != null && entity.getFeedShowStyle().equals("big_img_show")) {
            return IMAGE_GROUP;
        } else if (entity.getFeedShowStyle() == null || (entity.getFeedShowStyle() != null && entity.getFeedShowStyle().equals("common")
                && entity.getVideo_info() == null)) {
            return IMAGE_TEXT;
        } else if (entity.getVideo_info() != null) {
            return VIDEO_MEDIA_PLAYER;
        }
        return -1;
    }

    public void addAll(Collection<? extends NewsEntity.DataEntity.ListEntity> collection, boolean refresh) {
        if (refresh) {
            items.clear();
            turnItems.clear();
            items.addAll(collection);
            // 获取 有 isIs_focus 的数据
            for (NewsEntity.DataEntity.ListEntity entity : items) {
                if (entity.isIs_focus()) {
                    turnItems.add(entity);
                }
            }
            List<NewsEntity.DataEntity.ListEntity> temp = new ArrayList<>();
            for (int i = 0; i < turnItems.size() - 1; i++) {
                temp.add(turnItems.get(i));
            }
            items.removeAll(temp);
        } else {
            items.addAll(collection);
        }
        notifyDataSetChanged();
    }

    private void initDots() {
        if (dot_layout.getChildCount() != 0) {
            dot_layout.removeAllViews();
        }
        for (int i = 0; i < turnItems.size(); i++) {
            Log.d(TAG, "initDots: imageTextTOs==========" + turnItems.size());
            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i != 0) {
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.image_turn_selector_dot);
            dot_layout.addView(view);
        }
    }

    private void updateIntroAndDot() {
        int currentPage = viewPagerTurn.getCurrentItem() % turnItems.size();
        NewsEntity.DataEntity.ListEntity entity = turnItems.get(currentPage);
        if (entity.getCategory().equals("hdpic") || entity.getCategory().equals("ad")
                || entity.getCategory().equals("video") || entity.getCategory().equals("live")) {
            if (entity.getFeedShowStyle() != null && entity.getFeedShowStyle().equals("common")) {
                // 图组
                if (entity.getCategory().equals("hdpic")) {
                    newsItem_radio_text.setVisibility(View.VISIBLE);
                    newsItem_radio_text.setPadding(20, 0, 20, 0);
                    newsItem_radio_text.setText(" " + entity.getPics().getTotal());
                }
                // 视频
                if (entity.getCategory().equals("video")) {
                    newsItem_radio_text.setVisibility(View.VISIBLE);
                    newsItem_radio_text.setPadding(5, 0, 5, 0);
                    newsItem_radio_text.setText(" VIDEO");
                }

                // 直播
                if (entity.getCategory().equals("live")) {
                    newsItem_radio_text.setVisibility(View.VISIBLE);
                    newsItem_radio_text.setPadding(5, 0, 5, 0);
                    newsItem_radio_text.setText(" LIVE");
                }
            } else {
                if (entity.getCategory().equals("ad")) {
                    newsItem_radio_text.setVisibility(View.VISIBLE);
                    newsItem_radio_text.setPadding(5, 0, 5, 0);
                    newsItem_radio_text.setText(" 推广");
                }
            }
        } else {
            newsItem_radio_text.setVisibility(View.GONE);
        }
        tv_intro.setText(entity.getTitle());
        for (int i = 0; i < dot_layout.getChildCount(); i++) {
            dot_layout.getChildAt(i).setEnabled(i == currentPage);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        updateIntroAndDot();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void addTimer() {
        // TODO: 2016/1/4 开子线程
        // 子线程 设置计时器
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 获取总时长
                    int duration = mediaPlayer.getDuration();
                    // 获取当前歌曲播放进度
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    Message message = handlerVideo.obtainMessage();
                    //把进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    message.setData(bundle);
                    handlerVideo.sendMessage(message);
                }
                //开始计时任务后的5毫秒，第一次执行run方法，以后每500毫秒执行一次
            }, 5, 500);
        }
    }


    public String turnTime(int time) {
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String text = sdf.format(d);
        return text;
    }


    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (lastHolder != null && holder instanceof VideoViewHolder && lastHolder.equals(holder)) {
            Log.d(TAG, "onViewDetachedFromWindow: 滑动视频");
            // 正在播放的视频 滚出了屏幕
            mediaPlayer.reset();
            resetHolder(((VideoViewHolder) holder));
            // 释放 holder
            lastHolder = null;
        }
    }

    private void resetHolder(VideoViewHolder holder) {
        Log.d(TAG, "resetHolder: 进来拉滚出屏幕的操作");
        SimpleDraweeView videoPic = holder.getVideoPic();
        videoPic.setImageURI(Uri.parse(mVideoEntity.getKpic()));
        videoPic.setVisibility(View.VISIBLE);
        holder.getVideoPlay().setVisibility(View.VISIBLE);
        holder.getLinearLayoutCorrect().setVisibility(View.INVISIBLE);
    }
}
