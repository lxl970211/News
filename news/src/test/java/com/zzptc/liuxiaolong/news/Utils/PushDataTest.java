package com.zzptc.liuxiaolong.news.Utils;

import com.zzptc.liuxiaolong.news.model.NewsData;

import org.junit.Before;
import org.junit.Test;
import org.xutils.x;

import static org.junit.Assert.*;

/**
 * Created by lxl97 on 2016/10/1.
 */
public class PushDataTest {
    PushData pushData;
    @Before
    public void setUp() throws Exception {
        pushData = new PushData(x.app());
    }


}