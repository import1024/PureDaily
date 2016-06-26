package com.melodyxxx.puredaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 最新消息适配器
 * <p/>
 * Created by hanjie on 2016/5/31.
 */
public class LatestAdapter extends BaseAdapter<LatestAdapter.MyViewHolder> {

    private ArrayList<Latest> mLatests;
    private Context mContext;

    public LatestAdapter(Context context, ArrayList<Latest> latests) {
        this.mContext = context;
        mLatests = latests;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Latest latest = mLatests.get(position);
        String imageUrl = latest.getImageUrl();
        String title = latest.getTitle();
        if (!PrefUtils.getBoolean(mContext, PrefConstants.MODE_NO_PIC, false)) {
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(holder.image);
            } else {
                holder.image.setImageDrawable(null);
            }
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.title.setText(title);
        // 开启动画
        setAnimation(holder.itemView);
    }

    public void syncData(ArrayList<Latest> latests) {
        this.mLatests = latests;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_latest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mLatests.size();
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
