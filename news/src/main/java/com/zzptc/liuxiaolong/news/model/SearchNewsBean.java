package com.zzptc.liuxiaolong.news.model;

/**
 * Created by lxl97 on 2016/8/18.
 */
public class SearchNewsBean {

    private String status;
    private String msg;
    private Search_Result result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Search_Result getResult() {
        return result;
    }

    public void setResult(Search_Result result) {
        this.result = result;
    }
}
