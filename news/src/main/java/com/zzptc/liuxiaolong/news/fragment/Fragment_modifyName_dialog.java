package com.zzptc.liuxiaolong.news.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.javabean.ResultData;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link DialogFragment} subclass.
 */
@ContentView(R.layout.fragment_modify_name)
public class Fragment_modifyName_dialog extends DialogFragment implements OnRequestResultListener{

    @ViewInject(R.id.et_modifyusername)
    private EditText et_modifyUserName;
    @ViewInject(R.id.btn_modifyname_submit)
    private Button btn_modifyname_submit;
    @ViewInject(R.id.btn_modifyname_cancel)
    private Button btn_modifyname_cancel;

    private OnRequestResultListener OnRequestResultListener;
    public void setOnRequestResultListener(OnRequestResultListener listener){
        this.OnRequestResultListener = listener;
    }


    private PushData pushdata;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        pushdata = new PushData(getContext());
        return v;
    }


    @Event(value = {R.id.btn_modifyname_cancel, R.id.btn_modifyname_submit})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.btn_modifyname_cancel:
                dismiss();
                break;

            case R.id.btn_modifyname_submit:
                if (!et_modifyUserName.getText().toString().equals("")){

                    pushdata.updateUserName(et_modifyUserName.getText().toString());
                    pushdata.setOnRequestResultListener(this);
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "请输入新的昵称", Toast.LENGTH_SHORT).show();
                }
                break;


        }

    }

    @Override
    public void OnGetRequestResultStatusListener(int status) {
        System.out.println(status);
        SharedPreferences.Editor editor = x.app().getSharedPreferences("token", Context.MODE_PRIVATE).edit();
        editor.putString("name", et_modifyUserName.getText().toString());
        editor.commit();
        if (OnRequestResultListener != null){
            OnRequestResultListener.OnGetRequestResultStatusListener(status);
        }

    }
}
