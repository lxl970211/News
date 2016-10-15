package com.zzptc.liuxiaolong.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by lxl97 on 2016/10/15.
 */
@ContentView(R.layout.fragment_feedback)
public class Fragment_feedback extends Fragment implements OnRequestResultListener{
    //反馈信息输入框
    @ViewInject(R.id.et_feedbackInfo)
    private EditText feedback_info;
    //联系方式输入框
    @ViewInject(R.id.et_contact_infomation)
    private EditText contact_infomation;
    //提交按钮
    @ViewInject(R.id.btn_submit)
    private Button btn_submit;

    private PushData pushdata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);
        initListener();
        return v;
    }
    public void initListener() {
        pushdata = new PushData(getContext());
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
                    btn_submit.setBackgroundResource(R.drawable.commentbtn_selector_shape);
                    btn_submit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btn_submit.setEnabled(false);
                    btn_submit.setBackgroundResource(R.drawable.submit_shape);
                }
            }
        });

    }



    @Event(R.id.btn_submit)
    private void getEvent(View v) {

        switch (v.getId()) {
            case R.id.btn_submit:
                if (!feedback_info.getText().toString().equals("")){
                    pushdata.feedback(feedback_info.getText().toString(), contact_infomation.getText().toString());
                    pushdata.setOnRequestResultListener(this);
                    getActivity().finish();
                    MyAnimator.closeActivityAnim(getActivity());
                }
                break;
        }
    }


    @Override
    public void OnGetRequestResultStatusListener(int status) {
        switch (status){
            case 1:
                Toast.makeText(getContext(), "感谢您的反馈", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
