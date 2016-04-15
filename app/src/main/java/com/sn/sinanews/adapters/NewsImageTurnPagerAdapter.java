package com.sn.sinanews.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;
import com.sn.sinanews.entities.ImageTextTO;
import com.sn.sinanews.entities.NewsEntity;

import java.util.List;

/**
 * Created by Ming on 2016/1/11.
 */
public class NewsImageTurnPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private static final String TAG = "NewsImageTurnPager";
    private Context context;
    private List<NewsEntity.DataEntity.ListEntity> list;

    private int currentPosition;

    public NewsImageTurnPagerAdapter(Context context, List<NewsEntity.DataEntity.ListEntity> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 返回多少page
     */
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    /**
     * true: 表示不去创建，使用缓存  false:去重新创建
     * view： 当前滑动的view
     * object：将要进入的新创建的view，由instantiateItem方法创建
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 类似于BaseAdapger的getView方法
     * 用了将数据设置给view
     * 由于它最多就3个界面，不需要viewHolder
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.news_item_imgturn_image, null);
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.simpleImage);
        imageView.setAspectRatio(2f);
        currentPosition = position;
        NewsEntity.DataEntity.ListEntity entity = list.get(position % list.size());
        // 这边用 fresco
        imageView.setImageURI(Uri.parse(entity.getKpic()));
        imageView.setOnClickListener(this);
        container.addView(view);//一定不能少，将view加入到viewPager中
        return view;
    }

    /**
     * 销毁page
     * position： 当前需要消耗第几个page
     * object:当前需要消耗的page
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {

        // TODO: 2016/1/13 这里图片点击跳转
        NewsEntity.DataEntity.ListEntity entity = list.get((currentPosition - 1) % list.size());
        Toast.makeText(context, entity.getTitle(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onClick: " + entity.getTitle());

    }
}
