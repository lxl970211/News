package com.zzptc.liuxiaolong.news;

import com.zzptc.liuxiaolong.news.datapars.GetNews;

import org.junit.Test;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void time(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        System.out.println(sdf.format(new Date()));
    }


}