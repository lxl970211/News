package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.javabean.RegisterInfoBean;
import com.zzptc.liuxiaolong.news.model.LoginResult;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by lxl97 on 2016/8/24.
 */
public class LinkService {


    public void selectUserName(String userName){
        String loginservlet = "http://10.0.2.2:8080/News/servlet/LoginServlet";
        final String registerservlet = "http://10.0.2.2:8080/News/servlet/RegisterServlet";
        RequestParams rp = new RequestParams(registerservlet);
        rp.addParameter("regUserName", userName);
        x.http().post(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson g = new Gson();
                LoginResult lr = g.fromJson(result, LoginResult.class);
                Log.i("LinkService", lr.getStatus());

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }





}
