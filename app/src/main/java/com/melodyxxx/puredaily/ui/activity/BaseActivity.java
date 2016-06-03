package com.melodyxxx.puredaily.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.utils.ActivityCollector;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.PrefUtils;

import org.xutils.x;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(CommonUtils.getSkinStyle(PrefUtils.getInt(this, PrefConstants.CURRENT_SKIN, PrefConstants.DEFAULT_SKIN)));
        x.view().inject(this);
        initToolBar();
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setToolbarTitle(String toolbarTitle) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(toolbarTitle);
        }
    }

    public void setToolbarTitle(int resId) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(resId));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
