package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.javabean.ResultData;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
        void OnGetRequestDataListener(String json);
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

    /**
     * 写评论
     * @param content
     * @param newsId
     * @param title
     */
    public void writeComment (String content, String newsId, String title){
        RequestParams rp = new RequestParams(StaticProperty.COMMENT_SERVLET);
        if (UserInfoAuthentication.tokenExists(context)){
            rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        }else{
            rp.addParameter("name", "匿名");
        }
            rp.addParameter("content", content);
            rp.addParameter("type", "writeComment");
            rp.addParameter("commentTime", MyUtils.getNowTime());
            rp.addParameter("newsId", newsId);
            rp.addParameter("title", title);
        new GetRequestStatus().execute(rp);
    }




    /**
     * 获取新闻评论列表
     * @param newsId
     */
    public void getCommentList(String newsId){
        RequestParams rp = new RequestParams(StaticProperty.COMMENT_SERVLET);
        rp.addParameter("type", "commentList");
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        rp.addParameter("newsId", newsId);
        new GetRequestData().execute(rp);
    }



    /**
     * like 点赞
     * @param newsId
     * @param lou
     */
    public void like(String newsId, int lou){
        RequestParams rp = new RequestParams(StaticProperty.COMMENT_SERVLET);
        rp.addParameter("type", "like");
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        rp.addParameter("newsId", newsId);
        rp.addParameter("lou", lou);
        new GetRequestStatus().execute(rp);

    }
    /**
     * 获取评论列表
     */
    public void getCollectNewsList(){
        RequestParams rp = new RequestParams(StaticProperty.USER_COLLECT_NEWS_MANAGE_SERVLET);
        rp.addParameter("type", "getCollectNewsList");
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        new GetRequestData().execute(rp);

    }

    /**
     * 收藏新闻
     * @param newsTitle
     * @param newsUrl
     */
    public void collectNews(String newsTitle, String newsUrl){
        RequestParams rp = new RequestParams(StaticProperty.USER_COLLECT_NEWS_MANAGE_SERVLET);
        rp.addParameter("type", "collectNews");
        rp.addParameter("newsTitle", newsTitle);
        rp.addParameter("newsUrl", newsUrl);
        rp.addParameter("collectTime",  MyUtils.getNowTime());
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        new GetRequestStatus().execute(rp);
    }

    /**
     * 检查新闻是否已收藏
     * @param newsUrl
     */
    public void checkIsCollect(String newsUrl){
        RequestParams rp = new RequestParams(StaticProperty.USER_COLLECT_NEWS_MANAGE_SERVLET);
        rp.addParameter("type", "checkIsCollect");
        rp.addParameter("newsUrl", newsUrl);
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        new GetRequestStatus().execute(rp);
    }

    /**
     * 删除收藏
     */
    public void deleteCollect(){
        RequestParams rp = new RequestParams(StaticProperty.USER_COLLECT_NEWS_MANAGE_SERVLET);
        rp.addParameter("type", "deleteCollect");
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        new GetRequestStatus().execute(rp);
    }

    /**
     * 用户评论列表
     */
    public void getMyCommentList(){
        RequestParams rp = new RequestParams(StaticProperty.COMMENT_SERVLET);
        rp.addParameter("type", "userCommentList");
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        new GetRequestData().execute(rp);
    }

    /**
     * 更新用户名
     * @param newName
     */
    public void updateUserName(String newName){
        RequestParams rp = new RequestParams(StaticProperty.GETINFO_SERVLET);
        rp.addParameter("type", "updateName");
        rp.addParameter("newName", newName);
        rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));
        new GetRequestStatus().execute(rp);
    }

    /**
     * 意见反馈
     * @param info
     * @param Contactinformation
     */
    public void feedback(String info, String Contactinformation){
        RequestParams rp = new RequestParams(StaticProperty.FEEDBACK_SERVLET);
        rp.addParameter("info", info);
        rp.addParameter("contact", Contactinformation);
        new GetRequestStatus().execute(rp);
    }



    /**
     * 检查软件更新
     */
    public void checkUpdate(){
        RequestParams rp = new RequestParams(StaticProperty.CHECKUPDATE_SERVLET);
        new GetRequestData().execute(rp);
    }
    public class GetRequestStatus extends AsyncTask<RequestParams, Integer, Void>{

        @Override
        protected Void doInBackground(RequestParams... params) {
            RequestParams rp = params[0];
            x.http().post(rp, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultData res = gson.fromJson(result, ResultData.class);
                    publishProgress(res.getStatus());
                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

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


    public class GetRequestData extends AsyncTask<RequestParams, String, Void>{
        @Override
        protected Void doInBackground(RequestParams... params) {
            RequestParams rp = params[0];
            x.http().post(rp, new Callback.CommonCallback<String>() {
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onSuccess(String result) {
                    publishProgress(result);
                }

                @Override
                public void onCancelled(CancelledException cex) {

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
            if (onPushInfoListener != null){
                onPushInfoListener.OnGetRequestDataListener(values[0]);
            }
        }
    }



}
