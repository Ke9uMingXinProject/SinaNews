package com.sn.sinanews.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sn.sinanews.R;
import com.sn.sinanews.activities.PicItemSelectActivity;
import com.sn.sinanews.entities.PicItems;
import com.sn.sinanews.services.ServiceSendTouchAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 卜令壮
 * on 2016/1/11
 * E-mail q137617549@qq.com
 */
public class PicItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = PicItemAdapter.class.getSimpleName();
    private Context context;
    private List<PicItems.DataEntity.ListEntity> list;
    private String adapterType;
    private ServiceSendTouchAction mServiceSendTouchAction;

    public PicItemAdapter(Context context,String adapterType,ServiceSendTouchAction mServiceSendTouchAction) {
        this.context = context;
        this.adapterType = adapterType;
        this.list = new ArrayList<>();
        this.mServiceSendTouchAction = mServiceSendTouchAction;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate;
        switch (viewType) {
            case 0:
                //单独一张大图
                inflate = LayoutInflater.from(context).inflate(R.layout.adapter_pic_item_one, parent, false);
                return new PicItemViewHolder4One(inflate);
            case 1:
                //两张图
                inflate = LayoutInflater.from(context).inflate(R.layout.adapter_pic_item, parent, false);
                return new PicItemViewHolder(inflate);
            case 2:
                //三张图，上面
                inflate = LayoutInflater.from(context).inflate(R.layout.adapter_pic_item_three_top, parent, false);
                return new PicItemViewHolder4ThreeTop(inflate);
            case 3:
                //三张图，左边
                inflate = LayoutInflater.from(context).inflate(R.layout.adapter_pic_item_three_left, parent, false);
                return new PicItemViewHolder4ThreeLeft(inflate);
            case 4:
                //四张图
                inflate = LayoutInflater.from(context).inflate(R.layout.adapter_pic_item_four, parent, false);
                return new PicItemViewHolder4Four(inflate);
        }
//        inflate = LayoutInflater.from(context).inflate(R.layout.adapter_pic_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PicItems.DataEntity.ListEntity entity = list.get(position);
        List<PicItems.DataEntity.ListEntity.PicsEntity.ListEntityEntity> picList = entity.getPics().getList();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PicItemSelectActivity.class);
                Bundle args = new Bundle();
                args.putString("id",entity.getId());
                args.putString("postt","hdpic_"+adapterType+"_feed_1");
                args.putString("comment",entity.getComment());
                intent.putExtras(args);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        int currentY = (int) event.getY();
                        Log.e(TAG, "currentY="+currentY);
                        mServiceSendTouchAction.sendAction(currentY);
                        break;
                }
                return false;
            }
        });
        switch (holder.getItemViewType()) {
            case 0:
                PicItemViewHolder4One holder4One = (PicItemViewHolder4One) holder;
                holder4One.title.setText(entity.getTitle());
                holder4One.intro.setText(entity.getIntro());
                holder4One.comments.setText(entity.getComment() + "评论");
                holder4One.pics_total.setText(entity.getPics().getTotal() + "图");
                holder4One.img1.setAspectRatio(1.5f);
                holder4One.img1.setImageURI(Uri.parse(picList.get(0).getKpic()));
                break;
            case 1:
                PicItemViewHolder viewHolder = (PicItemViewHolder) holder;
                viewHolder.title.setText(entity.getTitle());
                viewHolder.intro.setText(entity.getIntro());
                viewHolder.comments.setText(entity.getComment() + "评论");
                viewHolder.pics_total.setText(entity.getPics().getTotal() + "图");
                viewHolder.img1.setAspectRatio(0.75f);
                viewHolder.img2.setAspectRatio(0.75f);
                viewHolder.img1.setImageURI(Uri.parse(picList.get(0).getKpic()));
                viewHolder.img2.setImageURI(Uri.parse(picList.get(1).getKpic()));
                break;
            case 2:
                PicItemViewHolder4ThreeTop holder4ThreeTop = (PicItemViewHolder4ThreeTop) holder;
                holder4ThreeTop.title.setText(entity.getTitle());
                holder4ThreeTop.intro.setText(entity.getIntro());
                holder4ThreeTop.comments.setText(entity.getComment() + "评论");
                holder4ThreeTop.pics_total.setText(entity.getPics().getTotal() + "图");
                holder4ThreeTop.img3.setAspectRatio(1.5f);
                holder4ThreeTop.img3.setImageURI(Uri.parse(picList.get(0).getKpic()));
                holder4ThreeTop.img1.setAspectRatio(1.5f);
                holder4ThreeTop.img2.setAspectRatio(1.5f);
                holder4ThreeTop.img1.setImageURI(Uri.parse(picList.get(1).getKpic()));
                holder4ThreeTop.img2.setImageURI(Uri.parse(picList.get(2).getKpic()));
                break;
            case 3:
                PicItemViewHolder4ThreeLeft holder4ThreeLeft = (PicItemViewHolder4ThreeLeft) holder;
                holder4ThreeLeft.title.setText(entity.getTitle());
                holder4ThreeLeft.intro.setText(entity.getIntro());
                holder4ThreeLeft.comments.setText(entity.getComment() + "评论");
                holder4ThreeLeft.pics_total.setText(entity.getPics().getTotal() + "图");
                holder4ThreeLeft.img3.setAspectRatio(0.75f);
                holder4ThreeLeft.img3.setImageURI(Uri.parse(picList.get(0).getKpic()));
                holder4ThreeLeft.img1.setAspectRatio(1.5f);
                holder4ThreeLeft.img2.setAspectRatio(1.5f);
                holder4ThreeLeft.img1.setImageURI(Uri.parse(picList.get(1).getKpic()));
                holder4ThreeLeft.img2.setImageURI(Uri.parse(picList.get(2).getKpic()));
                break;
            case 4:
                PicItemViewHolder4Four holder4Four = (PicItemViewHolder4Four) holder;
                holder4Four.title.setText(entity.getTitle());
                holder4Four.intro.setText(entity.getIntro());
                holder4Four.comments.setText(entity.getComment() + "评论");
                holder4Four.pics_total.setText(entity.getPics().getTotal() + "图");
                holder4Four.img1.setAspectRatio(1.5f);
                holder4Four.img2.setAspectRatio(1.5f);
                holder4Four.img1.setImageURI(Uri.parse(picList.get(0).getKpic()));
                holder4Four.img2.setImageURI(Uri.parse(picList.get(1).getKpic()));
                holder4Four.img3.setAspectRatio(1.5f);
                holder4Four.img4.setAspectRatio(1.5f);
                holder4Four.img3.setImageURI(Uri.parse(picList.get(2).getKpic()));
                holder4Four.img4.setImageURI(Uri.parse(picList.get(3).getKpic()));
                break;
        }
    }

    //设置Item的类型
    @Override
    public int getItemViewType(int position) {
        String picTemplate = list.get(position).getPics().getPicTemplate();
        if (picTemplate != null) {
            return Integer.parseInt(picTemplate);
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addAll(Collection<? extends PicItems.DataEntity.ListEntity> collection, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
        }
        list.addAll(collection);
        notifyDataSetChanged();
//        notifyItemRangeInserted(0,collection.size());
    }

    //不同布局的ViewHolder
    //两张图的
    public static class PicItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView title;
        private final TextView comments;
        private final TextView intro;
        private final SimpleDraweeView img1;
        private final SimpleDraweeView img2;
        private final TextView pics_total;

        public PicItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_pic_item_title);
            comments = (TextView) itemView.findViewById(R.id.adapter_pic_item_comment);
            intro = (TextView) itemView.findViewById(R.id.adapter_pic_item_intro);
            pics_total = (TextView) itemView.findViewById(R.id.adapter_pic_item_pics_total);
            img1 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img1);
            img2 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img2);
        }
    }

    //一张图的
    public static class PicItemViewHolder4One extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView comments;
        private final TextView intro;
        private final SimpleDraweeView img1;
        private final TextView pics_total;

        public PicItemViewHolder4One(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_pic_item_title);
            comments = (TextView) itemView.findViewById(R.id.adapter_pic_item_comment);
            intro = (TextView) itemView.findViewById(R.id.adapter_pic_item_intro);
            pics_total = (TextView) itemView.findViewById(R.id.adapter_pic_item_pics_total);
            img1 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img1);
        }
    }

    //三张图，上
    public static class PicItemViewHolder4ThreeTop extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView comments;
        private final TextView intro;
        private final SimpleDraweeView img1;
        private final SimpleDraweeView img2;
        private final SimpleDraweeView img3;
        private final TextView pics_total;

        public PicItemViewHolder4ThreeTop(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_pic_item_title);
            comments = (TextView) itemView.findViewById(R.id.adapter_pic_item_comment);
            intro = (TextView) itemView.findViewById(R.id.adapter_pic_item_intro);
            pics_total = (TextView) itemView.findViewById(R.id.adapter_pic_item_pics_total);
            img1 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img1);
            img2 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img2);
            //上面的大图
            img3 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img3);
        }
    }

    //三张图，左
    public static class PicItemViewHolder4ThreeLeft extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView comments;
        private final TextView intro;
        private final SimpleDraweeView img1;
        private final SimpleDraweeView img2;
        private final SimpleDraweeView img3;
        private final TextView pics_total;

        public PicItemViewHolder4ThreeLeft(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_pic_item_title);
            comments = (TextView) itemView.findViewById(R.id.adapter_pic_item_comment);
            intro = (TextView) itemView.findViewById(R.id.adapter_pic_item_intro);
            pics_total = (TextView) itemView.findViewById(R.id.adapter_pic_item_pics_total);
            img1 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img1);
            img2 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img2);
            //左边的大图
            img3 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img3);
        }
    }

    //四张图
    public static class PicItemViewHolder4Four extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView comments;
        private final TextView intro;
        private final SimpleDraweeView img1;
        private final SimpleDraweeView img2;
        private final SimpleDraweeView img3;
        private final SimpleDraweeView img4;
        private final TextView pics_total;

        public PicItemViewHolder4Four(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_pic_item_title);
            comments = (TextView) itemView.findViewById(R.id.adapter_pic_item_comment);
            intro = (TextView) itemView.findViewById(R.id.adapter_pic_item_intro);
            pics_total = (TextView) itemView.findViewById(R.id.adapter_pic_item_pics_total);
            img1 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img1);
            img2 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img2);
            img3 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img3);
            img4 = (SimpleDraweeView) itemView.findViewById(R.id.adapter_pic_item_img4);
        }
    }
}
