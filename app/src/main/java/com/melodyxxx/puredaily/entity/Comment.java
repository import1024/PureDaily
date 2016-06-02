package com.melodyxxx.puredaily.entity;

/**
 * Created by hanjie on 2016/6/2.
 */
public class Comment {

    private String author;
    private String id;
    private String content;
    private String likes;
    private String time;
    private String avatar;

    public Comment() {
    }

    public Comment(String author, String id, String content, String likes, String time, String avatar) {
        this.author = author;
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.time = time;
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", likes='" + likes + '\'' +
                ", time='" + time + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
