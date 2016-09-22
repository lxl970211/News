package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.view.BaseActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.io.IOException;



@ContentView(R.layout.activity_news_detail)
public class Activity_NewsDetail extends BaseActivity {

    @ViewInject(R.id.webview)
    private WebView webView;

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


        initdata();
        init();

    }

    public void init(){

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
            //新闻url
            final String url = intent.getStringExtra("url");
            if ("search".equals(intent.getStringExtra("search")) | intent.getStringExtra("search") == "search"){

                webView.loadUrl(url);

            }else{

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Document document = null;
                    try {
                        //删除新闻页面中不必要的模块
                        document = Jsoup.connect(url).get();
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



    }



    //返回按钮监听
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyAnimator.closeActivityAnim(Activity_NewsDetail.this);
    }
}
