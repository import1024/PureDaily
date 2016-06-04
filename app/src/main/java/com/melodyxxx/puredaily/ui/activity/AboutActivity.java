package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.LatestVersion;
import com.melodyxxx.puredaily.task.FetchLatestVersionInfoTask;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.DialogUtils;
import com.melodyxxx.puredaily.utils.SnackBarUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by hanjie on 2016/6/4.
 */
@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseActivity {

    @ViewInject(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @ViewInject(R.id.tv_version_info)
    private TextView mVersionInfo;

    @ViewInject(R.id.iv_header_img)
    private ImageView mHeaderImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mVersionInfo.setText(String.format(getString(R.string.activity_about_version_info), CommonUtils.getVersionName(this), CommonUtils.getVersionCode(this)));
        if (CommonUtils.nowIsDay(this)) {
            mHeaderImg.setImageResource(R.drawable.img_header_day);
        } else {
            mHeaderImg.setImageResource(R.drawable.img_header_night);
        }
    }

    @Event(value = R.id.tv_open_source_address, type = View.OnClickListener.class)
    private void jumpToGithub(View view) {
        CommonUtils.jumpTo(this, getString(R.string.open_source_address));
    }

    @Event(value = R.id.rl_weibo, type = View.OnClickListener.class)
    private void jumpToWeibo(View view) {
        CommonUtils.jumpTo(this, "http://weibo.com/95han/");
    }

    @Event(value = R.id.rl_email, type = View.OnClickListener.class)
    private void copyEmailAddress(View view) {
        CommonUtils.copy2Clipboard(this, "hanjie95@126.com");
        SnackBarUtils.makeShort(this, mHeaderImg, getString(R.string.copy_success)).show();
    }

    @Event(value = R.id.fab, type = View.OnClickListener.class)
    private void checkLatestVersionInfo(View view) {
        FetchLatestVersionInfoTask.fetch(this, new FetchLatestVersionInfoTask.CallBack() {
            @Override
            public void onSuccess(final LatestVersion latestVersion) {
                int latestVersionCode = latestVersion.getVersionCode();
                if (latestVersionCode <= CommonUtils.getVersionCode(AboutActivity.this)) {
                    SnackBarUtils.makeShort(AboutActivity.this, mHeaderImg, getString(R.string.tip_app_no_update)).show();
                    return;
                }
                DialogUtils.showAlertDialog(AboutActivity.this, String.format(getString(R.string.dialog_title_app_update), latestVersion.getVersionName()), latestVersion.getChangelog(), getString(R.string.dialog_action_update_now), getString(R.string.dialog_action_remind_next_time), null, false, new DialogUtils.DialogCallBack() {
                    @Override
                    public void onPositiveButton(DialogInterface dialog, int which) {
                        CommonUtils.jumpTo(AboutActivity.this, latestVersion.getDownloadUrl());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeButton(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNeutralButton(DialogInterface dialog, int which) {

                    }
                });
            }

            @Override
            public void onFailed(String errorMsg) {
                SnackBarUtils.makeShort(AboutActivity.this, mHeaderImg, errorMsg).show();
            }
        });
    }

    public static void startAboutActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }


}
