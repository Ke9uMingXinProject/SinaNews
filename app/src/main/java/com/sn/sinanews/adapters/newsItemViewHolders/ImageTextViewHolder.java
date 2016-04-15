package com.sn.sinanews.adapters.newsItemViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;

/**
 * Created by Ming on 2016/1/13.
 */
public class ImageTextViewHolder extends RecyclerView.ViewHolder {


    private SimpleDraweeView image_text_image;
    private TextView image_text_title;
    private TextView image_text_content;
    private TextView image_text_comments;
    private TextView image_text_img;
    private ImageButton image_text_video;
    public ImageTextViewHolder(View itemView) {
        super(itemView);
        image_text_image = (SimpleDraweeView) itemView.findViewById(R.id.image_text_image);
        image_text_title = (TextView) itemView.findViewById(R.id.image_text_title);
        image_text_content = (TextView) itemView.findViewById(R.id.image_text_content);
        image_text_comments = (TextView) itemView.findViewById(R.id.image_text_comments);
        image_text_img = (TextView) itemView.findViewById(R.id.image_text_img);
        image_text_video = (ImageButton) itemView.findViewById(R.id.image_text_video);
    }

    public SimpleDraweeView getImage_text_image() {
        return image_text_image;
    }

    public void setImage_text_image(SimpleDraweeView image_text_image) {
        this.image_text_image = image_text_image;
    }

    public TextView getImage_text_title() {
        return image_text_title;
    }

    public void setImage_text_title(TextView image_text_title) {
        this.image_text_title = image_text_title;
    }

    public TextView getImage_text_content() {
        return image_text_content;
    }

    public void setImage_text_content(TextView image_text_content) {
        this.image_text_content = image_text_content;
    }

    public TextView getImage_text_comments() {
        return image_text_comments;
    }

    public void setImage_text_comments(TextView image_text_comments) {
        this.image_text_comments = image_text_comments;
    }

    public TextView getImage_text_img() {
        return image_text_img;
    }

    public void setImage_text_img(TextView image_text_img) {
        this.image_text_img = image_text_img;
    }

    public ImageButton getImage_text_video() {
        return image_text_video;
    }

    public void setImage_text_video(ImageButton image_text_video) {
        this.image_text_video = image_text_video;
    }
}
