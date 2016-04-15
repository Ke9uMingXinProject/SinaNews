package com.sn.sinanews.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.entities.PicItemReveal;

import java.util.Collection;
import java.util.List;

/**
 * Created by 卜令壮
 * on 2016/1/17
 * E-mail q137617549@qq.com
 */
public class PicSelectAdapter extends PagerAdapter {
    private static final String TAG = PicSelectAdapter.class.getSimpleName();
    private List<PicItemReveal.DataEntity.PicsModuleEntity.DataEntityEntity> mPicData;

    public PicSelectAdapter(List<PicItemReveal.DataEntity.PicsModuleEntity.DataEntityEntity> mPicData) {
        this.mPicData = mPicData;
    }

    @Override
    public int getCount() {
        return mPicData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        simpleDraweeView.setImageURI(Uri.parse(mPicData.get(position).getKpic()));
        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        container.addView(simpleDraweeView);
        return simpleDraweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }

    public void addAll(Collection<PicItemReveal.DataEntity.PicsModuleEntity.DataEntityEntity> collection){
        mPicData.addAll(collection);
        notifyDataSetChanged();
    }
}
