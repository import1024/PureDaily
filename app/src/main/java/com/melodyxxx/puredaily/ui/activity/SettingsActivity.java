package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.entity.LatestVersion;
import com.melodyxxx.puredaily.task.FetchLatestVersionInfoTask;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.DialogUtils;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.StatusBarUtils;
import com.melodyxxx.puredaily.widget.CheckBoxItemView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 设置界面
 * <p>
 * Created by hanjie on 2016/6/4.
 */
@ContentView(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {

    @ViewInject(R.id.item_auto_check_app_update)
    private CheckBoxItemView mAutoCheckAppUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, CommonUtils.getThemePrimaryColor(this));
        setToolbarTitle(R.string.activity_title_settings);
        init();
    }

    private void init() {
        mAutoCheckAppUpdate.setChecked(PrefUtils.getBoolean(this, PrefConstants.AUTO_CHECK_APP_UPDATE, PrefConstants.DEFAULT_CHECK_APP_UPDATE));
    }

    @Event(value = R.id.item_auto_check_app_update)
    private void OnAutoCheckAppUpdateItemClick(View view) {
        boolean isChecked = mAutoCheckAppUpdate.isChecked();
        PrefUtils.putBoolean(this, PrefConstants.AUTO_CHECK_APP_UPDATE, !isChecked);
        mAutoCheckAppUpdate.setChecked(!isChecked);
    }

    public static void startSettingsActivity(Activity activity) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }
}
