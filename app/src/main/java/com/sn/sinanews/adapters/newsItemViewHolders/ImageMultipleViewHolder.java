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
public class ImageMultipleViewHolder extends RecyclerView.ViewHolder {

    private TextView image_multiple_title;
    private TextView image_multiple_comments;
    private RadioButton image_multiple_radio_text;
    private SimpleDraweeView image_multiple_left;
    private SimpleDraweeView image_multiple_center;
    private SimpleDraweeView image_multiple_right;

    public ImageMultipleViewHolder(View itemView) {
        super(itemView);
        image_multiple_title = (TextView) itemView.findViewById(R.id.image_multiple_title);
        image_multiple_comments = (TextView) itemView.findViewById(R.id.image_multiple_comments);
        image_multiple_radio_text = (RadioButton) itemView.findViewById(R.id.image_multiple_radio_text);
        image_multiple_left = (SimpleDraweeView) itemView.findViewById(R.id.image_multiple_left);
        image_multiple_center = (SimpleDraweeView) itemView.findViewById(R.id.image_multiple_center);
        image_multiple_right = (SimpleDraweeView) itemView.findViewById(R.id.image_multiple_right);


    }

    public TextView getImage_multiple_title() {
        return image_multiple_title;
    }

    public void setImage_multiple_title(TextView image_multiple_title) {
        this.image_multiple_title = image_multiple_title;
    }

    public TextView getImage_multiple_comments() {
        return image_multiple_comments;
    }

    public void setImage_multiple_comments(TextView image_multiple_comments) {
        this.image_multiple_comments = image_multiple_comments;
    }

    public RadioButton getImage_multiple_radio_text() {
        return image_multiple_radio_text;
    }

    public void setImage_multiple_radio_text(RadioButton image_multiple_radio_text) {
        this.image_multiple_radio_text = image_multiple_radio_text;
    }

    public SimpleDraweeView getImage_multiple_left() {
        return image_multiple_left;
    }

    public void setImage_multiple_left(SimpleDraweeView image_multiple_left) {
        this.image_multiple_left = image_multiple_left;
    }

    public SimpleDraweeView getImage_multiple_center() {
        return image_multiple_center;
    }

    public void setImage_multiple_center(SimpleDraweeView image_multiple_center) {
        this.image_multiple_center = image_multiple_center;
    }

    public SimpleDraweeView getImage_multiple_right() {
        return image_multiple_right;
    }

    public void setImage_multiple_right(SimpleDraweeView image_multiple_right) {
        this.image_multiple_right = image_multiple_right;
    }
}
