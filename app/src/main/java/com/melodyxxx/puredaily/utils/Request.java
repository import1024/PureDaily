package com.melodyxxx.puredaily.utils;

import android.util.Log;

import org.xutils.common.Callback;
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
                Log.d("bingo", "onError:ex:" + ex.toString() + " isOnCallback:" + isOnCallback);
                errorMsg = ex.toString();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("bingo", "onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                if (this.result != null) {
                    Log.d("bingo", "onFinished:获取数据成功");
                    requestCallback.onSuccess(this.result);
                    if (this.hasError) {
                        Log.d("bingo", "BUT,网络连接失败，这次从缓存获取的");
                    }
                } else {
                    if (this.hasError && errorMsg != null) {
                        Log.d("bingo", "onFinished:获取数据失败");
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
