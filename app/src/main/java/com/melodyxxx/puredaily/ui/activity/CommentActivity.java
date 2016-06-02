package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.utils.StatusBarUtils;

import org.xutils.view.annotation.ContentView;

/**
 * 评论页
 * <p>
 * Created by hanjie on 2016/6/2.
 */
@ContentView(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        setToolbarTitle(R.string.activity_title_comment);
    }

    public static void startCommentActivity(Activity activity, Latest latest) {
        Intent intent = new Intent(activity, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", latest);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

}
