package com.melodyxxx.puredaily.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.API;
import com.melodyxxx.puredaily.entity.LatestVersion;
import com.melodyxxx.puredaily.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Pure Daily 应用检查更新
 */
public class FetchLatestVersionInfoTask {

    private static final int RESULT_FETCH_SUCCESS = 0;
    private static final int RESULT_DATA_PARSE_ERROR = 1;
    private static final int RESULT_BAD_NETWORK = 2;

    public interface CallBack {
        void onSuccess(LatestVersion latestVersion);

        void onFailed(String errorMsg);
    }

    public static void fetch(final Context context, @NonNull final CallBack callBack) {
        new AsyncTask<Void, Void, Integer>() {

            LatestVersion latestVersion = null;

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    String result = HttpUtils.requestData(API.LATEST_VERSION_INFO);
                    latestVersion = parseResult(result);
                    return RESULT_FETCH_SUCCESS;
                } catch (IOException e) {
                    e.printStackTrace();
                    return RESULT_BAD_NETWORK;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return RESULT_DATA_PARSE_ERROR;
                }
            }

            @Override
            protected void onPostExecute(Integer resultCode) {
                super.onPostExecute(resultCode);
                switch (resultCode) {
                    case RESULT_FETCH_SUCCESS: {
                        callBack.onSuccess(latestVersion);
                        break;
                    }
                    case RESULT_BAD_NETWORK: {
                        callBack.onFailed(context.getString(R.string.tip_bad_network));
                        break;
                    }
                    case RESULT_DATA_PARSE_ERROR: {
                        callBack.onFailed(context.getString(R.string.tip_data_error));
                        break;
                    }
                }
            }
        }.execute();
    }

    private static LatestVersion parseResult(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        LatestVersion latestVersion = new LatestVersion();
        latestVersion.setVersionName(jsonObject.getString("version_name"));
        latestVersion.setVersionCode(jsonObject.getInt("version_code"));
        latestVersion.setUpdateTime(jsonObject.getString("update_time"));
        latestVersion.setChangelog(jsonObject.getString("changelog"));
        latestVersion.setDownloadUrl(jsonObject.getString("download_url"));
        return latestVersion;
    }

}
