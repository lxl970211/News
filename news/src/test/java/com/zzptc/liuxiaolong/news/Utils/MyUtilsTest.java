package com.zzptc.liuxiaolong.news.Utils;

import android.os.Build;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by lxl97 on 2016/9/8.
 */
public class MyUtilsTest {
    private MyUtils myUtils;
    @Before
    public void setUp() throws Exception {
        myUtils = new MyUtils();
    }

    @Test
    public void timeTest(){
        String a = "2016-10-08 16:07:20";
        long time =  MyUtils.nowTimeLong() - MyUtils.timeStringToLong(a);
        String i = MyUtils.getCommentDistanceCurrentTime(time);
        System.out.println(i);
    }

}