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
import com.sn.sinanews.adapters.VideoItemPagerAdapter;
import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.services.ServiceRefreshFragment;
import com.sn.sinanews.services.ServiceSnackBarAppear;
import com.sn.sinanews.services.ServiceToActivity;
import com.sn.sinanews.tools.IndicateUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements ServiceSnackBarAppear, View.OnClickListener, ViewPager.OnPageChangeListener {
    private List<NewsTO> mNewsTOList;
    private CoordinatorLayout mLayout;
    private ViewPager mViewPager;
    private VideoItemPagerAdapter mAdapter;
    private TextView mToolBarTitle;
    private LinearLayout mToolBarLinearLayout;
    private TextView mToolBarIntro;
    private LinearLayout mToolBarHorinzontalPoint;
    private ServiceToActivity mServiceToActivity;

    private void initData(){
        mNewsTOList = new ArrayList<>();
        //private String[] titles = {"笑cry","震惊","暖心","八卦"};
        mNewsTOList.add(new NewsTO("笑cry","video_video"));
        mNewsTOList.add(new NewsTO("震惊","video_highlights"));
        mNewsTOList.add(new NewsTO("暖心","video_scene"));
        mNewsTOList.add(new NewsTO("八卦","video_funny"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mLayout = (CoordinatorLayout) view.findViewById(R.id.video_coordinatorLayout);
        mViewPager = (ViewPager) view.findViewById(R.id.video_viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.video_tabLayout);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.video_floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        FragmentManager childFragmentManager = getChildFragmentManager();
        mAdapter = new VideoItemPagerAdapter(childFragmentManager,mNewsTOList);
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.video_toolBar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu);
        toolbar.setTitle("");
        mToolBarTitle = (TextView) view.findViewById(R.id.video_toolBar_title);
        mToolBarLinearLayout = (LinearLayout)view.findViewById(R.id.video_toolBar_linearLayout);
        mToolBarIntro = (TextView) view.findViewById(R.id.video_toolBar_intro);
        mToolBarHorinzontalPoint = (LinearLayout) view.findViewById(R.id.video_toolBar_horizontalPoint);
        mServiceToActivity.returnLayout(mToolBarLinearLayout,mToolBarTitle);

        //初始化导航栏
        IndicateUtils.initDots(mNewsTOList,getActivity(),mToolBarHorinzontalPoint,R.drawable.selector_dot);
        mViewPager.addOnPageChangeListener(this);
        IndicateUtils.updateIntroAndDot(mViewPager,mNewsTOList,mToolBarIntro,mToolBarHorinzontalPoint);
    }

    @Override
    public void snackAppear(String text) {
        Snackbar snackbar = Snackbar.make(mLayout, text, Snackbar.LENGTH_SHORT);
        //白底的SnackBar样式的方法
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(Color.parseColor("#fdfdfe"));
        ((TextView)snackBarLayout.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.parseColor("#ef3744"));
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        int currentItem = mViewPager.getCurrentItem();
        Fragment item = mAdapter.getItem(currentItem);
        if (item instanceof ServiceRefreshFragment) {
            ServiceRefreshFragment serviceRefreshFragment = (ServiceRefreshFragment) item;
            serviceRefreshFragment.clickRefresh();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("换页", "换页了");
        IndicateUtils.updateIntroAndDot(mViewPager,mNewsTOList,mToolBarIntro,mToolBarHorinzontalPoint);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiceToActivity){
            mServiceToActivity = (ServiceToActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mServiceToActivity = null;
    }
}
