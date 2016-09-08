package com.zzptc.liuxiaolong.news.model;

import java.util.List;

/**
 * Created by lxl97 on 2016/7/27.
 */
public class Result {

    private String stat;
    private List<NewsData> data;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<NewsData> getData() {
        return data;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }
}
