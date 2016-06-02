package com.melodyxxx.puredaily.task;

import android.os.AsyncTask;

import com.melodyxxx.puredaily.constant.API;
import com.melodyxxx.puredaily.entity.Comment;
import com.melodyxxx.puredaily.utils.ParserUtils;
import com.melodyxxx.puredaily.utils.Request;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * 获取评论
 * Created by hanjie on 2016/6/2.
 */
public class FetchCommentTask {

    private static final long CACHE_MAX_AGE = 86400000L * 7;

    public static final int TYPE_LONG_COMMENT = 0;

    public static final int TYPE_SHORT_COMMENT = 1;

    public interface FetchLatestCallback {
        void onSuccess(ArrayList<Comment> comments);

        void onError(String errorMsg);
    }

    /**
     * 获取评论数据
     *
     * @param id            文章id
     * @param type          长评论:{@link #TYPE_LONG_COMMENT} or 短评论:{@link #TYPE_SHORT_COMMENT}
     * @param fetchCallback
     */
    public static void fetch(String id, final int type, final FetchLatestCallback fetchCallback) {
        String api = null;
        if (type == TYPE_LONG_COMMENT) {
            api = API.BASE_COMMENT + id + API.PARAM_LONG_COMMENT;
        } else if (type == TYPE_SHORT_COMMENT) {
            api = API.BASE_COMMENT + id + API.PARAM_SHORT_COMMENT;
        }
        Request.requestUrl(api, CACHE_MAX_AGE, false, new Request.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                parseCommentResult(result, type, fetchCallback);
            }

            @Override
            public void onError(String errorMsg) {
                fetchCallback.onError(errorMsg);
            }
        });
    }

    private static void parseCommentResult(String result, final int type, final FetchLatestCallback fetchCallback) {
        new AsyncTask<String, Void, ArrayList<Comment>>() {

            private String errorMsg = null;

            @Override
            protected ArrayList<Comment> doInBackground(String... params) {
                String result = params[0];
                try {
                    if (type == TYPE_LONG_COMMENT) {
                        return ParserUtils.parseLongComments(result);
                    } else if (type == TYPE_SHORT_COMMENT) {
                        return ParserUtils.parseShortComments(result);
                    } else {
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.errorMsg = e.toString();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Comment> comments) {
                if (comments != null) {
                    fetchCallback.onSuccess(comments);
                } else if (this.errorMsg != null) {
                    fetchCallback.onError(this.errorMsg);
                }
            }
        }.execute(result);
    }

}
