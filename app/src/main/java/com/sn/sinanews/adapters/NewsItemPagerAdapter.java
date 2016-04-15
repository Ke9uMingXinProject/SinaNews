package com.sn.sinanews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.fragments.NewsItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ming on 2016/1/10.
 */
public class NewsItemPagerAdapter extends FragmentPagerAdapter {

    private List<NewsTO> newsTOs;
    private List<NewsItemFragment> mNewsItemFragments;
    public NewsItemPagerAdapter(FragmentManager fm, List<NewsTO> newsTOs) {
        super(fm);
        this.newsTOs = newsTOs;
        mNewsItemFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {

        if (mNewsItemFragments.size()<newsTOs.size()) {
            for (int i = 0; i < newsTOs.size(); i++) {
                mNewsItemFragments.add( NewsItemFragment.newInstance(newsTOs.get(i).getTitleParam()));
            }
        }
        return mNewsItemFragments.get(position);
    }

    @Override
    public int getCount() {
        return newsTOs.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return newsTOs.get(position).getTitle();
    }
}
