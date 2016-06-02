package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.adapter.CommentAdapter;
import com.melodyxxx.puredaily.entity.Comment;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.task.FetchCommentTask;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.StatusBarUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 评论页
 * <p>
 * Created by hanjie on 2016/6/2.
 */
@ContentView(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private CommentAdapter mAdapter;

    private ArrayList<Comment> mComments = new ArrayList<>();

    // 长评在评论集合中的起始位置
    private int mLongCommentStartPos = -1;

    // 短评在评论集合中的起始位置
    private int mShortCommentStatPos = -1;

    private int mLongCommentSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        setToolbarTitle(R.string.activity_title_comment);
        final Latest latest = (Latest) getIntent().getSerializableExtra("latest");
        FetchCommentTask.fetch(latest.getId(), FetchCommentTask.TYPE_LONG_COMMENT, new FetchCommentTask.FetchLatestCallback() {
            @Override
            public void onSuccess(ArrayList<Comment> comments) {
                mComments.addAll(comments);
                if (comments.size() != 0) {
                    // 有长评
                    mLongCommentStartPos = 0;
                    mLongCommentSize = comments.size();
                }
                FetchCommentTask.fetch(latest.getId(), FetchCommentTask.TYPE_SHORT_COMMENT, new FetchCommentTask.FetchLatestCallback() {
                    @Override
                    public void onSuccess(ArrayList<Comment> comments) {
                        mComments.addAll(comments);
                        if (comments.size() != 0) {
                            // 有短评
                            mShortCommentStatPos = mLongCommentSize;
                        }
                        onFetchSuccess();
                    }

                    @Override
                    public void onError(String errorMsg) {

                    }
                });
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    private void onFetchSuccess() {
        Log.d("bingo", mComments.size() + "");
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d("bingo", "mLongCommentSize:" + mLongCommentSize + " mLongCommentStartPos:" + mLongCommentStartPos + " mShortCommentStatPos:" + mShortCommentStatPos);
        mAdapter = new CommentAdapter(this, mComments, mLongCommentStartPos, mShortCommentStatPos);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void startCommentActivity(Activity activity, Latest latest, View view) {
        Intent intent = new Intent(activity, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", latest);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, view, activity.getString(R.string.transition_latest_details_with_comments)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
