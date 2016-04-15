package com.sn.sinanews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sn.sinanews.R;
import com.sn.sinanews.adapters.VideoItemAdapter;
import com.sn.sinanews.custom.SpaceItemDecoration;
import com.sn.sinanews.entities.VideoEntity;
import com.sn.sinanews.services.BaseService;
import com.sn.sinanews.services.ServiceFlag;
import com.sn.sinanews.services.ServiceRefreshFragment;
import com.sn.sinanews.services.ServiceSnackBarAppear;
import com.sn.sinanews.tools.HttpUtils;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class VideoItemsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<VideoEntity>,ServiceRefreshFragment, View.OnTouchListener {
    private static final String TAG = VideoItemsFragment.class.getSimpleName();
    private static final String TABTITLENAME = "tabTitleName";
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private VideoItemAdapter adapter;
    private boolean isRefresh;
    private int lastPullTimes;
    private int lastVisibleItem;
    private ServiceSnackBarAppear serviceSnackBarAppear;
    private ServiceFlag mServiceFlag;
    private int mStartY;

    public static VideoItemsFragment newInstance(String tabTitleName) {
        Bundle args = new Bundle();
        VideoItemsFragment fragment = new VideoItemsFragment();
        args.putString(TABTITLENAME,tabTitleName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceSnackBarAppear = (VideoFragment) getParentFragment();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_video_items_swipeRefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.SinaNewsDefault);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_video_items_recyclerView);
        //设置每个Item之间的间距
        recyclerView.addItemDecoration(new SpaceItemDecoration(12));
        adapter = new VideoItemAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(this);
        getMessages(0);
        //上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem +1 ==adapter.getItemCount()){
                    refreshLayout.setRefreshing(true);
                    lastPullTimes++;
                    getMessages(lastPullTimes);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void getMessages(int pull_times) {
        String tabTitleName = getArguments().getString(TABTITLENAME);
        String channel = "";
        if (tabTitleName != null) {
            switch (tabTitleName) {
                case "笑cry":
                    channel = "video_video";
                    break;
                case "震惊":
                    channel = "video_highlights";
                    break;
                case "暖心":
                    channel = "video_scene";
                    break;
                case "八卦":
                    channel = "video_funny";
                    break;
            }
        }
        if (pull_times == 0) {
            isRefresh = true;
            lastPullTimes = 0;
        }else {
            isRefresh = false;
        }
        BaseService service = HttpUtils.getService();
        Call<VideoEntity> call = service.getVideoList(channel, pull_times, "up", 6049295012L);
        call.enqueue(this);
    }

    @Override
    public void onRefresh() {
        getMessages(0);
    }

    @Override
    public void onResponse(Response<VideoEntity> response, Retrofit retrofit) {
        List<VideoEntity.DataEntity.ListEntity> listEntityList = response.body().getData().getList();
        adapter.addAll(listEntityList,isRefresh);
        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
            if (lastPullTimes == 0){
                serviceSnackBarAppear.snackAppear("刷新成功");
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (serviceSnackBarAppear != null) {
            serviceSnackBarAppear.snackAppear("网络错误");
        }
    }

    @Override
    public void clickRefresh() {
        Log.e(TAG, "回调的刷新");
        refreshLayout.setRefreshing(true);
        recyclerView.scrollToPosition(0);
        getMessages(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiceSnackBarAppear) {
            serviceSnackBarAppear = (ServiceSnackBarAppear) context;
        }
        if (context instanceof ServiceFlag){
            mServiceFlag = (ServiceFlag) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        serviceSnackBarAppear = null;
        mServiceFlag = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float minMove = 20;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int currentY = (int) event.getY();
                if (currentY - mStartY > minMove) {
                    mServiceFlag.returnFlag(true);
                } else if (mStartY - currentY > minMove) {
                    mServiceFlag.returnFlag(false);
                }
                break;
        }
        return false;
    }
}
