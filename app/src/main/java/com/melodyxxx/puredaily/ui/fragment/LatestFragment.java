package com.melodyxxx.puredaily.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.adapter.BaseAdapter;
import com.melodyxxx.puredaily.adapter.LatestAdapter;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.task.FetchLatestTask;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;
import com.melodyxxx.puredaily.ui.activity.LatestDetailsActivity;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.wang.avi.AVLoadingIndicatorView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initSwipe();
        startLoadingAnim();
        fetchLatestData(null);
        return view;
    }

    private void initSwipe() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLatestData(null);
            }
        });
        mSwipe.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 获取消息
     *
     * @param date 指定获取的该日期的日报(date为null代表默认获取今日最新消息)
     */
    private void fetchLatestData(String date) {
        FetchLatestTask.fetch(date, new FetchLatestTask.FetchLatestCallback() {
            @Override
            public void onSuccess(ArrayList<Latest> latests, String resultDate) {
                onFetchSuccess(latests, resultDate);
            }

            @Override
            public void onError(String errorMsg) {
                SnackBarUtils.makeShort(mLoadingView, errorMsg).show();
                onFetchFailed();
            }
        });
    }

    private void onFetchSuccess(ArrayList<Latest> latests, String resultDate) {
        ((HomeActivity) getActivity()).setToolbarTitle(CommonUtils.formatResultDate(resultDate));
        this.mStories = latests;
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
                LatestDetailsActivity.startLatestDetailsActivity(getActivity(), mStories.get(position), ((LatestAdapter.MyViewHolder) holder).image);
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
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history: {
                displayCalendarChooser();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayCalendarChooser() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        fetchLatestData(CommonUtils.formatDateForHistory(year, monthOfYear, dayOfMonth + 1));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar min = Calendar.getInstance();
        min.set(2013, 4, 19);
        dpd.setMinDate(min);
        Calendar max = Calendar.getInstance();
        dpd.setMaxDate(max);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }
}
