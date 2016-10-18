package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzptc.liuxiaolong.news.MainActivity;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.adapter.SearchRecyclerViewAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.model.SearchNewsBean;
import com.zzptc.liuxiaolong.news.model.Search_Result;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.BaseActivity;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


@ContentView(R.layout.activity_search_news)
public class Activity_SearchNews extends BaseActivity implements GetNews.OnGetNewsListener{
    @ViewInject(R.id.searchNews_toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.search_recyclerview)
    private AutoLoadRecyclerView recyclerView;
    @ViewInject(R.id.et_searchInfo)
    private EditText editText;

    @ViewInject(R.id.tv_search)
    private TextView tv_search;
    @ViewInject(R.id.cp_load)
    private CircularProgressBar cp_load;
    private SearchRecyclerViewAdapter adapter;

    private ImageLoader imageLoader;
    private LoadFinshCallBack mloadfinshcallBack;


    private GetNews getNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        init();



    }


    public void init(){
        getNews = new GetNews(this);
        recyclerView.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyAnimator.closeActivityAnim(Activity_SearchNews.this);
            }
        });
        mloadfinshcallBack = recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });



    }

    @Event(R.id.tv_search)
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.tv_search:
                getNews.searchNews(editText.getText().toString());
                getNews.setOnGetNewsListener(this);
                cp_load.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
               break;


        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭activity动画
        MyAnimator.closeActivityAnim(Activity_SearchNews.this);

    }

    @Override
    public void OnStartGetNewsListener() {

    }

    @Override
    public void OnProcessGetNewsListener(int process, int total) {

    }

    @Override
    public void OnCompleteGetNewsListener(List<NewsData> list) {
        adapter = new SearchRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(adapter);
        cp_load.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setOnClickerListener(new SearchRecyclerViewAdapter.OnClickerListener() {
            @Override
            public void OnclickerListener(View v, int position, NewsData newsData) {
                Intent intent = new Intent(Activity_SearchNews.this, Activity_NewsDetail.class);
                intent.putExtra("newsData", newsData);
                startActivity(intent);
                MyAnimator.openActivityAnim(Activity_SearchNews.this);
            }
        });
    }
}
