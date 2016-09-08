package com.zzptc.liuxiaolong.news.activity;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.fragment.FragmentLogin;
import com.zzptc.liuxiaolong.news.fragment.FragmentRegister;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class Login extends AppCompatActivity {

    @ViewInject(R.id.login_toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.tv_register)
    private TextView register;
    FragmentRegister fragmentRegister;
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

        getSupportFragmentManager().beginTransaction().replace(R.id.login_content, new FragmentLogin(), "login").commit();
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
                    fragmentRegister = new FragmentRegister();
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
