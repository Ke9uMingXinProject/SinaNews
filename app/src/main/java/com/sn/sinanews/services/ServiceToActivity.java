package com.sn.sinanews.services;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ming on 2016/1/11.
 * 回调数据给 mainActivity
 */
public interface ServiceToActivity {

     void returnLayout(LinearLayout linearLayout,TextView textView);
}
