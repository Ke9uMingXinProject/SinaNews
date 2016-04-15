package com.sn.sinanews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.fragments.PicItemsFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 卜令壮
 * on 2016/1/11
 * E-mail q137617549@qq.com
 */
public class PicItemPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "Adapter";
    private List<NewsTO> mNewsTOList;

    private List<PicItemsFragment> fragments;

    public PicItemPagerAdapter(FragmentManager fm,List<NewsTO> mNewsTOList) {
        super(fm);
        this.mNewsTOList = mNewsTOList;
        fragments = new LinkedList<>();
    }

    @Override
    public Fragment getItem(int position) {
//        String tabTitleName = titles[position];
//
//        int size = fragments.size();
//
//        PicItemsFragment ret;
//
//        if(position >= size) {
//            ret = PicItemsFragment.newInstance(tabTitleName);
//            fragments.add(ret);
//        }else{
//            ret = fragments.get(position);
//        }
        if (fragments.size() < mNewsTOList.size()) {
//            for (String title : titles) {
//                fragments.add(PicItemsFragment.newInstance(title));
//            }
            for (int i = 0; i < mNewsTOList.size(); i++) {
                fragments.add(PicItemsFragment.newInstance(mNewsTOList.get(i)));
            }
        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mNewsTOList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNewsTOList.get(position).getTitle();
    }
}
