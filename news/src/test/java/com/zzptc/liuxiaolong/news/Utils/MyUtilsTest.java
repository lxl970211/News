package com.zzptc.liuxiaolong.news.Utils;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

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

    String pwd = "liuxiaolong0733";
    @Test
    public void testMD5() throws Exception {
        System.out.println(myUtils.MD5("lxl0211"));
    }

    @Test
    public void testKL() throws Exception {
        System.out.println(myUtils.MD5(myUtils.KL(pwd)));
    }

    @Test
    public void testJM() throws Exception {
        System.out.println(myUtils.JM(myUtils.JM(pwd)));
    }
}