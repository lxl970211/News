package com.zzptc.liuxiaolong.news.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.fragment.Fragment_Login;
import com.zzptc.liuxiaolong.news.fragment.Fragment_Register;
import com.zzptc.liuxiaolong.news.view.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class Activity_Login extends BaseActivity{

    @ViewInject(R.id.login_toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.tv_register)
    private TextView register;
    Fragment_Register fragmentRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initFragment();
        init();
    }
    public void init(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }



    public void initFragment(){

        getSupportFragmentManager().beginTransaction().replace(R.id.login_content, new Fragment_Login(), "login").commit();
        //设置toolbar title
        toolbar.setTitle(R.string.login);
        //显示注册按钮
        register.setVisibility(View.VISIBLE);
    }
    /**
     * 点击监听
     * @param v
     */
    @Event(R.id.tv_register)
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.tv_register:
                if (fragmentRegister == null){
                    fragmentRegister = new Fragment_Register();
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.login_content, fragmentRegister,"register").commit();
                //隐藏注册按钮
                register.setVisibility(View.GONE);
                //设置toolbar title
                toolbar.setTitle(R.string.register);

                break;
        }
    }
    //返回键监听
    @Override
    public void onBackPressed() {
        if (null != getSupportFragmentManager().findFragmentByTag("register")){

            initFragment();
            //退出注册页面时将EditText数据清空
            resetRegisterEditText();
        }else {
            finish();
            MyAnimator.closeActivityAnim(this);
            super.onBackPressed();
        }
    }

    public void resetRegisterEditText(){
        int [] edittext = new int[]{R.id.et_register_userName, R.id.et_register_pwd, R.id.et_register_email, R.id.et_register_confirmPwd, R.id.et_enterCodes};
        for (int i = 0; i<edittext.length; i++){
            EditText et = (EditText) findViewById(edittext[i]);
            et.setText(null);
        }


    }
}
