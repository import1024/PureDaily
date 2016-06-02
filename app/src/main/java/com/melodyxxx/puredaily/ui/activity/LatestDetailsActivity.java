package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.entity.LatestDetails;
import com.melodyxxx.puredaily.task.FetchLatestDetailsTask;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by hanjie on 2016/6/1.
 */
@ContentView(R.layout.activity_latest_details)
public class LatestDetailsActivity extends BaseActivity {

    @ViewInject(R.id.iv_image)
    private ImageView mImage;

    @ViewInject(R.id.web_view)
    private WebView mWebView;

    @ViewInject(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @ViewInject(R.id.loading)
    private AVLoadingIndicatorView mLoadingView;

    @ViewInject(R.id.tv_image_source)
    private TextView mImageSource;

    @ViewInject(R.id.fab)
    private FloatingActionButton mFab;

    private Latest mLatest;

    private LatestDetails mLatestDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mCollapsingToolbarLayout.setTitle(" ");
        mLatest = (Latest) getIntent().getSerializableExtra("latest");
        initWebView();
        startLoadingAnim();
        fetchLatestDetailsData();
    }

    private void fetchLatestDetailsData() {
        FetchLatestDetailsTask.fetch(mLatest.getId(), new FetchLatestDetailsTask.FetchLatestCallback() {
            @Override
            public void onSuccess(LatestDetails latestDetails) {
                onFetchSuccess(latestDetails);
                stopLoadingAnim();
            }

            @Override
            public void onError(String errorMsg) {
                SnackBarUtils.makeShort(mLoadingView, errorMsg).show();
                stopLoadingAnim();
            }
        });
    }

    @Event(value = R.id.fab, type = View.OnClickListener.class)
    private void onFabClick(View view) {
        CommentActivity.startCommentActivity(this, mLatest, mFab);
    }

    private void onFetchSuccess(LatestDetails latestDetails) {
        this.mLatestDetails = latestDetails;
        if (!isFinishing()) {
            mImageSource.setText(mLatestDetails.getImageSource());
            mCollapsingToolbarLayout.setTitle(mLatestDetails.getTitle());
            mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            Glide.with(LatestDetailsActivity.this)
                    .load(mLatestDetails.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontTransform()
                    .dontAnimate()
                    .into(mImage);
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
        // 是否加载图片，后面无图模式会用到
//        mWebView.getSettings().setBlockNetworkImage(true);
    }

    public static void startLatestDetailsActivity(Activity activity, Latest latest, View view) {
        Intent intent = new Intent(activity, LatestDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", latest);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, view, activity.getString(R.string.transition_latest_with_latest_details)).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_latest, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: {
                shareStory();
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

}
