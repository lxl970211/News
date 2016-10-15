package com.zzptc.liuxiaolong.news.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.MainActivity;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.FileUtils;
import com.zzptc.liuxiaolong.news.Utils.MyAsyncTask;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.fragment.Fragment_modifyName_dialog;
import com.zzptc.liuxiaolong.news.view.BaseActivity;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_setting)
public class Activity_Setting extends BaseActivity implements MyAsyncTask.OnGetUserInfoListener, OnRequestResultListener{
    @ViewInject(R.id.setting_toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.clear_cache)
    private LinearLayout clear_cache;
    @ViewInject(R.id.tv_aboutapp)
    private TextView tv_aboutapp;
    @ViewInject(R.id.clear_cache_size)
    private TextView clearsize;
    @ViewInject(R.id.tv_feedback)
    private TextView feedback;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(R.id.tv_myemail)
    private TextView myemail;
    @ViewInject(R.id.btn_signUp)
    private Button btn_signup;
    private int resultcode = 0;

    private Handler handler;
    private MyAsyncTask myAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();


    }

    public void init(){
        if (UserInfoAuthentication.tokenExists(this)){
            btn_signup.setVisibility(View.VISIBLE);
            myemail.setText(UserInfoAuthentication.getTokeninfo(this, "email"));
            tv_userName.setText(UserInfoAuthentication.getTokeninfo(this, "name"));
        }else {
            btn_signup.setVisibility(View.GONE);
        }
        handler = new Handler();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



    /**
     * 点击监听
     * @param v
     */
    @Event({R.id.tv_aboutapp,  R.id.clear_cache,R.id.tv_feedback, R.id.iv_userhead, R.id.tv_userName, R.id.btn_signUp})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.tv_aboutapp:
                Intent intent1 = new Intent(this, MyActivity.class);
                intent1.setAction("aboutApp");
                startActivity(intent1);
                MyAnimator.openActivityAnim(this);
                break;
            case R.id.tv_feedback:
                Intent intent2 = new Intent(this, MyActivity.class);
                intent2.setAction("feedback");
                startActivity(intent2);
                MyAnimator.openActivityAnim(this);
                break;


            case R.id.clear_cache:
                handler.post(clearCache);
                break;

            case R.id.iv_userhead:
                if (UserInfoAuthentication.tokenExists(this)){
                }else{
                   MyUtils.login(this);
                }

                break;

            case R.id.tv_userName:
                if (!UserInfoAuthentication.tokenExists(this)){
                    //跳转到登录页面
                    MyUtils.login(this);
                }else{
                    //修改用户名
                    Fragment_modifyName_dialog fragment_modifyName_dialog = new Fragment_modifyName_dialog();
                    fragment_modifyName_dialog.show(getSupportFragmentManager(), null);
                    fragment_modifyName_dialog.setOnRequestResultListener(this);

                }

                break;

            case R.id.btn_signUp:
                SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                tv_userName.setText("点击登录");
                myemail.setText("");
                setResult(2);
                btn_signup.setVisibility(View.GONE);
                break;
        }

    }



    Runnable clearCache = new Runnable() {
        @Override
        public void run() {
            if (FileUtils.deleteCache()){
                clearsize.setText(FileUtils.getCacheSize());
                Toast.makeText(x.app(), "缓存有助于更好的节约数据流量\n如果可以请保留缓存", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(x.app(), "清理出错", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        clearsize.setText(FileUtils.getCacheSize());
    }

    @Override
    public void onBackPressed() {
        finish();
        MyAnimator.closeActivityAnim(this);
    }

    @Override
    public void OnGetUserInfoListener(String name, String email) {
        tv_userName.setText(name);
        myemail.setText(UserInfoAuthentication.getTokeninfo(this, "email"));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCodes.LOGIN_AUCCESS && resultCode == ResultCodes.LOGIN_AUCCESS){
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.getinfo();
            myAsyncTask.setOnGetUserInfoListener(this);
            btn_signup.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void OnGetRequestResultStatusListener(int status) {
        switch (status){
            case 1:
                tv_userName.setText(UserInfoAuthentication.getTokeninfo(this, "name"));
                break;
        }
    }
}
