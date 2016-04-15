package com.sn.sinanews.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.sn.sinanews.adapters.NewsItemComplexAdapter;
import com.sn.sinanews.custom.SpaceItemDecoration;
import com.sn.sinanews.entities.NewsEntity;
import com.sn.sinanews.services.ServiceFlag;
import com.sn.sinanews.services.ServiceRefreshFragment;
import com.sn.sinanews.services.ServiceSnackBarAppear;
import com.sn.sinanews.tools.HttpUtils;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NewsItemFragment extends Fragment implements View.OnTouchListener, Callback<NewsEntity>, SwipeRefreshLayout.OnRefreshListener,ServiceRefreshFragment {

    private static final String ARG_PARAM = "titleParam";
    private static final String TAG = "NewsItemFragment";
    private RecyclerView recyclerView;
    private NewsItemComplexAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    private ServiceFlag serviceFlag;
    private ServiceSnackBarAppear serviceSnackBarAppear;

    private SwipeRefreshLayout refreshLoadLayout;


    // 获取拉下的次数
    private int downTime;
    private int upTime;
    private String pull_direction;
    private String behavior;
    private boolean upFlag;
    private boolean downFlag;

    // 记录展示了多少条数据
    private int lastVisibleItem;

    private boolean flag;
    private Call<NewsEntity> newsList;
    private int startY;
    private String mChannel;


    public NewsItemFragment() {
        // Required empty public constructor
    }

    public static NewsItemFragment newInstance(String titleParam) {
        NewsItemFragment fragment = new NewsItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, titleParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 获取  snakebar
        NewsFragment parentFragment = (NewsFragment) getParentFragment();
        serviceSnackBarAppear = parentFragment;

        // 获取下拉刷新
        refreshLoadLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_news_items_swipeRefreshLayout);
        refreshLoadLayout.setColorSchemeResources(R.color.SinaNewsDefault);
        refreshLoadLayout.setOnRefreshListener(this);
        refreshLoadLayout.setRefreshing(true);
        downFlag = true;
        // 获取 url
        mChannel = (String) getArguments().get(ARG_PARAM);
        // 初始化次数
        downTime = 1;
        // TODO: 2016/1/12 当刷新的时候设置 pull_direction 的方向 还有 加载的时候 设置
        pull_direction = "down";
        behavior = "auto";

        Log.d(TAG, "onViewCreated: " + getArguments().get(ARG_PARAM));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SpaceItemDecoration(1));
        // TODO: 2016/1/12 获取数据  初始化adapter
        if (mChannel.equals("news_tuijian")) {

        } else {
            newsList = HttpUtils.getService().getNewsList(behavior, mChannel, downTime, pull_direction, 6049295012l);
        }
        newsList.enqueue(this);
        adapter = new NewsItemComplexAdapter(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnTouchListener(this);
        // 上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断是不是向下滑动 且 当前显示的最下面那一项是不是adapter中最后一个
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    refreshLoadLayout.setRefreshing(true);
                    //pulltimes加1记录
                    upTime++;
                    behavior = "manual";
                    pull_direction = "up";
                    HttpUtils.getService().getNewsList(behavior, mChannel,
                            upTime, pull_direction, 6049295012l).enqueue(NewsItemFragment.this);
                    upFlag = true;
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
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiceFlag) {
            serviceFlag = (ServiceFlag) context;
            Log.d(TAG, "onAttach: " + context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        serviceFlag = null;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float minMove = 20;
        switch (action) {
            //用户手指摸到屏幕
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                break;
            //用户手指正在滑动
            case MotionEvent.ACTION_MOVE:
                break;
            //用户手指离开屏幕
            case MotionEvent.ACTION_UP:
                int y = (int) event.getY();
                if (y - startY > minMove) {
                    flag = true;
                    serviceFlag.returnFlag(flag);
                } else if (startY - y > minMove) {
                    flag = false;
                    serviceFlag.returnFlag(flag);
                }
                break;
        }
        return false;
    }

    //  进行数据的传输
    @Override
    public void onResponse(Response<NewsEntity> response, Retrofit retrofit) {

        // TODO: 2016/1/12 这是需要处理数据的 次数 和 方向
        Log.d(TAG, "onResponse: " + response.body().getData().getList());
        if (downFlag){
            adapter.addAll(response.body().getData().getList(),downFlag);
            refreshLoadLayout.setRefreshing(false);
            downFlag = false;
            serviceSnackBarAppear.snackAppear("刷新成功");
        }
        if (upFlag){
            adapter.addAll(response.body().getData().getList(),!upFlag);
            upFlag = false;
            refreshLoadLayout.setRefreshing(false);
        }

    }
    @Override
    public void onFailure(Throwable t) {
        if (refreshLoadLayout.isRefreshing()) {
            refreshLoadLayout.setRefreshing(false);
        }
        t.printStackTrace();
        serviceSnackBarAppear.snackAppear("网络错误");

    }

    @Override
    public void onRefresh() {
        downTime++;
        Log.d(TAG, "onRefresh: "+downTime);
        pull_direction = "down";
        behavior = "manual";
        HttpUtils.getService().getNewsList(behavior, mChannel,
                downTime, pull_direction, 6049295012l).enqueue(this);
        downFlag = true;
    }

    @Override
    public void clickRefresh() {
        refreshLoadLayout.setRefreshing(true);
        recyclerView.scrollToPosition(0);
        downTime++;
        Log.d(TAG, "onRefresh: "+downTime);
        pull_direction = "down";
        behavior = "manual";
        HttpUtils.getService().getNewsList(behavior, mChannel,
                downTime, pull_direction, 6049295012l).enqueue(this);
        downFlag = true;
    }
}
