package com.zzptc.liuxiaolong.news.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.zzptc.liuxiaolong.news.animator.MyAnimator;

import org.xutils.view.annotation.Event;

/**
 * Created by lxl97 on 2016/10/3.
 */

public class MyGestureDetector{
    private GestureDetector gestureDetector;
    private Activity activity;
    public MyGestureDetector(Activity activity){
        this.activity  = activity;

    }
    public void slide(){

        gestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX() >0 && (e1.getX() >=0 && e1.getX() <= 400)){
                    if (Math.abs(e2.getX() - e1.getX()) > Math.abs(e2.getY() - e1.getY())
                            && Math.abs(velocityX) > 1500){
                        MyAnimator.closeActivityAnim(activity);
                        activity.finish();
                        activity.onBackPressed();
                    }

                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }



    public void rightSlideClose(MotionEvent v){
        gestureDetector.onTouchEvent(v);

    }

}
