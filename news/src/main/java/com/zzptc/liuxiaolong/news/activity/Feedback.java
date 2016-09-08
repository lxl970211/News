package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by lxl97 on 2016/8/26.
 */
@ContentView(R.layout.feedback)
public class Feedback extends AppCompatActivity {
    //反馈信息输入框
    @ViewInject(R.id.et_feedbackInfo)
    private EditText feedback_info;
    //联系方式输入框
    @ViewInject(R.id.et_contact_infomation)
    private EditText contact_infomation;
    //提交按钮
    @ViewInject(R.id.btn_submit)
    private Button btn_submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.getAction().equals("feedback")) {
            x.view().inject(this);
            init();
            initListener();
        } else {
            setContentView(R.layout.activity_about_app);
        }

    }


    public void init() {
        //半透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    @Event(R.id.btn_submit)
    private void getEvent(View v) {

        switch (v.getId()) {
            case R.id.btn_submit:

                break;
        }
    }

    public void initListener() {

        //意见反馈监听
        feedback_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!"".equals(s.toString())) {
                    btn_submit.setEnabled(true);
                } else {
                    btn_submit.setEnabled(false);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyAnimator.closeActivityAnim(this);
    }
}
