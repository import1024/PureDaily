package com.melodyxxx.puredaily.utils;

import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 数据请求
 * <p>
 * Created by hanjie on 2016/5/31.
 */
public class Request {

    public interface RequestCallback {
        void onSuccess(String result);

        void onError(String errorMsg);
    }

    public static void requestUrl(String url, long cacheMaxAge, final boolean trustCache, final RequestCallback requestCallback) {
        RequestParams params = new RequestParams(url);
        params.setCacheMaxAge(cacheMaxAge);
        x.http().get(params, new Callback.CacheCallback<String>() {

            private boolean hasError = false;
            private String result = null;
            private String errorMsg = null;

            @Override
            public void onSuccess(String result) {
                Log.d("bingo", "onSuccess:" + result);
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                errorMsg = "请检查网络环境设置";
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (this.result != null) {
                    requestCallback.onSuccess(this.result);
                } else {
                    if (this.hasError && errorMsg != null) {
                        requestCallback.onError(errorMsg);
                    }
                }
            }

            /**
             * 缓存过期不会调用该方法
             */
            @Override
            public boolean onCache(String result) {
                Log.d("bingo", "onCache:" + result);
                this.result = result;
//                requestCallback.onSuccess(this.result);
                // 返回false表示不信任获取到的缓存数据，将继续请求网络获取数据来覆盖缓存数据
                // 返回true表示信任获取到的缓存的数据，将不会继续请求网络获取数据
                return trustCache;
            }
        });
    }

}
