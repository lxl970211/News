package com.zzptc.liuxiaolong.news.model;

import java.util.List;

/**
 * Created by lxl97 on 2016/7/26.
 */
public class Newsbean {

    private String reason;
    private Result result;
    private String error_code;




    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
