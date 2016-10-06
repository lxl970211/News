package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.javabean.ResultData;
import com.zzptc.liuxiaolong.news.model.Comment;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.model.Result;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
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

    //接口 得到用户收藏新闻列表的json数据
    public interface OnPushInfoListener{
        void OnGetUserCollectNewsListListener(String json);
    }



    private OnPushInfoListener onPushInfoListener;
    private OnRequestResultListener onRequestResultListener;

    //获取请求结果监听
    public void setOnRequestResultListener(OnRequestResultListener listener){
        onRequestResultListener = listener;
    }

    //上传用户评论监听
    public void setOnPushInfoListener(OnPushInfoListener listener){
        onPushInfoListener = listener;
    }
    //上传收藏新闻方法
    public void pushCollectNews(NewsData newsdata, String type){
        new PushInfo().execute(new Object[]{newsdata, type});
    }


    public void sendComment (Comment comment){
        new MyComment().execute(comment);

    }

    class PushInfo extends AsyncTask<Object, String, Void>{
        @Override
        protected Void doInBackground(final Object... params) {
            NewsData newsData = (NewsData) params[0];

            RequestParams rp = new RequestParams(StaticProperty.SERVERURL+"UserCollectNewsManageServlet");
            if ("collectNews".equals(params[1])){              //收藏新闻
                rp.addParameter("type", params[1]);
                rp.addParameter("newsTitle", newsData.getTitle());
                rp.addParameter("newsUrl", newsData.getUrl());
                rp.addParameter("collectTime", MyUtils.getNowTime());

            }else if ("checkIsCollect".equals(params[1])){  //查看新闻是否已收藏
                rp.addParameter("type", params[1]);
                rp.addParameter("newsUrl", newsData.getUrl());

            }else if("deleteCollect".equals(params[1])){    //删除收藏的新闻
                rp.addParameter("type", params[1]);
                rp.addParameter("newsUrl", newsData.getUrl());

            }else if ("getCollectNewsList".equals(params[1])){
                rp.addParameter("type", "getCollectNewsList");

            }
            rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
            x.http().post(rp, new Callback.CommonCallback<String>() {
                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onSuccess(String result) {
                    if (!"getCollectNewsList".equals(params[1])) {
                        ResultData res = gson.fromJson(result, ResultData.class);
                        publishProgress(String.valueOf(res.getStatus()), "");
                    }else{
                        publishProgress(result, "getCollectNewsList");
                    }
                }

                @Override
                public void onFinished() {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[1] == null || values[1].equals("")) {
                if (onRequestResultListener != null){
                    onRequestResultListener.OnGetRequestResultStatusListener(Integer.parseInt(values[0]));
                }
            }else {
                if (onPushInfoListener != null) {
                    try {
                        String json = new String(values[0].getBytes(), "utf-8");
                        onPushInfoListener.OnGetUserCollectNewsListListener(json);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }





    class MyComment extends AsyncTask<Comment, Integer, Void>{

        @Override
        protected Void doInBackground(Comment... params) {
            RequestParams rp = new RequestParams(StaticProperty.SERVERURL+"CommentServlet");
            if (UserInfoAuthentication.tokenExists(context)){
                System.out.println(UserInfoAuthentication.getTokeninfo(context, "token"));
                rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
            }else{
                rp.addParameter("name", "匿名");
            }
            rp.addParameter("commentTime", MyUtils.getNowTime());
            rp.addParameter("content", params[0].getContent());
            rp.addParameter("newsId", params[0].getNewsId());

            x.http().post(rp, new Callback.CommonCallback<String>() {
                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    ResultData res = gson.fromJson(result, ResultData.class);
                    publishProgress(res.getStatus());
                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (onRequestResultListener != null){
                onRequestResultListener.OnGetRequestResultStatusListener(values[0]);
            }
        }
    }

}
