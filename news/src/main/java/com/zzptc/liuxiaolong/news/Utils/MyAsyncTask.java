package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.javabean.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by lxl97 on 2016/9/26.
 */

public class MyAsyncTask {
    private Context context;
    public interface OnGetUserInfoListener{
        void OnGetUserInfoListener(User user);
    }
    private OnGetUserInfoListener onGetUserInfoListener;

    public void setOnGetUserInfoListener(OnGetUserInfoListener listener){
        onGetUserInfoListener = listener;
    }
    public MyAsyncTask(Context context){
        this.context = context;
    }



    public void getUserInfo() {
        new GetUserInfo().execute();

    }





    private class GetUserInfo extends AsyncTask<Void, User, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Environment.getExternalStorageDirectory() + "/MyNews/image.jpg");
            if (UserInfoAuthentication.tokenExists(context) && UserInfoAuthentication.getTokeninfo(context, "name") != "" && file.exists()){
                String name = UserInfoAuthentication.getTokeninfo(context, "name");
                String email = UserInfoAuthentication.getTokeninfo(context, "email");
                String imageurl = ImageDownloader.Scheme.FILE.wrap(file.getPath());
                User user = new User();
                user.setUserName(name);
                user.setUserEmail(email);
                user.setHeadPath(imageurl);

                publishProgress(user);
            } else{


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
                        String path = user.getHeadPath();

                        user.setHeadPath(StaticProperty.DOWNLOAD_URL+path);
                        publishProgress(user);
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
        protected void onProgressUpdate(User... str) {
            super.onProgressUpdate(str);
            if ( onGetUserInfoListener != null){
                User user = str[0];

                if (UserInfoAuthentication.getTokeninfo(context, "name") == ""){
                    SharedPreferences.Editor editor = context.getSharedPreferences("token", Context.MODE_PRIVATE).edit();
                    editor.putString("name", user.getUserName());
                    editor.commit();

                }


                onGetUserInfoListener.OnGetUserInfoListener(user);
            }
        }
    }
}
