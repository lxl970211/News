package com.zzptc.liuxiaolong.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzptc.liuxiaolong.news.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by lxl97 on 2016/10/15.
 */
@ContentView(R.layout.fragment_about_app)
public class Fragment_aboutapp extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);



        return v;
    }
}
