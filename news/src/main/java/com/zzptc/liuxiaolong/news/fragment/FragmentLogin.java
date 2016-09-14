package com.zzptc.liuxiaolong.news.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.MyApplication;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.javabean.User;
import com.zzptc.liuxiaolong.news.model.LoginResult;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.body.StringBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_login)
public class FragmentLogin extends Fragment {

    @ViewInject(R.id.iv_userEmail_img)
    private ImageView ivEmail;
    @ViewInject(R.id.iv_userPassword_img)
    private ImageView ivPwd;

    @ViewInject(R.id.et_userEmail)
    private EditText et_userEmail;
    @ViewInject(R.id.et_userPassword)
    private EditText et_userPassword;

    @ViewInject(R.id.btn_login)
    private Button submit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        init();


        return view;
    }


    public void init(){

        //修改icon颜色
        ivEmail.setColorFilter(getResources().getColor(R.color.blue));
        ivPwd.setColorFilter(getResources().getColor(R.color.blue));

    }

    @Event(R.id.btn_login)
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.btn_login:
                User user = new User();
                user.setUser_email(et_userEmail.getText().toString());
                String pwd = MyUtils.MD5(et_userPassword.getText().toString());
                user.setUser_pwd(pwd);
                new Login().execute(user);

                break;

        }

    }


    class Login extends AsyncTask<User, String , Void>{


        @Override
        protected Void doInBackground(User... params) {

            RequestParams rp = new RequestParams(StaticProperty.SERVERURL+"LoginServlet");
            rp.addParameter("user_email", params[0].getUser_email());
            rp.addParameter("user_password", params[0].getUser_pwd());
            rp.addParameter("token", MyUtils.myToken(params[0].getUser_pwd()));
            x.http().post(rp, new Callback.CommonCallback<String>() {
                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();

                    LoginResult res =gson.fromJson(result, LoginResult.class);
                    publishProgress(res.getStatus(), res.getToken());

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
            System.out.println(values[0]);

            switch (Integer.parseInt(values[0])) {
                case ResultCodes.LOGIN_AUCCESS:
                    Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getContext().getSharedPreferences("token", Context.MODE_PRIVATE).edit();
                    editor.putString("token", values[1]);
                    editor.putString("name", et_userEmail.getText().toString());
                    editor.putString("password", MyUtils.MD5(et_userPassword.getText().toString()));
                    editor.commit();

                    break;
                case ResultCodes.LOGIN_ERROR:
                    Toast.makeText(getContext(), "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }


}
