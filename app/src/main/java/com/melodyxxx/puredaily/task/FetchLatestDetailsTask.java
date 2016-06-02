package com.melodyxxx.puredaily.task;

import android.os.AsyncTask;

import com.melodyxxx.puredaily.constant.API;
import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.entity.LatestDetails;
import com.melodyxxx.puredaily.utils.ParserUtils;
import com.melodyxxx.puredaily.utils.Request;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * 获取最新消息
 * Created by hanjie on 2016/5/31.
 */
public class FetchLatestDetailsTask {

    private static final long CACHE_MAX_AGE = 86400000L * 7;

    public interface FetchLatestCallback {
        void onSuccess(LatestDetails latestDetails);

        void onError(String errorMsg);
    }

    public static void fetch(String latestId, final FetchLatestCallback fetchCallback) {
        Request.requestUrl(API.BASE_LATEST_DETAILS + latestId, CACHE_MAX_AGE, true, new Request.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                parseLatestResult(result, fetchCallback);
            }

            @Override
            public void onError(String errorMsg) {
                fetchCallback.onError(errorMsg);
            }
        });
    }

    private static void parseLatestResult(String result, final FetchLatestCallback fetchCallback) {
        new AsyncTask<String, Void, LatestDetails>() {

            private String errorMsg = null;

            @Override
            protected LatestDetails doInBackground(String... params) {
                String result = params[0];
                try {
                    return ParserUtils.parseLatestDetailsResult(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.errorMsg = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LatestDetails latestDetails) {
                if (latestDetails != null) {
                    fetchCallback.onSuccess(latestDetails);
                } else if (this.errorMsg != null) {
                    fetchCallback.onError(this.errorMsg);
                }
            }
        }.execute(result);
    }

}
