package com.zzptc.liuxiaolong.news;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.Utils.LinkService;
import com.zzptc.liuxiaolong.news.activity.Login;
import com.zzptc.liuxiaolong.news.activity.SearchNews;
import com.zzptc.liuxiaolong.news.activity.Setting;
import com.zzptc.liuxiaolong.news.adapter.MyFragmentPagerAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.fragment.FragmentLogin;
import com.zzptc.liuxiaolong.news.fragment.MyFragment;
import com.zzptc.liuxiaolong.news.service.MyService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    @ViewInject(R.id.tablelayout)

    private TabLayout tablayout;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;
    @ViewInject(R.id.navigationview)
    private NavigationView navigationView;
    
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.appbar)
    private AppBarLayout appbar;

    @ViewInject(R.id.search_news)
    private ImageView search_news;

    //navigationView headerview
    private ImageView user_head;
    private TextView user_name;

    String[] titles = {"头条","娱乐","国内","社会","体育","军事","财经","科技","国际","时尚"};
    public static final String[] pinyintitles = {"top", "yule", "guonei", "shehui", "tiyu", "junshi", "caijing", "keji", "guoji", "shishang"};

    private Handler handler;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
            //如果系统版本为6.0则向用户取要读取内存卡权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
            }


        init();

        initListener();
        initData();

        SharedPreferences sp = getSharedPreferences("token", MODE_PRIVATE);
        String name = sp.getString("token", "");
        System.out.println(name);

    }

    public void init(){
        handler = new Handler();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //点击toolbar菜单键打开左侧菜单

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //找到navigationView头布局控件
        View headerLayout = navigationView.getHeaderView(0);

        user_name = (TextView) headerLayout.findViewById(R.id.user_name);
        user_head = (ImageView) headerLayout.findViewById(R.id.user_head);




    }

    public void initListener(){

        user_name.setOnClickListener(this);
        search_news.setOnClickListener(this);
        //左侧菜单条目监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.my_reviews:
                        drawer.closeDrawers();
                        break;

                    case R.id.my_collect:
                        //关闭侧边菜单
                        drawer.closeDrawers();
                        break;

                    case R.id.setting:

                        startActivity(new Intent(MainActivity.this, Setting.class));
                        overridePendingTransition(R.anim.activity_in_anim, R.anim.activity_out_anim);


                        break;
                }


                return false;
            }
        });




    }

    /**
     * 初始化数据
     */
    private void initData(){

        List<Fragment> fragmentList = new ArrayList<>();

        for (int i = 0; i<titles.length; i++) {
            fragmentList.add(MyFragment.newInstance(titles[i], pinyintitles[i]));
        }
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles, this);
        viewPager.setAdapter(adapter);
        //绑定viewpager
        tablayout.setupWithViewPager(viewPager);


    }




    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {


        //发出清除图片缓存的意图
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(StaticProperty.DELETE_TIMEOUT_CACHE);
        startService(intent);
        //软件关闭时关闭系统服务
        stopService(new Intent(this, MyService.class));

        super.onDestroy();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_news:
                startActivity(new Intent(this, SearchNews.class));
                MyAnimator.openActivityAnim(MainActivity.this);
                break;
            case R.id.user_name:
                startActivity(new Intent(this, Login.class));
                MyAnimator.openActivityAnim(MainActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (navigationView.isShown()){
            drawer.closeDrawers();
        }else {
            super.onBackPressed();
        }
    }
}
