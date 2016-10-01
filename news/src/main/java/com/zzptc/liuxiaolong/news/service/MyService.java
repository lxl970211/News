package com.zzptc.liuxiaolong.news.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zzptc.liuxiaolong.news.Utils.FileUtils;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.model.NewsData;

import org.xutils.x;

/**
 * Created by lxl97 on 2016/8/5.
 */
public class MyService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String action = intent.getAction();
        if (!intent.getAction().equals("") || intent.getAction() != null){

//            if (!action.equals("") || action != null){
            switch (intent.getAction()){
                case StaticProperty.SAVENEWS_TOSDCARD_CACHE:
                    Bundle bundle = intent.getExtras();
                    //保存新闻数据到sd卡缓存
                    FileUtils.saveNewsToSDCardCache(bundle.getString("fileName"), bundle.getString("newsJson"), this);
                    //关闭服务
                    stopSelf();
                    break;
                case StaticProperty.DELETE_TIMEOUT_CACHE:
                    //删除过期无效的缓存文件
                    FileUtils.deleteCacheImg();
                    stopSelf();
                    break;
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}



