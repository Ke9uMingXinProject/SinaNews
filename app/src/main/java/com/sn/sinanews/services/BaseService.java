package com.sn.sinanews.services;

import com.sn.sinanews.entities.NewsEntity;
import com.sn.sinanews.entities.PicItemReveal;
import com.sn.sinanews.entities.PicItems;
import com.sn.sinanews.entities.VideoEntity;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by 张坤
 * on 2016/1/11
 * E-mail
 */
public interface BaseService {

    // 获取新闻
    // http://api.sina.cn/sinago/list.json?behavior=auto&pull_times=1&pull_direction=down&uid=3b45760d901c8f15&channel=news_toutiao
    @GET("sinago/list.json")
    Call<NewsEntity> getNewsList(@Query("behavior") String behavior,
                                 @Query("channel") String channel,
                                 @Query("pull_times") int pull_times,
                                 @Query("pull_direction") String pull_direction,
                                 @Query("from") long from);

    // 获取推荐
    // http://api.sina.cn/sinago/list.json?uid=3b45760d901c8f15&channel=news_tuijian
    @GET("sinago/list.json")
    Call<NewsEntity> getNewsRecommendList(@Query("uid") String uid,
                                          @Query("behavior") String behavior,
                                          @Query("channel") String channel,
                                          @Query("pull_times") int pull_times,
                                          @Query("pull_direction") String pull_direction,
                                          @Query("from") long from);


    // 获取图片
    @GET("sinago/list.json")
    Call<PicItems> getPicsList(@Query("channel") String channel,
                               @Query("pull_times") int pull_times,
                               @Query("pull_direction") String pull_direction,
                               @Query("from") long from);
    //点击图片显示详细信息
    //http://api.sina.cn/sinago/articlev2.json?&id=704-130292-hdpic&from=6049295012&postt=hdpic_hdpic_pretty_feed_1
    @GET("sinago/articlev2.json")
    Call<PicItemReveal> getPicRevealList(@Query("id") String id,
                                @Query("from") String from,
                                @Query("postt") String postt);

    // 获取视频
    @GET("sinago/list.json")
    Call<VideoEntity> getVideoList(@Query("channel") String channel,
                                   @Query("pull_times") int pull_times,
                                   @Query("pull_direction") String pull_direction,
                                   @Query("from") long from);

    // 获取评论



}



