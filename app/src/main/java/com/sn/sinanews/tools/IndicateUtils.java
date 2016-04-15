package com.sn.sinanews.tools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sn.sinanews.entities.NewsTO;

import java.util.List;

/**
 * Created by Ming on 2016/1/12.
 * <p/>
 * 获取图片轮播的指示 和 tabLayout 的指示,同时更新对应的数据 用 viewPager
 */
public class IndicateUtils {

    /**
     * 设置 导航指示
     *
     * @param commonList   传入的数据
     * @param commonContext 上下文
     * @param commonLinearLayout 布局文件 添加对应的指示个数
     * @param commonDrawableRes 指示器的样式
     */
    public static void initDots(List<?> commonList, Context commonContext,
                                LinearLayout commonLinearLayout, int commonDrawableRes) {
        for (int i = 0; i < commonList.size(); i++) {
            View view = new View(commonContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i != 0) {
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(commonDrawableRes);
            commonLinearLayout.addView(view);
        }
    }

    /**
     * 获取当前的指示 更新对应的数据
     *
     * @param commonViewpager viewPager
     * @param commonList 传入的数据
     * @param commonTextView 更新数据的文本
     * @param commonLinearLayout 布局文件显示那一条被锁定
     */
    public static void updateIntroAndDot(ViewPager commonViewpager, List<?> commonList,
                                         TextView commonTextView, LinearLayout commonLinearLayout) {
        int currentPage = commonViewpager.getCurrentItem();
        commonTextView.setText(((List<NewsTO>) commonList).get(currentPage).getTitle());
        for (int i = 0; i < commonLinearLayout.getChildCount(); i++) {
            commonLinearLayout.getChildAt(i).setEnabled(i == currentPage);
        }
    }
}
