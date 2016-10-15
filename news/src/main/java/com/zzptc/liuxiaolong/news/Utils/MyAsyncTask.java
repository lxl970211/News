package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.javabean.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by lxl97 on 2016/9/26.
 */

public class MyAsyncTask {
    private Context context;
    public interface OnGetUserInfoListener{
        void OnGetUserInfoListener(String name, String email);
    }
    private OnGetUserInfoListener onGetUserInfoListener;

    public void setOnGetUserInfoListener(OnGetUserInfoListener listener){
        onGetUserInfoListener = listener;
    }
    public MyAsyncTask(Context context){
        this.context = context;
    }

    public void getinfo(){
        new GetUserInfo().execute();
    }

    private class GetUserInfo extends AsyncTask<Void, String, User> {
        @Override
        protected User doInBackground(Void... params) {
            //token存在并且name不为空
            if (UserInfoAuthentication.tokenExists(context) && UserInfoAuthentication.getTokeninfo(context, "name") != ""){
                String name = UserInfoAuthentication.getTokeninfo(context, "name");
                String email = UserInfoAuthentication.getTokeninfo(context, "email");
                if (name.equals("")){
                    name = "登录";
                }
                publishProgress(name, email);
            }else {

                RequestParams rp = new RequestParams(StaticProperty.GETINFO_SERVLET);
                rp.addParameter("type", "BasicInfo");
                rp.addParameter("token", UserInfoAuthentication.getTokeninfo(context, "token"));


                x.http().post(rp, new Callback.CommonCallback<String>() {
                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onSuccess(String result) {
                        Gson g = new Gson();
                        User user = g.fromJson(result, User.class);
                        publishProgress(user.getUserName(), null);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onFinished() {

                    }


                });
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... str) {
            super.onProgressUpdate(str);
            if ( onGetUserInfoListener != null){
                SharedPreferences.Editor editor = context.getSharedPreferences("token", Context.MODE_PRIVATE).edit();
                editor.putString("name", str[0]);
                editor.commit();
                onGetUserInfoListener.OnGetUserInfoListener(str[0], str[1]);
            }
        }
    }
}
