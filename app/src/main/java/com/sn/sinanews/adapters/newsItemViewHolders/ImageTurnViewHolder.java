package com.sn.sinanews.adapters.newsItemViewHolders;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sn.sinanews.R;

/**
 * Created by Ming on 2016/1/12.
 */
public class ImageTurnViewHolder extends RecyclerView.ViewHolder {

    private ViewPager newsItem_Image_viewPager;

    private TextView newsItem_Image_tv_intro;

    private LinearLayout newsItem_Image_dot_layout;
    private RadioButton newsItem_radio_text;


    public ImageTurnViewHolder(View itemView) {
        super(itemView);
        newsItem_Image_viewPager =  (ViewPager) itemView.findViewById(R.id.newsItem_Image_viewPager);
        newsItem_Image_tv_intro =  (TextView) itemView.findViewById(R.id.newsItem_Image_tv_intro);
        newsItem_Image_dot_layout =  (LinearLayout) itemView.findViewById(R.id.newsItem_Image_dot_layout);

        newsItem_radio_text = (RadioButton) itemView.findViewById(R.id.newsItem_radio_text);
    }

    public ViewPager getNewsItem_Image_viewPager() {
        return newsItem_Image_viewPager;
    }

    public void setNewsItem_Image_viewPager(ViewPager newsItem_Image_viewPager) {
        this.newsItem_Image_viewPager = newsItem_Image_viewPager;
    }

    public TextView getNewsItem_Image_tv_intro() {
        return newsItem_Image_tv_intro;
    }

    public void setNewsItem_Image_tv_intro(TextView newsItem_Image_tv_intro) {
        this.newsItem_Image_tv_intro = newsItem_Image_tv_intro;
    }

    public LinearLayout getNewsItem_Image_dot_layout() {
        return newsItem_Image_dot_layout;
    }

    public void setNewsItem_Image_dot_layout(LinearLayout newsItem_Image_dot_layout) {
        this.newsItem_Image_dot_layout = newsItem_Image_dot_layout;
    }


    public RadioButton getNewsItem_radio_text() {
        return newsItem_radio_text;
    }

    public void setNewsItem_radio_text(RadioButton newsItem_radio_text) {
        this.newsItem_radio_text = newsItem_radio_text;
    }
}
