package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.db.Dao;
import com.melodyxxx.puredaily.entity.Collection;
import com.melodyxxx.puredaily.entity.LatestDetails;
import com.melodyxxx.puredaily.task.FetchLatestDetailsTask;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by hanjie on 2016/6/1.
 */
@ContentView(R.layout.activity_latest_details)
public class DailyDetailsActivity extends BaseActivity implements NestedScrollView.OnScrollChangeListener, AppBarLayout.OnOffsetChangedListener {

    @ViewInject(R.id.iv_image)
    private ImageView mImage;

    @ViewInject(R.id.web_view)
    private WebView mWebView;

    @ViewInject(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @ViewInject(R.id.app_bar)
    private AppBarLayout mAppBarLayout;

    @ViewInject(R.id.loading)
    private AVLoadingIndicatorView mLoadingView;

    @ViewInject(R.id.tv_image_source)
    private TextView mImageSource;

    @ViewInject(R.id.fab)
    private FloatingActionButton mFab;

    @ViewInject(R.id.fab_go_to_top)
    private FloatingActionButton mGoToTopFab;

    @ViewInject(R.id.nested_scrollview)
    private NestedScrollView mNestedScrollView;

    private String mId;

    private LatestDetails mLatestDetails;

    private Dao mDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mDao = Dao.getInstance(this);
        mCollapsingToolbarLayout.setTitle(" ");
        mId = getIntent().getStringExtra("id");
        initListener();
        initWebView();
        startLoadingAnim();
        fetchLatestDetailsData();
        hideGoToTopFab();
    }

    private void initListener() {
        mAppBarLayout.addOnOffsetChangedListener(this);
        mNestedScrollView.setOnScrollChangeListener(this);
    }

    private void hideGoToTopFab() {
        mNestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                mGoToTopFab.hide();
            }
        });
    }

    private void fetchLatestDetailsData() {
        FetchLatestDetailsTask.fetch(mId, new FetchLatestDetailsTask.FetchLatestCallback() {
            @Override
            public void onSuccess(LatestDetails latestDetails) {
                onFetchSuccess(latestDetails);
                stopLoadingAnim();
            }

            @Override
            public void onError(String errorMsg) {
                SnackBarUtils.makeShort(DailyDetailsActivity.this, mLoadingView, errorMsg).show();
                stopLoadingAnim();
            }
        });
    }

    @Event(value = R.id.fab, type = View.OnClickListener.class)
    private void onFabClick(View view) {
        CommentActivity.startCommentActivity(this, mId, mFab);
    }

    @Event(value = R.id.fab_go_to_top, type = View.OnClickListener.class)
    private void onFabToTopClick(View view) {
        mNestedScrollView.smoothScrollTo(0, 0);
    }

    private void onFetchSuccess(LatestDetails latestDetails) {
        this.mLatestDetails = latestDetails;
        if (!isFinishing()) {
            mImageSource.setText(mLatestDetails.getImageSource());
            mCollapsingToolbarLayout.setTitle(mLatestDetails.getTitle());
            mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            if (!PrefUtils.getBoolean(this, PrefConstants.MODE_NO_PIC, false)) {
                Glide.with(DailyDetailsActivity.this)
                        .load(mLatestDetails.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontTransform()
                        .dontAnimate()
                        .into(mImage);
            }
            if (TextUtils.isEmpty(mLatestDetails.getBody())) {
                mWebView.loadUrl(mLatestDetails.getShareUrl());
                return;
            }
            String body = mLatestDetails.getBody().replace("<div class=\"headline\">", "").replace("<div class=\"img-place-holder\">", "");
            String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + "<br/>" + body;
            mWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
        }
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        // 是否加载图片
        mWebView.getSettings().setBlockNetworkImage(PrefUtils.getBoolean(this, PrefConstants.MODE_NO_PIC, false));
    }

    public static void startDailyDetailsActivity(Activity activity, String id, View view) {
        Intent intent = new Intent(activity, DailyDetailsActivity.class);
        intent.putExtra("id", id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !PrefUtils.getBoolean(activity, PrefConstants.MODE_NO_PIC, false)) {
            // Android 5.0+ && 没有开启无图模式 开启共享元素动画
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, view, activity.getString(R.string.transition_latest_with_latest_details)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_daily_details, menu);

        // 初始化收藏菜单状态
        boolean isCollected = mDao.isExistInCollections(mId);
        MenuItem collectItem = menu.findItem(R.id.action_collect);
        collectItem.setIcon(isCollected ? R.drawable.ic_collected : R.drawable.ic_collect);
        collectItem.setChecked(isCollected);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: {
                shareStory();
                break;
            }
            case R.id.action_collect: {
                if (mLatestDetails == null) {
                    break;
                }
                // 构建Collection对象
                Collection collection = new Collection();
                collection.setId(mLatestDetails.getId());
                collection.setTitle(mLatestDetails.getTitle());
                collection.setImgUrl(mLatestDetails.getSmallImageUrl());
                collection.setTime(System.currentTimeMillis());
                boolean isCollected = item.isChecked();
                if (isCollected) {
                    mDao.removeFromCollections(mLatestDetails.getId());
                    item.setIcon(R.drawable.ic_collect);
                    SnackBarUtils.makeShort(this, mWebView, getString(R.string.tip_cancel_collect)).show();
                } else {
                    mDao.insertToCollections(collection);
                    item.setIcon(R.drawable.ic_collected);
                    SnackBarUtils.makeShort(this, mWebView, getString(R.string.tip_collected)).show();
                }
                item.setChecked(!isCollected);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareStory() {
        if (mLatestDetails == null) {
            return;
        }
        // 构建分享内容
        StringBuilder sb = new StringBuilder();
        sb.append("分享来自「Pure Daily」：");
        sb.append(mLatestDetails.getShareUrl());
        sb.append(" （");
        sb.append(mLatestDetails.getTitle());
        sb.append("）");

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.dialog_title_share)));
    }

    private void startLoadingAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if ((oldScrollY - scrollY) > ViewConfiguration.get(this).getScaledTouchSlop()) {
            mGoToTopFab.show();
        } else if ((scrollY - oldScrollY > ViewConfiguration.get(this).getScaledTouchSlop())) {
            mGoToTopFab.hide();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset != 0) {
            // 展开状态
            mGoToTopFab.hide();
        }
    }

}
