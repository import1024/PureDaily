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
import com.melodyxxx.puredaily.entity.Comment;
import com.melodyxxx.puredaily.utils.CommonUtils;

import java.util.ArrayList;

/**
 * 评论适配器
 * <p>
 * Created by hanjie on 2016/6/2.
 */
public class CommentAdapter extends BaseAdapter<CommentAdapter.MyViewHolder> {

    private ArrayList<Comment> mComments;
    private Context mContext;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.mContext = context;
        mComments = comments;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Comment comment = mComments.get(position);
        Glide.with(mContext)
                .load(comment.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.avatar);
        holder.author.setText(comment.getAuthor());
        holder.time.setText(CommonUtils.formatCommentTime(Long.parseLong(comment.getTime() + "000")));
        holder.content.setText(comment.getContent());
        holder.likeCount.setText(comment.getLikes());
        // 开启动画
        setAnimation(holder.itemView);
    }

    public void syncData(ArrayList<Comment> comments) {
        this.mComments = comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView author;
        TextView time;
        TextView content;
        TextView likeCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            likeCount = (TextView) itemView.findViewById(R.id.tv_like_count);
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
