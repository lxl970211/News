package com.zzptc.liuxiaolong.news.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.NetWorkStatus;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.activity.Activity_NewsDetail;
import com.zzptc.liuxiaolong.news.adapter.CollectNewsAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.javabean.CollectNewsBean;
import com.zzptc.liuxiaolong.news.javabean.CollectNewsData;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;
import com.zzptc.liuxiaolong.news.view.OnLongClickListener;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment__my_collect_news)
public class Fragment_MyCollectNews extends Fragment implements PushData.OnPushInfoListener,OnRequestResultListener, CollectNewsAdapter.OnClickerListener, OnLongClickListener{
    private CollectNewsAdapter adapter;
    @ViewInject(R.id.rv_my_collect)
    private AutoLoadRecyclerView autoLoadRecyclerView;
    @ViewInject(R.id.no_collectNews)
    private TextView no_collectNews;
    private LoadFinshCallBack loadFinshCallBack;
    private PushData pushData;
    private int Itemposition;
    public Fragment_MyCollectNews() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);
        init();
        return v;
    }


    public void init(){

        pushData = new PushData(getContext());


        loadFinshCallBack = autoLoadRecyclerView;
        autoLoadRecyclerView.setHasFixedSize(true);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });


    }


    @Override
    public void OnGetRequestDataListener(String json) {
        Gson gson = new Gson();
        CollectNewsBean collectNewsBean = gson.fromJson(json, CollectNewsBean.class);
        List<CollectNewsData> list = collectNewsBean.getList();

        if (list != null) {
            adapter = new CollectNewsAdapter(list, getContext());
            autoLoadRecyclerView.setAdapter(adapter);
            adapter.setOnClickListener(this);
            adapter.setOnLongClickListener(this);
        }else {
            no_collectNews.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void OnItemClickListener(View view, String url) {
        Intent intent = new Intent(getContext(), Activity_NewsDetail.class);
        NewsData newsData = new NewsData();
        newsData.setUrl(url);
        intent.putExtra("newsData", newsData);
        startActivity(intent);
        MyAnimator.openActivityAnim(getActivity());
    }


    @Override
    public void OnLongClickListener(View view, final int position, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(new String[]{"分享", "删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "xinwen");
                                intent.putExtra(Intent.EXTRA_TEXT, url);
                                startActivity(Intent.createChooser(intent, "分享到"));
                                break;

                            case 1:
                                NewsData newsData = new NewsData();
                                newsData.setUrl(url);
                                pushData.deleteCollect();
                                Itemposition = position;
                                break;
                        }
                    }
                })
                .create();
                builder.show();

    }

    @Override
    public void OnGetRequestResultStatusListener(int status) {
        switch (status){
            case 0:
                adapter.removeItem(Itemposition);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCodes.LOGIN_AUCCESS && resultCode == ResultCodes.LOGIN_AUCCESS){
            pushData.getCollectNewsList();
            pushData.setOnPushInfoListener(this);
            pushData.setOnRequestResultListener(this);
        }
    }


    @Override
    public void onResume() {
        //网路已连接
        if (NetWorkStatus.getNetWorkType(getContext()) != 0){
            //已登录
            if (UserInfoAuthentication.tokenExists(getContext())){

                pushData.getCollectNewsList();
                pushData.setOnPushInfoListener(this);
                pushData.setOnRequestResultListener(this);
                registerForContextMenu(autoLoadRecyclerView);
            }
        }else {
            Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }




}
