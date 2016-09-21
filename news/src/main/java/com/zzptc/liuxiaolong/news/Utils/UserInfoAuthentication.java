package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lxl97 on 2016/9/20.
 */
public class UserInfoAuthentication {


    public static boolean tokenExists(Context context){
        SharedPreferences sp = context.getSharedPreferences("token", context.MODE_PRIVATE);

        String token = sp.getString("token", "");
        return token !="" ? true : false;

    }

    public static String getTokeninfo(Context context, String field){
        SharedPreferences sp = context.getSharedPreferences("token", context.MODE_PRIVATE);
        return sp.getString(field, "");
    }
}
