package com.zzptc.liuxiaolong.news;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.xutils.x;

/**
 * Created by jack on 2016/7/14.
 */
public class MyApplication extends Application {
    private static Context mContext;
    public static DisplayImageOptions mOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoader(this);
        x.Ext.init(this);
        AutoLayoutConifg.getInstance().useDeviceSize();
    }


    public static Context getContext() {
        return mContext;
    }

    /**
     * ImageLoader
     */
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(1)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(30)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(200 * 1024 * 1024) // 200 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);


        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.edit_mode_image)
                .showImageForEmptyUri(R.mipmap.edit_mode_image)
                .showImageOnFail(R.mipmap.edit_mode_image)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(200))
                .build();
    }




}
