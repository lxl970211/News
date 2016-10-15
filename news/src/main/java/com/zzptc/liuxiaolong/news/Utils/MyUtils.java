package com.zzptc.liuxiaolong.news.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;


import com.zzptc.liuxiaolong.news.activity.Activity_Login;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lxl97 on 2016/8/1.
 */
public class MyUtils {
    public static final String KEY_MD5 = "MD5";

    /*
    验证输入的表单内容是否合格
     */
    public static boolean EditTextFormat(String s, String RegularExpressions){
        Pattern p = Pattern.compile(RegularExpressions);
        Matcher m = p.matcher(s);

        return m.matches();
    }



    /*
     *获取手机mac地址
     */
    public String getMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);


            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }


    /*
    生成token
     */
    public static String myToken(String userpassword, String email){
        return Build.MODEL+new MyUtils().getMac()+userpassword+email;
    }

    /*
    获取当前时间 String类型
     */
    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    //评论时间String 转为long类型

    public static long timeStringToLong(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return cal.getTimeInMillis();
    }

    /*
    当前时间long类型
     */
    public static long nowTimeLong(){
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();

    }


    public static String getCommentDistanceCurrentTime(long time){
        if (time < (60*1000)){
            return time/1000+"秒";
        }else if (time < (3600*1000)){
            return time/ (60 * 1000)+"分钟";
        }else if(time < (3600*24*1000) ){
            return time / (3600*1000)+"小时";
        }else{
            return time/(3600*24*1000)+"天";
        }
    }


    public static void login(Activity activity){
        Intent intent = new Intent(activity, Activity_Login.class);
        activity.startActivityForResult(intent, ResultCodes.LOGIN_AUCCESS);
        MyAnimator.openActivityAnim(activity);
    }
}
