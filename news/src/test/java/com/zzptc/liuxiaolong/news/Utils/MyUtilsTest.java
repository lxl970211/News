package com.zzptc.liuxiaolong.news.Utils;

import android.os.Build;
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

    @Test
    public void tokenTest(){
//        System.out.println(myUtils.getMac());nulld3d221e33b16134a43bc1e4823a88320
        System.out.println(myUtils.MD5("刘小龙"));
    }

}