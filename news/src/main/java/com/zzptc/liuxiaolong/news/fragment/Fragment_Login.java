package com.zzptc.liuxiaolong.news.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.LoginRegister;
import com.zzptc.liuxiaolong.news.Utils.MD5;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.javabean.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_login)
public class Fragment_Login extends Fragment implements LoginRegister.OnLoginRegisterListener{

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

    private LoginRegister loginRegister;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        init();


        return view;
    }


    public void init(){
        loginRegister = new LoginRegister();
        loginRegister.setOnLoginRegisterListener(this);
        //修改icon颜色
        ivEmail.setColorFilter(getResources().getColor(R.color.blue));
        ivPwd.setColorFilter(getResources().getColor(R.color.blue));

    }

    @Event(R.id.btn_login)
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.btn_login:
                User user = new User();
                user.setType("login");
                user.setUserEmail(et_userEmail.getText().toString());
                String pwd = MD5.MD5(et_userPassword.getText().toString());
                user.setUserPassword(pwd);
                loginRegister.login(user);
                break;

        }

    }

    @Override
    public void OnGetServerResponseCodesListener(int ResponseCodes, String token) {
        switch (ResponseCodes){
            case ResultCodes.LOGIN_AUCCESS:
                Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                //将登录信息保存到SharedPreferences
                SharedPreferences.Editor editor = getContext().getSharedPreferences("token", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.putString("token", token);
                editor.putString("email", et_userEmail.getText().toString());
                editor.putString("password", MD5.MD5(et_userPassword.getText().toString()));
                editor.commit();
                //设置返回结果码
                getActivity().setResult(ResultCodes.LOGIN_AUCCESS);
                //登录成功关闭登录界面
                getActivity().finish();
                MyAnimator.closeActivityAnim(getActivity());
                break;
            case ResultCodes.LOGIN_ERROR:
                Toast.makeText(getContext(), "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();

                break;
        }

    }

}
