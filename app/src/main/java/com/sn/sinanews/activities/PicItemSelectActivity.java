package com.sn.sinanews.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sn.sinanews.R;
import com.sn.sinanews.adapters.PicSelectAdapter;
import com.sn.sinanews.entities.PicItemReveal;
import com.sn.sinanews.services.BaseService;
import com.sn.sinanews.tools.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PicItemSelectActivity extends AppCompatActivity implements Callback<PicItemReveal>, ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = PicItemSelectActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private PicSelectAdapter mPicSelectAdapter;
    private ImageButton mBackBtn;
    private TextView mTitle;
    private TextView mAlt;
    private TextView mCurrentPic;
    private TextView mPicCount;
    private List<PicItemReveal.DataEntity.PicsModuleEntity.DataEntityEntity> mPicData;
    private Handler hanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCurrentPic.setText(""+(msg.arg1+1));
            mAlt.setText(mPicData.get(msg.arg1).getAlt());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_item_select);
        //从上一个页面传过来的数据
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String postt = intent.getStringExtra("postt");
        String commentStr = intent.getStringExtra("comment");
        BaseService service = HttpUtils.getService();
        Call<PicItemReveal> call = service.getPicRevealList(id, "6049295012L", postt);
        call.enqueue(this);
        //TODO:评论数从上一页传过来，图片总数可以获取list的数量
        //findById
        mViewPager = (ViewPager) findViewById(R.id.pic_item_select_viewPager);
        mBackBtn = (ImageButton) findViewById(R.id.pic_item_select_back);
        mTitle = (TextView) findViewById(R.id.pic_item_select_title);
        mAlt = (TextView) findViewById(R.id.pic_item_select_alt);
        mCurrentPic = (TextView) findViewById(R.id.pic_item_select_currentPic);
        mPicCount = (TextView) findViewById(R.id.pic_item_select_picCount);
        TextView comment = (TextView) findViewById(R.id.pic_item_select_comment);

        //设置
        mBackBtn.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        comment.setText(commentStr);
        mPicData = new ArrayList<>();
        mPicSelectAdapter = new PicSelectAdapter(mPicData);
        mViewPager.setAdapter(mPicSelectAdapter);
    }

    @Override
    public void onResponse(Response<PicItemReveal> response, Retrofit retrofit) {
        PicItemReveal.DataEntity data = response.body().getData();
        mPicData = data.getPics_module().get(0).getData();
        mPicSelectAdapter.addAll(mPicData);
        mTitle.setText(data.getTitle());
        mAlt.setText(mPicData.get(0).getAlt());
        mPicCount.setText("/" + mPicData.size());
    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        Toast.makeText(this,"网络错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Message message = new Message();
        message.arg1 = position;
        hanlder.sendMessage(message);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
