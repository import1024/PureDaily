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
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.entity.Collection;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.utils.PrefUtils;

import java.util.ArrayList;

/**
 * 收藏夹适配器
 * <p>
 * Created by hanjie on 2016/5/31.
 */
public class CollectionsAdapter extends BaseAdapter<CollectionsAdapter.MyViewHolder> {

    private ArrayList<Collection> mCollections;
    private Context mContext;

    public CollectionsAdapter(Context context, ArrayList<Collection> collections) {
        this.mContext = context;
        mCollections = collections;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Collection collection = mCollections.get(position);
        String imageUrl = collection.getImgUrl();
        String title = collection.getTitle();
        if (!PrefUtils.getBoolean(mContext, PrefConstants.MODE_NO_PIC, false)) {
            Glide.with(mContext)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.title.setText(title);
        // 开启动画
        setAnimation(holder.itemView);
    }

    public void syncData(ArrayList<Collection> collections) {
        this.mCollections = collections;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_latest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mCollections.size();
    }

    /**
     * 删除数据
     */
    public void delete(int position) {
        this.mCollections.remove(position);
        notifyItemRemoved(position);
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
