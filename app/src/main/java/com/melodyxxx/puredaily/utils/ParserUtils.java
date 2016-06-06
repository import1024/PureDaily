package com.melodyxxx.puredaily.utils;

import com.melodyxxx.puredaily.entity.Comment;
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

    /**
     * 解析Latest最新消息
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public static ArrayList<Latest> parseLatestResult(String result) throws JSONException {
        ArrayList<Latest> stories = new ArrayList<Latest>();
        JSONObject resultJObj = new JSONObject(result);
        JSONArray latestsJArray = resultJObj.getJSONArray("stories");
        JSONObject latestJObj = null;
        JSONArray imagesJArray = null;
        for (int pos = 0; pos < latestsJArray.length(); pos++) {
            latestJObj = latestsJArray.getJSONObject(pos);
            Latest latest = new Latest();
            latest.setTitle(latestJObj.getString("title"));
            latest.setGa_prefix(latestJObj.getString("ga_prefix"));
            latest.setType(latestJObj.getString("type"));
            latest.setId(latestJObj.getString("id"));
            if (latestJObj.has("multipic")) {
                latest.setMultipic(latestJObj.getBoolean("multipic"));
            }
            imagesJArray = latestJObj.getJSONArray("images");
            if (imagesJArray.length() != 0) {
                latest.setImageUrl(imagesJArray.getString(0));
            }
            stories.add(latest);
        }
        return stories;
    }

    /**
     * 解析Latest内容
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public static LatestDetails parseLatestDetailsResult(String result) throws JSONException {
        LatestDetails latestDetails = new LatestDetails();
        JSONObject resultJObj = new JSONObject(result);
        latestDetails.setBody(resultJObj.getString("body"));
        latestDetails.setImageSource(resultJObj.getString("image_source"));
        latestDetails.setTitle(resultJObj.getString("title"));
        latestDetails.setImageUrl(resultJObj.getString("image"));
        JSONArray smallImagesJArray = resultJObj.getJSONArray("images");
        if (smallImagesJArray.length() != 0) {
            latestDetails.setSmallImageUrl(smallImagesJArray.getString(0));
        }
        latestDetails.setShareUrl(resultJObj.getString("share_url"));
        latestDetails.setId(resultJObj.getString("id"));
        latestDetails.setType(resultJObj.getString("type"));
        return latestDetails;
    }

    /**
     * 解析长评论
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public static ArrayList<Comment> parseLongComments(String result) throws JSONException {
        return parseComments(result);
    }

    /**
     * 解析短评论
     *
     * @param result
     * @return
     * @throws JSONException
     */
    public static ArrayList<Comment> parseShortComments(String result) throws JSONException {
        return parseComments(result);
    }


    /**
     * 解析评论
     *
     * @param result
     * @return
     * @throws JSONException
     */
    private static ArrayList<Comment> parseComments(String result) throws JSONException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        JSONObject resultJObj = new JSONObject(result);
        JSONArray commentsJArray = resultJObj.getJSONArray("comments");
        JSONObject commentJObj = null;
        for (int pos = 0; pos < commentsJArray.length(); pos++) {
            commentJObj = commentsJArray.getJSONObject(pos);
            Comment comment = new Comment();
            comment.setAuthor(commentJObj.getString("author"));
            comment.setContent(commentJObj.getString("content"));
            comment.setAvatar(commentJObj.getString("avatar"));
            comment.setTime(commentJObj.getString("time"));
            comment.setId(commentJObj.getString("id"));
            comment.setLikes(commentJObj.getString("likes"));
            comments.add(comment);
        }
        return comments;
    }

}
