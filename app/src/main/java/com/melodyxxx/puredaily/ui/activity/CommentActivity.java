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
import android.widget.LinearLayout;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.adapter.CommentAdapter;
import com.melodyxxx.puredaily.entity.Comment;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.task.FetchCommentTask;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.melodyxxx.puredaily.utils.StatusBarUtils;
import com.wang.avi.AVLoadingIndicatorView;

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

    @ViewInject(R.id.loading)
    private AVLoadingIndicatorView mLoadingView;

    @ViewInject(R.id.no_data_area)
    private LinearLayout mNoDataArea;

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
        Latest latest = (Latest) getIntent().getSerializableExtra("latest");
        startLoadingAnim();
        featchCommentData(latest);
    }

    private void featchCommentData(final Latest latest) {
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
                        stopLoadingAnim();
                        mComments.addAll(comments);
                        if (mComments.size() == 100){
                            // 无长评&短评
                            mNoDataArea.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (comments.size() != 0) {
                            // 有短评
                            mShortCommentStatPos = mLongCommentSize;
                        }
                        onFetchSuccess();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        stopLoadingAnim();
                        SnackBarUtils.makeShort(mLoadingView, errorMsg).show();
                    }
                });
            }

            @Override
            public void onError(String errorMsg) {
                SnackBarUtils.makeShort(mLoadingView, errorMsg).show();
                stopLoadingAnim();
            }
        });
    }


    private void onFetchSuccess() {
        initRecyclerView();
    }

    private void initRecyclerView() {
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

    private void startLoadingAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
