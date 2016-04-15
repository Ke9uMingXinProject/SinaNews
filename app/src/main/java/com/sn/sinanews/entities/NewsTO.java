package com.sn.sinanews.entities;

/**
 * Created by Ming on 2016/1/11.
 */
public class NewsTO {

    private String title;
    private String titleParam;

    public NewsTO() {
    }

    public NewsTO(String title, String titleParam) {
        this.title = title;
        this.titleParam = titleParam;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleParam() {
        return titleParam;
    }

    public void setTitleParam(String titleParam) {
        this.titleParam = titleParam;
    }
}
