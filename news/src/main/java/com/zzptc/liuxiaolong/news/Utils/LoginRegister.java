package com.zzptc.liuxiaolong.news.Utils;

import android.os.AsyncTask;


import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.javabean.ResultData;
import com.zzptc.liuxiaolong.news.javabean.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by lxl97 on 2016/9/28.
 */

public class LoginRegister {
    private Gson gson;

    public LoginRegister(){
        gson = new Gson();
    }

    public interface OnLoginRegisterListener{
        void OnGetServerResponseCodesListener(int ResponseCodes, String token);

    }
    private OnLoginRegisterListener onloginregisterlistener;

    public void setOnLoginRegisterListener(OnLoginRegisterListener onlistener){
        onloginregisterlistener = onlistener;
    }
    public void login(User user){
        new LinkServer().execute(user);
    }
    public void register(User user){
        new LinkServer().execute(user);
    }

    class LinkServer extends AsyncTask<User, String, Void>{

        @Override
        protected Void doInBackground(User... params) {
            final User user = params[0];
            RequestParams rp = null;
            //注册
            if ("register".equals(user.getType())) {
                rp = new RequestParams(StaticProperty.REGISTER_SERVLET);
                //请求参数
                rp.addParameter("type", user.getType());
                rp.addParameter("user_name", user.getUserName());
                rp.addParameter("user_email", user.getUserEmail());
                rp.addParameter("user_password", user.getUserPassword());
            } else if ("login".equals(user.getType())){     //登录
                rp = new RequestParams(StaticProperty.LOGIN_SERVLET);
                rp.addParameter("user_email", params[0].getUserEmail());
                rp.addParameter("user_password", params[0].getUserPassword());
                rp.addParameter("token", MyUtils.myToken(params[0].getUserPassword(), params[0].getUserEmail()));

            }else if("email".equals(user.getType())) {  //验证邮箱
                if (!MyUtils.EditTextFormat(user.getUserEmail(), StaticProperty.EMAIL_REGULAT_EXPRESSIONS)) {
                    publishProgress(String.valueOf(ResultCodes.EMAIL_ERROR), null);
                } else {
                    rp = new RequestParams(StaticProperty.REGISTER_SERVLET);
                    rp.addParameter("type", user.getType());
                    rp.addParameter("user_email", user.getUserEmail());
                }
            }
            x.http().post(rp, new Callback.CommonCallback<String>() {

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onSuccess(String result) {
                        ResultData lr = gson.fromJson(result, ResultData.class);
                    if ("email".equals(user.getType())) {
                        publishProgress(String.valueOf(lr.getStatus()), null);
                        System.out.println(lr.getStatus());
                    }else{
                        publishProgress(String.valueOf(lr.getStatus()), lr.getToken());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {


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
            System.out.println(values[1]);
            if (values[1] != null) {
                System.out.println("!null"+ values[0]);
                if (onloginregisterlistener != null) {
                    onloginregisterlistener.OnGetServerResponseCodesListener(Integer.parseInt(values[0]), values[1]);
                }
            }else{
                System.out.println("null"+ values[0]);
                onloginregisterlistener.OnGetServerResponseCodesListener(Integer.parseInt(values[0]), null);
            }
        }
    }
}
