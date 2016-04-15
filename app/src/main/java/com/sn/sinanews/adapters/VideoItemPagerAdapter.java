package com.sn.sinanews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.sn.sinanews.entities.NewsTO;
import com.sn.sinanews.fragments.VideoItemsFragment;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 卜令壮
 * on 2016/1/13
 * E-mail q137617549@qq.com
 */
public class VideoItemPagerAdapter extends FragmentPagerAdapter{
    private List<VideoItemsFragment> mList;
    private List<NewsTO> mNewsTOList;
    public VideoItemPagerAdapter(FragmentManager fm,List<NewsTO> mNewsTOList) {
        super(fm);
        this.mNewsTOList = mNewsTOList;
        mList = new LinkedList<>();
    }

    @Override
    public Fragment getItem(int position) {
//        if (mList.size() < titles.length) {
//            for (String title : titles) {
//                mList.add(VideoItemsFragment.newInstance(title));
//            }
//        }
        if (mList.size() < mNewsTOList.size()){
            for (int i = 0; i < mNewsTOList.size(); i++) {
                Log.e("hh", mNewsTOList.get(i).getTitle());
                mList.add(VideoItemsFragment.newInstance(mNewsTOList.get(i).getTitle()));
            }
        }
        return mList.get(position);
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
