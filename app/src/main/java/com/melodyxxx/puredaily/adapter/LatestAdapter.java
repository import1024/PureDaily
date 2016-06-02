package com.melodyxxx.puredaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.Latest;

import java.util.ArrayList;

/**
 * 最新消息Adapter
 * <p>
 * Created by hanjie on 2016/5/31.
 */
public class LatestAdapter extends BaseAdapter<LatestAdapter.MyViewHolder> {

    private ArrayList<Latest> mStories;
    private Context mContext;

    public LatestAdapter(Context context, ArrayList<Latest> stories) {
        this.mContext = context;
        mStories = stories;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Latest latest = mStories.get(position);
        String imageUrl = latest.getImageUrl();
        String title = latest.getTitle();
        Glide.with(mContext)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.image);
        holder.title.setText(title);
        // 开启动画
        setAnimation(holder.itemView);
    }

    public void syncData(ArrayList<Latest> stories) {
        this.mStories = stories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_latest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.latest_image);
            title = (TextView) itemView.findViewById(R.id.latest_title);
        }

    }

    private void setAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        view.startAnimation(anim);
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        holder.itemView.clearAnimation();
    }
}