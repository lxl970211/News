package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.javabean.ResultData;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.model.Result;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 上传用户保存的新闻、评论等数据
 * Created by lxl97 on 2016/10/1.
 */

public class PushData {
    private Gson gson;
    private Context context;
    public PushData(Context context){
        gson = new Gson();
        this.context = context;
    }


    public interface OnPushInfoListener{
        void OnPushCollectNewsListener(int status);
    }

    private OnPushInfoListener onPushInfoListener;
    public void setOnPushInfoListener(OnPushInfoListener listener){
        onPushInfoListener = listener;
    }

    public void pushCollectNews(NewsData newsdata, String type){
        new PushInfo().execute(new Object[]{newsdata, type});
    }

    class PushInfo extends AsyncTask<Object, Integer, Void>{
        @Override
        protected Void doInBackground(Object... params) {
            NewsData newsData = (NewsData) params[0];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time = sdf.format(new Date());
            RequestParams rp = new RequestParams(StaticProperty.SERVERURL+"UserCollectNewsManageServlet");
            if ("collectNews".equals(params[1])){              //收藏新闻
                rp.addParameter("type", params[1]);
                rp.addParameter("newsTitle", newsData.getTitle());
                rp.addParameter("newsUrl", newsData.getUrl());
                rp.addParameter("collectTime", time);
                rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
            }else if ("checkIsCollect".equals(params[1])){  //查看新闻是否已收藏
                rp.addParameter("type", params[1]);
                rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
                rp.addParameter("newsUrl", newsData.getUrl());

            }else if("deleteCollect".equals(params[1])){    //删除收藏的新闻

                rp.addParameter("type", params[1]);
                rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
                rp.addParameter("newsUrl", newsData.getUrl());

            }
            x.http().post(rp, new Callback.CommonCallback<String>() {
                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onSuccess(String result) {
                    ResultData res = gson.fromJson(result, ResultData.class);
                    publishProgress(res.getStatus());
                }

                @Override
                public void onFinished() {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (onPushInfoListener != null){
                onPushInfoListener.OnPushCollectNewsListener(values[0]);
            }
        }
    }

}
