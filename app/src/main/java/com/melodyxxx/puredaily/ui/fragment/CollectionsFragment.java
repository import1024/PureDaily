package com.melodyxxx.puredaily.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.adapter.BaseAdapter;
import com.melodyxxx.puredaily.adapter.CollectionsAdapter;
import com.melodyxxx.puredaily.db.Dao;
import com.melodyxxx.puredaily.entity.Collection;
import com.melodyxxx.puredaily.ui.activity.DailyDetailsActivity;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 收藏Fragment
 * Created by hanjie on 2016/6/6.
 */
@ContentView(R.layout.fragment_collections)
public class CollectionsFragment extends BaseFragment {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.loading)
    private AVLoadingIndicatorView mLoadingView;

    @ViewInject(R.id.empty_view)
    private LinearLayout mEmptyView;

    private Context mContext;

    private CollectionsAdapter mAdapter;

    private ArrayList<Collection> mCollections;

    private Dao mDao;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDao = Dao.getInstance(mContext);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).setToolbarTitle(R.string.fragment_title_collections);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        fetchDataFromDatabase();
        return view;
    }

    private void initRecyclerView() {
        mAdapter = new CollectionsAdapter(mContext, mCollections);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                DailyDetailsActivity.startDailyDetailsActivity(getActivity(), mCollections.get(position).getId(), ((CollectionsAdapter.MyViewHolder) holder).image);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int deletePosition = viewHolder.getLayoutPosition();
                mDao.removeFromCollections(mCollections.get(deletePosition).getId());
                mAdapter.delete(deletePosition);
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void fetchDataFromDatabase() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                startLoadingAnim();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                mCollections = mDao.getAllCollections();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                stopLoadingAnim();
                if (mCollections.size() == 0) {
                    displayEmptyView();
                } else if (mAdapter == null) {
                    initRecyclerView();
                } else {
                    mAdapter.syncData(mCollections);
                }
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private void startLoadingAnim() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

    private void displayEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

}
