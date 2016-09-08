package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.model.NewsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@ContentView(R.layout.activity_news_detail)
public class NewsDetail extends AppCompatActivity {

    @ViewInject(R.id.webview)
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initdata();

    }


    public void initdata(){
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


                        Element element = document.getElementById("DFTOUTIAOCS");
                        element.remove();

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
        MyAnimator.closeActivityAnim(NewsDetail.this);
    }
}
