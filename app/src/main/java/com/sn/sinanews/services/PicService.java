package com.sn.sinanews.services;

import com.sn.sinanews.entities.PicItems;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by 卜令壮
 * on 2016/1/11
 * E-mail q137617549@qq.com
 */
public interface PicService {
    //
    @GET("sinago/list.json")
    Call<PicItems> getList(@Query("channel") String channel,
                           @Query("pull_times") int pull_times,
                           @Query("pull_direction") String pull_direction,
                           @Query("from") long from);
}
