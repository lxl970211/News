package com.zzptc.liuxiaolong.news.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;

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
                System.out.println("登录");

                break;

        }

    }

}
