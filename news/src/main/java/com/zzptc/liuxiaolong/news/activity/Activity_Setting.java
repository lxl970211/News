package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.FileUtils;
import com.zzptc.liuxiaolong.news.Utils.MyAsyncTask;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.view.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_setting)
public class Activity_Setting extends BaseActivity implements MyAsyncTask.OnGetUserInfoListener{
    @ViewInject(R.id.setting_toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.user_setting)
    private LinearLayout user_setting;

    @ViewInject(R.id.clear_cache)
    private LinearLayout clear_cache;
    @ViewInject(R.id.about_app)
    private LinearLayout about_app;
    @ViewInject(R.id.clear_cache_size)
    private TextView clearsize;
    @ViewInject(R.id.Feedback)
    private LinearLayout feedback;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    private Handler handler;
    private MyAsyncTask myAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();


    }

    public void init(){
        myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.setOnGetUserInfoListener(this);
        myAsyncTask.getinfo();
        //半透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        handler = new Handler();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyAnimator.closeActivityAnim(Activity_Setting.this);
            }
        });
    }



    /**
     * 点击监听
     * @param v
     */
    @Event({R.id.about_app, R.id.user_setting, R.id.clear_cache,R.id.Feedback})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.about_app:
                Intent openAbout_app = new Intent(x.app(), Activity_Feedback.class);
                openAbout_app.setAction("aboutapp");
                startActivity(openAbout_app);
                MyAnimator.openActivityAnim(Activity_Setting.this);

                break;
            case R.id.Feedback:
                Intent feedback = new Intent(x.app(), Activity_Feedback.class);
                feedback.setAction("feedback");
                startActivity(feedback);
                MyAnimator.openActivityAnim(Activity_Setting.this);
                break;


            case R.id.clear_cache:
                handler.post(clearCache);
                break;


            case R.id.user_setting:
                Toast.makeText(x.app(), "用户设置", Toast.LENGTH_SHORT).show();
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
    public void OnGetUserInfoListener(String name) {
        tv_userName.setText(name);
    }
}
