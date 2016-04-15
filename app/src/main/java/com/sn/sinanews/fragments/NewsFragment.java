package com.sn.sinanews.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sn.sinanews.R;
import com.sn.sinanews.adapters.NewsItemPagerAdapter;
import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.services.ServiceRefreshFragment;
import com.sn.sinanews.services.ServiceSnackBarAppear;
import com.sn.sinanews.services.ServiceToActivity;
import com.sn.sinanews.tools.IndicateUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener,ServiceSnackBarAppear {

    private static final String TAG = "NewsFragment";
    private List<NewsTO> newsTOs;
    private TabLayout toolbar_tabLayout;
    private ViewPager newsItem_viewpager;
    private TextView toolbar_intro;
    private LinearLayout toolbar_dot_layout;

    private ServiceToActivity serviceToActivity;
    private TextView toolbar_title;
    private LinearLayout linearLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private NewsItemPagerAdapter mAdapter;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    // 初始化数据
    private void initData() {
        // TODO: 2016/1/11 为后面添加关注的项目做准备，添加项目需要接口回调 更新 list
        newsTOs = new ArrayList<>();
        newsTOs.add(new NewsTO("头条", "news_toutiao"));
       /* newsTOs.add(new NewsTO("推荐", "news_tuijian"));*/
        newsTOs.add(new NewsTO("娱乐", "news_ent"));
        newsTOs.add(new NewsTO("汽车", "news_auto"));
        newsTOs.add(new NewsTO("体育", "news_sports"));
        newsTOs.add(new NewsTO("财经", "news_finance"));
        newsTOs.add(new NewsTO("科技", "news_tech"));
        newsTOs.add(new NewsTO("搞笑", "news_funny"));
    }

    // 初始化 view
    private void initView(View view) {
        // 设置 toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.third_activity_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.ic_menu);
        // 标题 toolbar  默认显示 项目名称
        toolbar_title = (TextView) view.findViewById(R.id.id_toolbar_title);
        // 切换标题
        linearLayout = (LinearLayout) view.findViewById(R.id.id_toolbar_linear);

        toolbar_tabLayout = (TabLayout) view.findViewById(R.id.id_toolbar_layout);
        newsItem_viewpager = (ViewPager) view.findViewById(R.id.newsItem_viewpager);

        // toolbar tabLayout 显示 指示文本
        toolbar_intro = (TextView) view.findViewById(R.id.id_toolbar_intro);
        toolbar_dot_layout = (LinearLayout) view.findViewById(R.id.id_toolbar_dot_layout);
        // 获取协调者布局
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.main_content_CoordinatorLayout);
        // 获取 悬浮button
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.news_floatingActionButton);
        floatingActionButton.setOnClickListener(this);


        // 回调接口
        serviceToActivity.returnLayout(linearLayout, toolbar_title);
        // 初始化导航栏指示
        IndicateUtils.initDots(newsTOs, getActivity(), toolbar_dot_layout, R.drawable.selector_dot);
        FragmentManager fm = getChildFragmentManager();
        mAdapter = new NewsItemPagerAdapter(fm, newsTOs);
        newsItem_viewpager.setAdapter(mAdapter);
        newsItem_viewpager.addOnPageChangeListener(this);
        toolbar_tabLayout.setupWithViewPager(newsItem_viewpager);
        // 更新对应的数据
        IndicateUtils.updateIntroAndDot(newsItem_viewpager, newsTOs, toolbar_intro, toolbar_dot_layout);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 更新对应的数据
        IndicateUtils.updateIntroAndDot(newsItem_viewpager, newsTOs, toolbar_intro, toolbar_dot_layout);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiceToActivity) {
            serviceToActivity = (ServiceToActivity) context;
            Log.d(TAG, "onAttach: " + context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        serviceToActivity = null;
    }

    // 悬浮button 的点击事件  这边要用接口回调 父类掉子类
    @Override
    public void onClick(View v) {
        int currentItem = newsItem_viewpager.getCurrentItem();

        Fragment fragment = mAdapter.getItem(currentItem);
        if (fragment instanceof ServiceRefreshFragment) {
            ServiceRefreshFragment refresh = (ServiceRefreshFragment) fragment;
            refresh.clickRefresh();
        }
    }

    @Override
    public void snackAppear(String text) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_SHORT);
        //白底的SnackBar样式的方法
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(Color.parseColor("#fdfdfe"));
        ((TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.parseColor("#ef3744"));
        snackbar.show();
    }
}
