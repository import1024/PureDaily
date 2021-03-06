package com.melodyxxx.puredaily.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.db.Dao;
import com.melodyxxx.puredaily.entity.LatestVersion;
import com.melodyxxx.puredaily.task.FetchLatestVersionInfoTask;
import com.melodyxxx.puredaily.ui.fragment.CollectionsFragment;
import com.melodyxxx.puredaily.ui.fragment.ColorPickerDialogFragment;
import com.melodyxxx.puredaily.ui.fragment.LatestFragment;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.DialogUtils;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.StatusBarUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;

    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;

    private ImageView mNavHeaderImgView;

    private SwitchCompat mPicModeSwitch;

    private TextView mCollectionsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTopLayoutColor(this, (FrameLayout) findViewById(R.id.top_layout), CommonUtils.getThemePrimaryColor(this));
        setToolbarTitle(null);
        initDrawerLayout();
        initNavView();
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            switchFragment(new LatestFragment());
        }
        if (PrefUtils.getBoolean(this, PrefConstants.AUTO_CHECK_APP_UPDATE, PrefConstants.DEFAULT_CHECK_APP_UPDATE)) {
            checkLatestVersionInfo();
        }
    }

    private void checkLatestVersionInfo() {
        FetchLatestVersionInfoTask.fetch(this, new FetchLatestVersionInfoTask.CallBack() {
            @Override
            public void onSuccess(final LatestVersion latestVersion) {
                int latestVersionCode = latestVersion.getVersionCode();
                if (latestVersionCode <= CommonUtils.getVersionCode(HomeActivity.this)) {
                    return;
                }
                DialogUtils.showAlertDialog(HomeActivity.this, String.format(getString(R.string.dialog_title_app_update), latestVersion.getVersionName()), latestVersion.getChangelog(), getString(R.string.dialog_action_update_now), getString(R.string.dialog_action_remind_next_time), null, false, new DialogUtils.DialogCallBack() {
                    @Override
                    public void onPositiveButton(DialogInterface dialog, int which) {
                        CommonUtils.jumpTo(HomeActivity.this, latestVersion.getDownloadUrl());
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

            }
        });
    }

    private void initNavView() {
        // 初始化抽屉Header ImageView
        mNavHeaderImgView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.iv_header);
        if (CommonUtils.nowIsDay(this)) {
            mNavHeaderImgView.setImageResource(R.drawable.img_header_day);
        } else {
            mNavHeaderImgView.setImageResource(R.drawable.img_header_night);
        }
        // 初始化抽屉无图模式SwitchCompat
        Menu menu = mNavigationView.getMenu();
        MenuItem picModeMenuItem = menu.findItem(R.id.nav_pic_mode);
        mPicModeSwitch = (SwitchCompat) MenuItemCompat.getActionView(picModeMenuItem).findViewById(R.id.view_switch);
        mPicModeSwitch.setChecked(PrefUtils.getBoolean(this, PrefConstants.MODE_NO_PIC, false));
        mPicModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefUtils.putBoolean(HomeActivity.this, PrefConstants.MODE_NO_PIC, isChecked);
                mPicModeSwitch.setChecked(isChecked);
                switchFragment(new LatestFragment());
            }
        });
        // 初始化收藏夹数量TextView
        MenuItem collectionsMenuItem = menu.findItem(R.id.nav_collections);
        mCollectionsCount = (TextView) MenuItemCompat.getActionView(collectionsMenuItem).findViewById(R.id.text_view);
    }

    public void updateCollectionsCount() {
        mCollectionsCount.setText(String.valueOf(Dao.getInstance(this).getCountOfCollections()));
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCollectionsCount();
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                SettingsActivity.startSettingsActivity(this);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_latest_daily: {
                LatestFragment latestFragment = new LatestFragment();
                switchFragment(latestFragment);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_collections: {
                CollectionsFragment collectionsFragment = new CollectionsFragment();
                switchFragment(collectionsFragment);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_skin: {
                ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
                fragment.show(getSupportFragmentManager(), "color_picker");
                break;
            }
            case R.id.nav_about: {
                AboutActivity.startAboutActivity(this);
                break;
            }
            case R.id.nav_settings: {
                SettingsActivity.startSettingsActivity(this);
                break;
            }
        }
        return false;
    }

}
