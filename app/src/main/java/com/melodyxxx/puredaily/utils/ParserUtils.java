package com.melodyxxx.puredaily.utils;

import com.melodyxxx.puredaily.entity.Latest;
import com.melodyxxx.puredaily.entity.LatestDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 数据解析
 * <p>
 * Created by hanjie on 2016/5/31.
 */
public class ParserUtils {

    public static ArrayList<Latest> parseLatestResult(String result) throws JSONException {
        ArrayList<Latest> stories = new ArrayList<Latest>();
        JSONObject resultJObj = new JSONObject(result);
        JSONArray storiesJArray = resultJObj.getJSONArray("stories");
        JSONObject storyJObj = null;
        JSONArray imagesJArray = null;
        for (int pos = 0; pos < storiesJArray.length(); pos++) {
            storyJObj = storiesJArray.getJSONObject(pos);
            Latest latest = new Latest();
            latest.setTitle(storyJObj.getString("title"));
            latest.setGa_prefix(storyJObj.getString("ga_prefix"));
            latest.setType(storyJObj.getString("type"));
            latest.setId(storyJObj.getString("id"));
            if (storyJObj.has("multipic")) {
                latest.setMultipic(storyJObj.getBoolean("multipic"));
            }
            imagesJArray = storyJObj.getJSONArray("images");
            if (imagesJArray.length() != 0) {
                latest.setImageUrl(imagesJArray.getString(0));
            }
            stories.add(latest);
        }
        return stories;
    }

    public static LatestDetails parseLatestDetailsResult(String result) throws JSONException {
        LatestDetails latestDetails = new LatestDetails();
        JSONObject resultJObj = new JSONObject(result);
        latestDetails.setBody(resultJObj.getString("body"));
        latestDetails.setImageSource(resultJObj.getString("image_source"));
        latestDetails.setTitle(resultJObj.getString("title"));
        latestDetails.setImageUrl(resultJObj.getString("image"));
        latestDetails.setShareUrl(resultJObj.getString("share_url"));
        latestDetails.setId(resultJObj.getString("id"));
        latestDetails.setType(resultJObj.getString("type"));
        return latestDetails;
    }

}
