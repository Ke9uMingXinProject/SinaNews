package com.sn.sinanews.adapters.newsItemViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;

/**
 * Created by Ming on 2016/1/13.
 */
public class ImageGroupBigViewHolder extends RecyclerView.ViewHolder {

    private TextView image_group_title;
    private RadioButton image_group_radio_text;
    private SimpleDraweeView image_group_image;
    private TextView image_group_comments;
    private TextView image_group_img;

    public ImageGroupBigViewHolder(View itemView) {
        super(itemView);
        image_group_title = (TextView) itemView.findViewById(R.id.image_group_title);
        image_group_comments = (TextView) itemView.findViewById(R.id.image_group_comments);
        image_group_img = (TextView) itemView.findViewById(R.id.image_group_img);
        image_group_radio_text = (RadioButton) itemView.findViewById(R.id.image_group_radio_text);
        image_group_image = (SimpleDraweeView) itemView.findViewById(R.id.image_group_image);
    }

    public TextView getImage_group_title() {
        return image_group_title;
    }

    public void setImage_group_title(TextView image_group_title) {
        this.image_group_title = image_group_title;
    }

    public RadioButton getImage_group_radio_text() {
        return image_group_radio_text;
    }

    public void setImage_group_radio_text(RadioButton image_group_radio_text) {
        this.image_group_radio_text = image_group_radio_text;
    }

    public SimpleDraweeView getImage_group_image() {
        return image_group_image;
    }

    public void setImage_group_image(SimpleDraweeView image_group_image) {
        this.image_group_image = image_group_image;
    }

    public TextView getImage_group_comments() {
        return image_group_comments;
    }

    public void setImage_group_comments(TextView image_group_comments) {
        this.image_group_comments = image_group_comments;
    }

    public TextView getImage_group_img() {
        return image_group_img;
    }

    public void setImage_group_img(TextView image_group_img) {
        this.image_group_img = image_group_img;
    }
}
