package com.melodyxxx.puredaily.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by hanjie on 2016/5/31.
 */
public class Latest {

    private String title;
    private String imageUrl;
    private String ga_prefix;
    private String type;
    private String id;
    private boolean multipic;

    public Latest() {

    }

    public Latest(String title, String imageUrl, String ga_prefix, String type, String id, boolean multipic) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.ga_prefix = ga_prefix;
        this.type = type;
        this.id = id;
        this.multipic = multipic;
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

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
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

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }

    @Override
    public String toString() {
        return "Latest{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", multipic=" + multipic +
                '}';
    }
}
