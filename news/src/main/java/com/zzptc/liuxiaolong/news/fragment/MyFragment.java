package com.zzptc.liuxiaolong.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzptc.liuxiaolong.news.MainActivity;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.activity.NewsDetail;
import com.zzptc.liuxiaolong.news.adapter.RecylerViewAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.service.MyService;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.myfragment)
public class MyFragment extends Fragment implements GetNews.OnGetNewsListener{
    //下拉刷新控件
    @ViewInject(R.id.swiperefreshlayout)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.recyView)
    private AutoLoadRecyclerView recyclerView;



    RecylerViewAdapter adapter;

    List<NewsData> listdata = new ArrayList<>();
    private Handler handler;
    //刷新状态
    private boolean isRefresh = false;

    private GetNews getNews;
    private String newsType;

    private ImageLoader imageLoader;
    private LoadFinshCallBack mloadfinshcallBack;

    private int newsItemPosition;
    private LinearLayoutManager manager;
    public static MyFragment newInstance(String title, String pinyin){
        MyFragment fragment = new MyFragment();

        Bundle bundle = new Bundle();
        bundle.putString("tabName", title);
        bundle.putString("pinyin", pinyin);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this,inflater,container);

        handler = new Handler();

        getNews = new GetNews(getContext());

        getNews.setOnGetNewsListener(this);
        manager = new LinearLayoutManager(getContext());
        //新闻类型
        newsType = getArguments().getString("pinyin");
        getNews.getNews(newsType);

        return v;
    }



    @Override
    public void onStart() {
        super.onStart();
        initData();
        initSwipeRefresh();


    }


    //开始获取新闻监听
    @Override
    public void OnStartGetNewsListener() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void OnProcessGetNewsListener(int process, int total) {
    }
    //获取完成
    @Override
    public void OnCompleteGetNewsListener(List<NewsData> list) {
        //清除list中的数据
        listdata.clear();
        //将新闻的数据都添加都listdata集合中
        listdata.addAll(list);
        //使用handler更新数据
        handler.post(updateNewsData);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setOnPauseListenerParams(ImageLoader.getInstance(), false, true);

        adapter = new RecylerViewAdapter(listdata, getActivity());
        recyclerView.setAdapter(adapter);
    }


    /**
     * 初始化数据
     */
    public void initData(){


        mloadfinshcallBack = recyclerView;
        //设置适配器数据
        adapter = new RecylerViewAdapter(listdata, getContext());
        //如果item高度相同可提高速度
        recyclerView.setHasFixedSize(true);
        //默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //布局管理器
        recyclerView.setLayoutManager(manager);
        //设置recylerview适配器
        recyclerView.setAdapter(adapter);

        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
            }
        });


        //条目点击监听
        adapter.setOnItemClickListener(new RecylerViewAdapter.OnRecylerAdapterListener() {

            @Override
            public void OnItemClickListener(View itemView, int position, String newsUrl) {
                newsItemPosition = position;
                //跳转到新闻页面
                Intent intent = new Intent(getContext(), NewsDetail.class);
                intent.putExtra("url", newsUrl);
                startActivityForResult(intent, StaticProperty.RETURN_NEWSLIST);
                MyAnimator.openActivityAnim(getActivity());

            }
        });





    }


    //更新适配器数据
    Runnable updateNewsData = new Runnable() {
        @Override
        public void run() {

            adapter.notifyDataSetChanged();

        }
    };


    /**
     * 下拉刷新监听及设置
     */
    public void initSwipeRefresh(){
        //刷新按钮颜色 最多四种颜色
        swipeRefreshLayout.setColorSchemeResources(
                R.color.google_color_blue,
                R.color.google_color_red,
                R.color.google_color_yellow,
                R.color.google_color_green);


        //下拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefresh){
                    isRefresh = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            URL url = null;
                            //从网络获取新闻
                            try {
                                url = new URL("http://v.juhe.cn/toutiao/index?type=" + newsType + "&key=" + StaticProperty.APPKEYS);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            String json = getNews.getNewsJsonData(url);
                            System.out.println(json);
                            final List<NewsData> list = getNews.parsJsonToData(json);
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    //清除当前新闻数据
                                    listdata.clear();
                                    //添加最新从网络获取的数据
                                    listdata.addAll(list);
                                    adapter.notifyDataSetChanged();
                                    //隐藏刷新按钮
                                    swipeRefreshLayout.setRefreshing(false);
                                    isRefresh = false;
                                }
                            });
                        }
                    }).start();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticProperty.RETURN_NEWSLIST && resultCode == StaticProperty.RETURN_NEWSLIST){
            //回到进入新闻页面之前新闻列表的位置
            moveToPosition();

        }
    }

    /**
     * 移动新闻列表位置到进入新闻页面时的位置
     */
    public void moveToPosition(){
        int position = manager.findFirstVisibleItemPosition();
        int top = 0;
        View view = recyclerView.getChildAt(position);
        if (view != null) {
            top = view.getTop();
        }
        manager.scrollToPositionWithOffset(position, top);

    }





}
