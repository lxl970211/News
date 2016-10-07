package com.zzptc.liuxiaolong.news.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lxl97 on 2016/10/7.
 */

public class CommentBean implements Serializable {

    private List<Comment> list;

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }
}
