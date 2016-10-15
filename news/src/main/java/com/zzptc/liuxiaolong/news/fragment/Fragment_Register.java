package com.zzptc.liuxiaolong.news.fragment;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.CodeUtils;
import com.zzptc.liuxiaolong.news.Utils.LoginRegister;
import com.zzptc.liuxiaolong.news.Utils.MD5;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.javabean.User;
import com.zzptc.liuxiaolong.news.javabean.ResultData;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_register)
public class Fragment_Register extends Fragment implements LoginRegister.OnLoginRegisterListener{

    @ViewInject(R.id.et_register_userName)
    private EditText reg_userName;
    @ViewInject(R.id.et_register_pwd)
    private EditText reg_pwd;
    @ViewInject(R.id.et_register_confirmPwd)
    private EditText reg_confirmPwd;
    @ViewInject(R.id.et_register_email)
    private EditText reg_email;

    @ViewInject(R.id.pwdprompt)
    private TextView pwdprompt;
    @ViewInject(R.id.confirmpwd_prompt)
    private TextView confirmpwd_prompt;
    @ViewInject(R.id.userNamePrompt)
    private TextView userNamePrompt;
    @ViewInject(R.id.emailprompt)
    private TextView emailprompt;

    @ViewInject(R.id.btn_register)
    private Button btn_register;

    @ViewInject(R.id.et_enterCodes)
    private EditText enterCodes;
    @ViewInject(R.id.img_Codes)
    private ImageView codes;

    //验证表单
    private boolean user_namecorrect = false;
    private boolean pwdcorrect = false;
    private boolean confirmpwdcorrect = false;
    private boolean emailcorrect = false;
    private boolean codescorrect = false;


    private CodeUtils codeUtils;

    private LoginRegister loginRegister;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);
        init();
        return v;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter){
            //进入时的fragment动画
            return AnimationUtils.loadAnimation(getContext(), R.anim.activity_in_anim);
        }else{
            //退出时动画

            return AnimationUtils.loadAnimation(getContext(), R.anim.exit_activity_anim);
        }
    }

    public void init(){
        reg_userName.setText(null);
        reg_pwd.setText(null);
        reg_confirmPwd.setText(null);
        reg_email.setText(null);

        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();

        codes.setImageBitmap(bitmap);
        //
        loginRegister = new LoginRegister();
        loginRegister.setOnLoginRegisterListener(this);
    }

    /**
     * editText 焦点监听
     * @param v
     * @param hansFocus
     */
    @Event(value = {R.id.et_register_confirmPwd, R.id.et_register_pwd, R.id.et_register_userName, R.id.et_register_email, R.id.et_enterCodes, R.id.btn_register}, type = View.OnFocusChangeListener.class)
    private void onFocus(View v, boolean hansFocus){
        //如果失去焦点
        if (!hansFocus){
            switch (v.getId()){
                //用户名
                case R.id.et_register_userName:
                        user_namecorrect = reg_userName.getText().toString() != null ? true : false;

                    break;
                //密码
                case R.id.et_register_pwd:

                    String tempText = !MyUtils.EditTextFormat(reg_pwd.getText().toString(), StaticProperty.PASSWORD_REGULAT_EXPRESSIONS) ? "必须以字母开头并包含数字" : null;
                    pwdprompt.setText(tempText);
                    pwdcorrect = MyUtils.EditTextFormat(reg_pwd.getText().toString(), StaticProperty.PASSWORD_REGULAT_EXPRESSIONS);
                    break;
                //确认密码
                case R.id.et_register_confirmPwd:

                    String text = !reg_confirmPwd.getText().toString().equals(reg_pwd.getText().toString()) ? "密码不一致" : null;
                    confirmpwd_prompt.setText(text);
                    confirmpwdcorrect = reg_confirmPwd.getText().toString().equals(reg_pwd.getText().toString())? true: false;
                    break;
                //邮箱
                case R.id.et_register_email:

                    if (reg_email.getText().toString() != null && !"".equals(reg_email.getText().toString())){
                        User user = new User();
                        user.setType("email");
                        user.setUserEmail(reg_email.getText().toString());
                        loginRegister.register(user);
                    }
                    break;


                case R.id.et_enterCodes:

                        if (!enterCodes.getText().toString().equalsIgnoreCase(codeUtils.getCode())) {
                            codescorrect = false;
                            Toast.makeText(x.app(), "验证码错误", Toast.LENGTH_SHORT).show();

                        } else {
                            codescorrect = true;
                        }

                    break;
            }

        }
    }

    @Event(value = {R.id.btn_register, R.id.img_Codes})
    private void getEvent(View v){
        switch (v.getId()) {
            case R.id.btn_register:
            //如果表单内容正确则提交注册
            if (verificationform()) {

                User user = new User();
                user.setType("register");
                user.setUserName(reg_userName.getText().toString());
                user.setUserEmail(reg_email.getText().toString());
                //MD5加密密码
                user.setUserPassword(MD5.parseStrToMd5L32(reg_pwd.getText().toString()));
                loginRegister.register(user);

            } else {
                Toast.makeText(getContext(), "cuowu", Toast.LENGTH_SHORT).show();

            }
                break;

            case R.id.img_Codes:
                codes.setImageBitmap(codeUtils.createBitmap());

                break;
        }

    }
    //注册结果
    @Override
    public void OnGetServerResponseCodesListener(int ResponseCodes, String token) {
        switch (ResponseCodes){
            case ResultCodes.EMAILL_EXIST:
                //邮箱已存在
                emailprompt.setText("邮箱已存在");
                emailcorrect = false;

                break;
            case ResultCodes.EMAILL_UNEXIST:
                //邮箱不存在，可以使用
                emailprompt.setText(null);
                emailcorrect = true;

                break;
            case ResultCodes.EMAIL_ERROR:
                emailprompt.setText("请输入正确的邮箱");
                emailcorrect = false;

                break;
            case ResultCodes.REGISTER_AUCCESS:
                Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();

                returnloginFragment();
                //注册成功
                break;

            case ResultCodes.REGISTER_FAILED:
                Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                //注册失败
                break;
        }

    }


    public void returnloginFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.login_content, new Fragment_Login()).commit();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.login_toolbar);
        toolbar.setTitle(R.string.login);
        TextView register = (TextView) getActivity().findViewById(R.id.tv_register);
        //显示注册按钮
        register.setVisibility(View.VISIBLE);



    }

    /**
     * 验证表单内容是否正确
     * @return
     */
    public boolean verificationform(){

        if (user_namecorrect && pwdcorrect && confirmpwdcorrect && codescorrect && emailcorrect){
            return true;
        }
            return false;
    }

}
