package com.zzptc.liuxiaolong.news.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxl97 on 2016/7/21.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list = new ArrayList<>();
    private final String[] titles;
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] title, Context context) {
        super(fm);
        this.list = fragments;
        this.titles = title;
        this.context = context;
    }



    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
