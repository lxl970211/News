package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.adapter.SearchRecyclerViewAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.model.Search_Result;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.BaseActivity;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_search_news)
public class Activity_SearchNews extends BaseActivity {
    @ViewInject(R.id.searchNews_toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.search_recyclerview)
    private AutoLoadRecyclerView recyclerView;
    @ViewInject(R.id.et_searchInfo)
    private EditText editText;

    @ViewInject(R.id.clear_edittext)
    private ImageView cleareditText;

    private SearchRecyclerViewAdapter adapter;

    private ImageLoader imageLoader;
    private LoadFinshCallBack mloadfinshcallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        init();
        initListener();


    }


    public void init(){
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

    @Event(R.id.clear_edittext)
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.clear_edittext:
                editText.setText(null);
                break;


        }

    }


    public void initListener(){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //editText数据不为空则搜索
                if (s.toString() != null | !s.toString().equals(null)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            GetNews getNews = new GetNews(Activity_SearchNews.this);
                            //得到新闻数据集合
                            final List<Search_Result.S_NewsData> list = getNews.getsearchNews(s.toString());
                            //将数据返回主线程更新适配器
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new SearchRecyclerViewAdapter(Activity_SearchNews.this, list);
                                    recyclerView.setAdapter(adapter);

                                    adapter.setOnClickerListener(new SearchRecyclerViewAdapter.OnClickerListener() {
                                        @Override
                                        public void OnclickerListener(View v, int position, String newsUrl) {
                                            Intent intent = new Intent(Activity_SearchNews.this, Activity_NewsDetail.class);
                                            intent.putExtra("search", "search");
                                            intent.putExtra("url", newsUrl);
                                            startActivityForResult(intent, StaticProperty.RETURN_NEWSLIST);
                                            MyAnimator.openActivityAnim(Activity_SearchNews.this);
                                        }
                                    });
                                }
                            });
                        }
                    }).start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭activity动画
        MyAnimator.closeActivityAnim(Activity_SearchNews.this);

    }

}
