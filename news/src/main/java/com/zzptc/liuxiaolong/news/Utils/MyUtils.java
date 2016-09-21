package com.zzptc.liuxiaolong.news.Utils;

import android.os.Build;


import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
    public static String myToken(String userpassword){
        return Build.MODEL+new MyUtils().getMac()+userpassword;
    }
}
