package com.zzptc.liuxiaolong.news.Utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.activity.Activity_Login;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
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



    /**
     * 下载工具类
     *
     * @param url
     * @param path
     */
    public static void DownLoad(String url, final String path, final Context context) {

        final NotificationManager mNotifyManager;
        final NotificationCompat.Builder mBuilder;
        mNotifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("版本更新")
                .setContentText("正在下载...")
                .setContentInfo("0%")
                .setSmallIcon(R.mipmap.ic_launcher);


        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(path);

        x.http().get(params, new Callback.ProgressCallback<File>() {


            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                Toast.makeText(x.app(), "开始下载", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                BigDecimal b = new BigDecimal((float) current / (float) total);
                float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                mBuilder.setProgress(100, (int) (f1 * 100), false);
                mBuilder.setContentInfo((int) (f1 * 100) + "%");
                mNotifyManager.notify(1, mBuilder.build());
            }

            @Override
            public void onSuccess(File result) {
                mBuilder.setContentText("正在下载...")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                mNotifyManager.notify(1, mBuilder.build());
                mNotifyManager.cancel(1);
                Toast.makeText(x.app(), "下载成功", Toast.LENGTH_LONG).show();
                installApp(context, path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public static void installApp(Context context,String filePath) {
        File _file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(_file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
