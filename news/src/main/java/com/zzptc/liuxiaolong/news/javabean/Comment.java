package com.zzptc.liuxiaolong.news.javabean;

import java.io.Serializable;

/**
 * Created by lxl97 on 2016/10/6.
 */

public class Comment implements Serializable{
    private String token;
    private String name;
    private String commentTime;
    private String content;
    private int like;
    private String newsId;//以新闻url为id
    private Integer lou;
    private String title;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getLike() {
        return like;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsId() {
        return newsId;
    }


    public void setLou(Integer lou) {
        this.lou = lou;
    }

    public Integer getLou() {
        return lou;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
