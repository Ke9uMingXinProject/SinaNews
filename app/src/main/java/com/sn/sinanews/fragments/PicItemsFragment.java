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
import com.sn.sinanews.adapters.PicItemAdapter;
import com.sn.sinanews.custom.SpaceItemDecoration;
import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.entities.PicItems;
import com.sn.sinanews.services.BaseService;
import com.sn.sinanews.services.ServiceFlag;
import com.sn.sinanews.services.ServiceRefreshFragment;
import com.sn.sinanews.services.ServiceSendTouchAction;
import com.sn.sinanews.services.ServiceSnackBarAppear;
import com.sn.sinanews.tools.HttpUtils;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PicItemsFragment extends Fragment implements Callback<PicItems>, SwipeRefreshLayout.OnRefreshListener,ServiceRefreshFragment, View.OnTouchListener,ServiceSendTouchAction {
    private static final String TABTITLENAME = "tabTitleName";
    private SwipeRefreshLayout refreshLoadLayout;
    private boolean isRefresh;
    private int lastPullTimes = 0;
    private PicItemAdapter adapter;
    private int lastVisibleItem;
    private RecyclerView recyclerView;
    private ServiceSnackBarAppear serviceSnackBarAppear;
    private ServiceFlag mServiceFlag;
    private int mStartY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static PicItemsFragment newInstance(NewsTO mNewsTO) {
        Bundle args = new Bundle();
        PicItemsFragment fragment = new PicItemsFragment();
        args.putString(TABTITLENAME, mNewsTO.getTitleParam());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pic_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ServiceSnackBarAppear
        serviceSnackBarAppear = (PicturesFragment) getParentFragment();

        refreshLoadLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_pic_items_swipeRefreshLayout);
        refreshLoadLayout.setColorSchemeResources(R.color.SinaNewsDefault);
        refreshLoadLayout.setOnRefreshListener(this);
//        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fragment_pic_items_floatingActionButton);
//        floatingActionButton.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_pic_items_recyclerView);
        recyclerView.addItemDecoration(new SpaceItemDecoration(12));
        adapter = new PicItemAdapter(getContext(),getArguments().getString(TABTITLENAME),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(this);
        getMessages(0);

        //RecyclerView的上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断是不是向下滑动 且 当前显示的最下面那一项是不是adapter中最后一个
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    refreshLoadLayout.setRefreshing(true);
                    //pulltimes加1记录
                    lastPullTimes++;
                    getMessages(lastPullTimes);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //滑动屏幕，最下面出现新的一条的时候，记录为lastVisibleItem
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }


    //连接网络获取数据的方法
    private void getMessages(int pull_times) {
        String channel = getArguments().getString(TABTITLENAME);
        if (pull_times == 0) {
            isRefresh = true;
            lastPullTimes = 0;
        } else {
            isRefresh = false;
        }
        BaseService service = HttpUtils.getService();
        Call<PicItems> call = service.getPicsList(channel, pull_times, "up", 6049295012L);
        call.enqueue(this);
    }

    //下拉刷新的监听
    @Override
    public void onRefresh() {
        getMessages(0);
    }

    //访问网络的回调
    @Override
    public void onResponse(Response<PicItems> response, Retrofit retrofit) {
        List<PicItems.DataEntity.ListEntity> listEntityList = response.body().getData().getList();
        adapter.addAll(listEntityList, isRefresh);
        if (refreshLoadLayout.isRefreshing()) {
            refreshLoadLayout.setRefreshing(false);
            if (lastPullTimes == 0) {
//                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
//                showSnackBar("刷新成功");
                //尝试调用接口
//                MainActivity.SnackAppear snackAppear = new MainActivity().new SnackAppear();
//                snackAppear.setOnSnackBarAppearListener(new MainActivity.OnSnackBarAppearListener() {
//                    @Override
//                    public void onClick(MainActivity.SnackAppear mainActivity) {
//                        Log.e(TAG, "接口回调");
//                    }
//                });
//                snackAppear.click("刷新成功");
                serviceSnackBarAppear.snackAppear("刷新成功");
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        if (refreshLoadLayout.isRefreshing()) {
            refreshLoadLayout.setRefreshing(false);
        }
//        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
//        showSnackBar("网络错误");
        if (serviceSnackBarAppear != null) {
            serviceSnackBarAppear.snackAppear("网络错误");
        }
    }


    @Override
    public void clickRefresh() {
        //回到顶部
        refreshLoadLayout.setRefreshing(true);
        recyclerView.scrollToPosition(0);
        getMessages(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiceFlag) {
            mServiceFlag = (ServiceFlag) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                Log.e("是否下拉", "开始的Y："+mStartY+"，停止的Y："+currentY);
                if (currentY - mStartY > minMove) {
                    mServiceFlag.returnFlag(true);
                } else if (mStartY - currentY > minMove) {
                    mServiceFlag.returnFlag(false);
                }
                break;
        }
        return false;
    }

    @Override
    public void sendAction(int current) {
        mStartY = current;
    }
}
