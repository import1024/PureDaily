package com.melodyxxx.puredaily.entity;

/**
 * Created by hanjie on 2016/6/1.
 */
public class LatestDetails {

    private String body;
    private String imageSource;
    private String title;
    private String imageUrl;
    private String shareUrl;
    private String type;
    private String id;

    public LatestDetails() {

    }

    public LatestDetails(String body, String imageSource, String title, String imageUrl, String shareUrl, String type, String id) {
        this.body = body;
        this.imageSource = imageSource;
        this.title = title;
        this.imageUrl = imageUrl;
        this.shareUrl = shareUrl;
        this.type = type;
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LatestDetails{" +
                "body='" + body + '\'' +
                ", imageSource='" + imageSource + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
