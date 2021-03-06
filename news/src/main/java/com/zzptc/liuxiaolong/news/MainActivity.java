package com.zzptc.liuxiaolong.news;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zzptc.liuxiaolong.news.Utils.MyAsyncTask;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.activity.Activity_Login;
import com.zzptc.liuxiaolong.news.activity.Activity_SearchNews;
import com.zzptc.liuxiaolong.news.activity.Activity_Setting;
import com.zzptc.liuxiaolong.news.activity.MyActivity;
import com.zzptc.liuxiaolong.news.adapter.MyFragmentPagerAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.fragment.MyFragment;
import com.zzptc.liuxiaolong.news.javabean.User;
import com.zzptc.liuxiaolong.news.service.MyService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AutoLayoutActivity implements View.OnClickListener, MyAsyncTask.OnGetUserInfoListener{

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
    private CircleImageView user_head;
    private TextView user_login;

    
    String[] titles = {"头条","娱乐","国内","社会","体育","军事","财经","科技","国际","时尚"};
    public static final String[] pinyintitles = {"top", "yule", "guonei", "shehui", "tiyu", "junshi", "caijing", "keji", "guoji", "shishang"};

    private Handler handler;
    private MyAsyncTask myAsyncTask;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

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


    }




    public void init(){
        myAsyncTask = new MyAsyncTask(this);
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

        user_login = (TextView) headerLayout.findViewById(R.id.user_login);
        user_head = (CircleImageView) headerLayout.findViewById(R.id.user_head);


        user_head.setOnClickListener(this);
        user_login.setOnClickListener(this);
        search_news.setOnClickListener(this);



    }

    public void initListener(){


        //左侧菜单条目监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.my_comment:
                        if (UserInfoAuthentication.tokenExists(MainActivity.this)) {
                            //跳转到我的评论页面
                            Intent intent = new Intent(MainActivity.this, MyActivity.class);
                            intent.setAction("my_comment");
                            startActivity(intent);
                            //跳转动画
                            MyAnimator.openActivityAnim(MainActivity.this);
                        }else{
                            MyUtils.login(MainActivity.this);
                        }
                        break;

                    case R.id.my_collect:
                        if (UserInfoAuthentication.tokenExists(MainActivity.this)) {
                            //跳转到我的收藏页面
                            Intent intent1 = new Intent(MainActivity.this, MyActivity.class);
                            intent1.setAction("my_collect");
                            startActivity(intent1);
                            //跳转动画
                            MyAnimator.openActivityAnim(MainActivity.this);

                        }else{
                            MyUtils.login(MainActivity.this);
                        }
                        break;

                    case R.id.setting:
                        //跳转到设置页面
                        startActivityForResult(new Intent(MainActivity.this, Activity_Setting.class),2);
                        MyAnimator.openActivityAnim(MainActivity.this);


                        break;
                }


                return false;
            }
        });




    }

    /**
     * 初始化数据
     */
    private boolean misscrolled = false;
    private void initData(){

        List<Fragment> fragmentList = new ArrayList<>();

        for (int i = 0; i<titles.length; i++) {
            fragmentList.add(MyFragment.newInstance(titles[i], pinyintitles[i]));
        }
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles, this);
        viewPager.setAdapter(adapter);

        //绑定viewpager
        tablayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (viewPager.getCurrentItem() == 0 && !misscrolled){
                            drawer.openDrawer(Gravity.LEFT);
                        }
                        misscrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        misscrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        misscrolled = true;
                        break;
                }
            }
        });


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
    public void onBackPressed() {
        if (navigationView.isShown()){
            drawer.closeDrawers();
        }else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索按钮监听
            case R.id.search_news:
                startActivity(new Intent(this, Activity_SearchNews.class));
                MyAnimator.openActivityAnim(MainActivity.this);
                break;
            //登录按钮监听
            case R.id.user_login:
                startActivityForResult(new Intent(this, Activity_Login.class), 1);
                MyAnimator.openActivityAnim(MainActivity.this);
                break;
            //用户头像监听
            case R.id.user_head:
                //token不存在则打开登录页面 存在则打开设置页面
                if(!UserInfoAuthentication.tokenExists(this)){
                    //打开登录页面
                    startActivity(new Intent(this, Activity_Login.class));
                    MyAnimator.openActivityAnim(MainActivity.this);
                    break;
                }else{
                    //打开设置页面
                    startActivityForResult(new Intent(this, Activity_Setting.class), 2);
                    MyAnimator.openActivityAnim(MainActivity.this);
                    break;
                }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == ResultCodes.LOGIN_AUCCESS){

            myAsyncTask.getUserInfo();
            myAsyncTask.setOnGetUserInfoListener(this);
        }else if (requestCode == 2 && resultCode == 2){
            user_login.setText("登录");
            user_head.setImageResource(R.mipmap.man);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (UserInfoAuthentication.tokenExists(this)) {
            myAsyncTask.getUserInfo();
            myAsyncTask.setOnGetUserInfoListener(this);
        }
    }



    @Override
    public void OnGetUserInfoListener(User user) {
        System.out.println(user.getHeadPath());

        user_login.setText(user.getUserName());
        ImageLoader.getInstance().displayImage(user.getHeadPath(),user_head, MyApplication.headOptions);
    }
}
