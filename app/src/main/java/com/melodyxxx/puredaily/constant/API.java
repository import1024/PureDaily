package com.melodyxxx.puredaily.constant;

/**
 * 项目所需要的所有API: https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90
 * <p>
 * <p>
 * Created by hanjie on 2016/5/31.
 */
public class API {

    // 最新消息
    public static final String LATEST = "http://news-at.zhihu.com/api/4/news/latest";
    // 内容获取
    public static final String BASE_LATEST_DETAILS = "http://news-at.zhihu.com/api/4/news/";
    // 评论
    public static final String BASE_COMMENT = "http://news-at.zhihu.com/api/4/story/";
    // 长评论 - with BASE_COMMENT
    public static final String PARAM_LONG_COMMENT = "/long-comments";
    // 短评论 - with BASE_COMMENT
    public static final String PARAM_SHORT_COMMENT = "/short-comments";

}
