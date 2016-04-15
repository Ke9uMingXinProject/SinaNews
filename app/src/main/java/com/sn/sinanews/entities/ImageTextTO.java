package com.sn.sinanews.entities;

/**
 * Created by Ming on 2016/1/12.
 */
public class ImageTextTO {

    private String imageUrl;
    private String imageText;

    public ImageTextTO() {
    }

    public ImageTextTO(String imageUrl, String imageText) {
        this.imageUrl = imageUrl;
        this.imageText = imageText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }
}
