package com.sn.sinanews.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sn.sinanews.adapters.PicItemPagerAdapter;
import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.services.ServiceRefreshFragment;
import com.sn.sinanews.services.ServiceSnackBarAppear;
import com.sn.sinanews.services.ServiceToActivity;
import com.sn.sinanews.tools.IndicateUtils;

import java.util.ArrayList;
import java.util.List;

public class PicturesFragment extends Fragment implements ServiceSnackBarAppear, View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = PicturesFragment.class.getSimpleName();
    private CoordinatorLayout layout;
    private PicItemPagerAdapter mAdapter;
    private ViewPager viewPager;
    private LinearLayout mToolbarLinearLayout;
    private TextView mToolBarIntro;
    private LinearLayout mToolBarHorinzontalPoint;
    private ServiceToActivity mServiceToActivity;
    private TextView mToolBarTitle;
    private List<NewsTO> mNewsTOList;

    public PicturesFragment() {
        // Required empty public constructor
    }
    public void initData(){
        mNewsTOList = new ArrayList<>();
        //private String[] titles = {"精选","奇趣","明星","竞技"};
        mNewsTOList.add(new NewsTO("精选","hdpic_toutiao"));
        mNewsTOList.add(new NewsTO("奇趣","hdpic_funny"));
        mNewsTOList.add(new NewsTO("明星","hdpic_pretty"));
        mNewsTOList.add(new NewsTO("竞技","hdpic_story"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pictures, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        layout = (CoordinatorLayout) view.findViewById(R.id.pic_coordinatorLayout);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.pic_floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.pic_viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.pic_tabLayout);
        FragmentManager childFragmentManager = getChildFragmentManager();
        mAdapter = new PicItemPagerAdapter(childFragmentManager,mNewsTOList);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.pic_toolBar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu);
        toolbar.setTitle("");
        mToolBarTitle = (TextView) view.findViewById(R.id.pic_toolBar_title);
        mToolbarLinearLayout = (LinearLayout) view.findViewById(R.id.pic_toolBar_linearLayout);
        mToolBarIntro = (TextView) view.findViewById(R.id.pic_toolBar_intro);
        mToolBarHorinzontalPoint = (LinearLayout) view.findViewById(R.id.pic_toolBar_horizontalPoint);
        mServiceToActivity.returnLayout(mToolbarLinearLayout,mToolBarTitle);

        //初始化导航栏
        IndicateUtils.initDots(mNewsTOList,getActivity(),mToolBarHorinzontalPoint,R.drawable.selector_dot);
        viewPager.addOnPageChangeListener(this);
        IndicateUtils.updateIntroAndDot(viewPager,mNewsTOList,mToolBarIntro,mToolBarHorinzontalPoint);
    }

    @Override
    public void snackAppear(String text) {
        Snackbar snackbar = Snackbar.make(layout, text, Snackbar.LENGTH_SHORT);
        //白底的SnackBar样式的方法
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(Color.parseColor("#fdfdfe"));
        ((TextView) snackBarLayout.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.parseColor("#ef3744"));
        snackbar.show();
    }

    @Override
    public void onClick(View v) {

        int currentItem = viewPager.getCurrentItem();

        Fragment fragment = mAdapter.getItem(currentItem);

        Log.d(TAG, "onClick: " + currentItem + " " + fragment);

        if (fragment instanceof ServiceRefreshFragment) {
            ServiceRefreshFragment refresh = (ServiceRefreshFragment) fragment;
            refresh.clickRefresh();
        }

//        if (serviceRefreshFragment != null){
//            serviceRefreshFragment.clickRefresh();
//        }
    }

    //ViewPager切换的监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        IndicateUtils.updateIntroAndDot(viewPager,mNewsTOList,mToolBarIntro,mToolBarHorinzontalPoint);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiceToActivity) {
            mServiceToActivity = (ServiceToActivity) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mServiceToActivity = null;
    }
}
