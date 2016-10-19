package com.zzptc.liuxiaolong.news.fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.DownloadListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.download.DownloadInfo;
import com.zzptc.liuxiaolong.news.download.DownloadManager;
import com.zzptc.liuxiaolong.news.download.DownloadState;
import com.zzptc.liuxiaolong.news.javabean.Update;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * A simple {@link DialogFragment} subclass.
 */
@ContentView(R.layout.fragment__check_update)
public class Fragment_CheckUpdate extends DialogFragment implements PushData.OnPushInfoListener{

    @ViewInject(R.id.update_load)
    private CircularProgressBar update_load;
    @ViewInject(R.id.btn_cacelUpdate)
    private Button btn_cacel;
    @ViewInject(R.id.btn_update)
    private Button btn_update;
    @ViewInject(R.id.tv_updateInfo)
    private TextView tv_updateInfo;
    @ViewInject(R.id.rl_load)
    private RelativeLayout rl_load;
    @ViewInject(R.id.rl_updateInfo)
    private RelativeLayout rl_updateInfo;

    private Handler handler;
    private PushData pushData;
    private Update up;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        return v;
    }


    public void init(){
        handler = new Handler();
        pushData = new PushData(getContext());
        pushData.setOnPushInfoListener(this);

        handler.postDelayed(checkUpdate, 2000);
    }


    Runnable checkUpdate = new Runnable() {
        @Override
        public void run() {
            pushData.checkUpdate();

        }
    };

    @Override
    public void OnGetRequestDataListener(String json) {
        up = new Gson().fromJson(json, Update.class);
        PackageManager packageManager = getContext().getPackageManager();
        try {
            PackageInfo pi = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            String version = pi.versionName;
            if (Double.parseDouble(up.getVersion()) > Double.parseDouble(version)){
                tv_updateInfo.setText("检查到新版本是否更新?");
                rl_load.setVisibility(View.GONE);
                rl_updateInfo.setVisibility(View.VISIBLE);

            }else{
                tv_updateInfo.setText("已是最新版本");
                btn_cacel.setVisibility(View.GONE);
                rl_load.setVisibility(View.GONE);
                rl_updateInfo.setVisibility(View.VISIBLE);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.btn_cacelUpdate, R.id.btn_update})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.btn_cacelUpdate:
                dismiss();
                break;
            case R.id.btn_update:
                if (tv_updateInfo.getText().toString().equals("已是最新版本")){
                    dismiss();
                }else{

                    String path = Environment.getExternalStorageDirectory()+"/News";
                    MyUtils.DownLoad(StaticProperty.DOWNLOAD_URL+up.getPath(),path, getActivity());
                    dismiss();

                }
                break;
        }


    }



}
