package com.melodyxxx.puredaily.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.adapter.BaseAdapter;
import com.melodyxxx.puredaily.adapter.LatestAdapter;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.task.FetchLatestTask;
import com.melodyxxx.puredaily.ui.activity.LatestDetailsActivity;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 最新消息Fragment
 * Created by hanjie on 2016/5/31.
 */
@ContentView(R.layout.fragment_latest)
public class LatestFragment extends BaseFragment {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.loading)
    private AVLoadingIndicatorView mLoadingView;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout mSwipe;

    @ViewInject(R.id.data_area)
    private FrameLayout mDataArea;

    private Context mContext;

    private LatestAdapter mAdapter;

    private ArrayList<Latest> mStories;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initSwipe();
        startLoadingAnim();
        fetchLatestData();
        return view;
    }

    private void initSwipe() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLatestData();
            }
        });
        mSwipe.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private void fetchLatestData() {
        FetchLatestTask.fetch(new FetchLatestTask.FetchLatestCallback() {
            @Override
            public void onSuccess(ArrayList<Latest> latests) {
                onFetchSuccess(latests);
            }

            @Override
            public void onError(String errorMsg) {
                onFetchFailed();
            }
        });
    }

    private void onFetchSuccess(ArrayList<Latest> stories) {
        this.mStories = stories;
        if (mAdapter == null) {
            initRecyclerView();
        } else {
            mAdapter.syncData(mStories);
            mAdapter.notifyDataSetChanged();
        }
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
        stopLoadingAnim();
    }

    private void initRecyclerView() {
        mAdapter = new LatestAdapter(mContext, mStories);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                LatestDetailsActivity.startStoryActivity(getActivity(), mStories.get(position), ((LatestAdapter.MyViewHolder) holder).image);
            }
        });
    }

    private void onFetchFailed() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
        stopLoadingAnim();
    }

    private void startLoadingAnim() {
        mDataArea.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mDataArea.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

}
