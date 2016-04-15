package com.sn.sinanews;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sn.sinanews.fragments.NewsFragment;
import com.sn.sinanews.fragments.PicturesFragment;
import com.sn.sinanews.fragments.VideoFragment;
import com.sn.sinanews.services.ServiceFlag;
import com.sn.sinanews.services.ServiceToActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ServiceFlag,ServiceToActivity {
    long fristTime = 0L;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggle;

    private NewsFragment newsFragment;
    private PicturesFragment picturesFragment;
    private VideoFragment videoFragment;

    private List<Fragment> fragments;

    private TextView toolbar_title;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initView();
        init();
        //初始化Fresco
        Fresco.initialize(this);
        // 初始化显示fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        transaction.replace(R.id.frame_container, newsFragment);
        transaction.commit();
    }

    // 初始化 toolbar
    private void initToolBar() {

/*        Toolbar toolbar = (Toolbar)findViewById(R.id.third_activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu);*/
    }

    // 初始化 view
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_DrawLayout);
        mNavigationView = (NavigationView) findViewById(R.id.main_Navigation);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mToggle.syncState();
        mDrawerLayout.setDrawerListener(mToggle);
    }

    private void init(){
        fragments = new ArrayList<>();
        newsFragment = new NewsFragment();
        fragments.add(newsFragment);
        picturesFragment = new PicturesFragment();
        fragments.add(picturesFragment);
        videoFragment = new VideoFragment();
        fragments.add(videoFragment);
        mNavigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "onOptionsItemSelected: 点击 home");
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int itemId = item.getItemId();
        int count = 0;
        switch (itemId) {
            case R.id.main_News:
                count = 0;
                item.setChecked(true);
                break;
            case R.id.main_Pictures:
                count = 1;
                item.setChecked(true);
                break;
            case R.id.main_Videos:
                count = 2;
                item.setChecked(true);
                break;
            case R.id.mine_Collect:
                Toast.makeText(this, "跳转我的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub_Mode:
                Toast.makeText(this, "准备切换模式", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub_Quit:
                Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
                ActivityCompat.finishAffinity(this);
                break;
        }
        Fragment fragment = fragments.get(count);
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
        if ((paramKeyEvent.getAction() == 0) && (4 == paramInt))
        {
            long currentTime = System.currentTimeMillis();
            if (currentTime - fristTime >= 2000L)
            {
                invalidateOptionsMenu();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                fristTime = currentTime;
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public void returnFlag(boolean flag) {
        if (flag) {
            if (linearLayout.getVisibility() == View.GONE) {
                return;
            } else {
                linearLayout.setVisibility(View.GONE);
                toolbar_title.setVisibility(View.VISIBLE);
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(toolbar_title, "scaleY", 0,1);
                oa1.setDuration(1000);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(linearLayout, "scaleX", 0,1);
                oa2.setDuration(1000);
                set.playTogether(oa1, oa2);
                set.start();
            }
        } else {
            if (toolbar_title.getVisibility()==View.GONE){
                return;
            }else {
                linearLayout.setVisibility(View.VISIBLE);
                toolbar_title.setVisibility(View.GONE);
                ObjectAnimator oa = ObjectAnimator.ofFloat(linearLayout, "translationY", 80,0);
                oa.setDuration(2000);
                oa.setRepeatMode(ValueAnimator.REVERSE);
                oa.start();
            }

        }
    }

    @Override
    public void returnLayout(LinearLayout linearLayout, TextView textView) {
        this.toolbar_title = textView;
        this.linearLayout = linearLayout;
    }
}
