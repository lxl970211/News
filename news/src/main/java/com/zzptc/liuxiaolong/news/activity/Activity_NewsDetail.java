package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.NetWorkStatus;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.service.MyService;
import com.zzptc.liuxiaolong.news.view.BaseActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.io.IOException;



@ContentView(R.layout.activity_news_detail)
public class Activity_NewsDetail extends BaseActivity implements PushData.OnPushInfoListener{

    @ViewInject(R.id.webview)
    private WebView webView;
    @ViewInject(R.id.writeReview)
    private RelativeLayout writeReview;
    @ViewInject(R.id.iv_like)
    private ImageView iv_like;
    @ViewInject(R.id.iv_share)
    private ImageView iv_share;
    @ViewInject(R.id.tv_sumReview)
    private TextView tv_sumReview;

    private boolean isLike = false;
    private GestureDetector gestureDetector;

    private NewsData newsData;
    PushData pushData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


        init();
        initdata();

    }

    public void init(){
        pushData = new PushData(this);



        //右滑关闭
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX() >0 && (e1.getX() >=0 && e1.getX() <= 400)){
                    if (Math.abs(e2.getX() - e1.getX()) > Math.abs(e2.getY() - e1.getY())
                            && Math.abs(velocityX) > 1500){
                        MyAnimator.closeActivityAnim(Activity_NewsDetail.this);
                        finish();
                        onBackPressed();
                    }

                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });


        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });


    }


    public void initdata(){

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //返回值
        setResult(StaticProperty.RETURN_NEWSLIST);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        Intent intent = getIntent();

        if (intent != null){
            newsData = (NewsData) intent.getSerializableExtra("newsData") ;
            //新闻url

            if ("search".equals(intent.getStringExtra("search"))){

                webView.loadUrl(newsData.getUrl());

            }else{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document document = null;
                    try {
                        //删除新闻页面中不必要的模块
                        document = Jsoup.connect(newsData.getUrl()).get();
                        document.select("div.pswp").remove();


//                        Element element = document.getElementById("DFTOUTIAOCS");
//                        element.remove();

                        Element e1 = document.getElementById("news_check");
                        e1.remove();

                        final String url = document.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
                }
          }

            if (NetWorkStatus.getNetWorkType(this) != 0 && UserInfoAuthentication.tokenExists(this)){
                pushData.pushCollectNews(newsData, "checkIsCollect");
                pushData.setOnPushInfoListener(this);
            }


    }

    @Event(value = {R.id.writeReview, R.id.iv_like, R.id.iv_share})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.writeReview: //写评论
                break;

            case R.id.iv_like: //收藏新闻
                //网络已连接并且用户已登录
                if (NetWorkStatus.getNetWorkType(this) != 0) {
                    System.out.println("network");
                    if (UserInfoAuthentication.tokenExists(this)) {
                        System.out.println("true");
                        if (!isLike) {
                            pushData.pushCollectNews(newsData, "collectNews");
                        }else{
                            pushData.pushCollectNews(newsData, "deleteCollect");
                        }

                    }else{
                        //如果没有登录则跳转到登录页面
                        startActivity(new Intent(this, Activity_Login.class));
                        MyAnimator.openActivityAnim(this);
                    }
                }else{
                    Toast.makeText(this, "网络好像开小差了，请稍后再试", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_share: //分享新闻

                break;
        }


    }

    //返回按钮监听
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyAnimator.closeActivityAnim(Activity_NewsDetail.this);
    }

    @Override
    public void OnPushCollectNewsListener(int status) {
        System.out.println(status);
        switch (status){
            case 1:
                iv_like.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
                isLike = true;
                break;

            case 0:
                iv_like.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                isLike = false;
                break;
        }
    }
}
