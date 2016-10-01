package com.zzptc.liuxiaolong.news.animator;

import android.app.Activity;
import android.content.Context;
import com.zzptc.liuxiaolong.news.R;


/**
 * Created by lxl97 on 2016/7/23.
 */
public class MyAnimator {
    private Context context;
    public MyAnimator( Context context){
        this.context = context;
    }





    /**
     * 打开activity动画
     * @param activity
     */
    public static void openActivityAnim(Activity activity){
        activity.overridePendingTransition(R.anim.activity_in_anim, R.anim.activity_out_anim);

    }

    /**
     * 关闭activity动画
     * @param activity
     */
    public static void closeActivityAnim(Activity activity){
        activity.overridePendingTransition(R.anim.activity_out_anim, R.anim.exit_activity_anim);

    }

}
