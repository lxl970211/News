package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.adapter.CommentListAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.javabean.CommentBean;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity__news_comment)
public class Activity_NewsComment extends AppCompatActivity implements LoadFinshCallBack{
    @ViewInject(R.id.recy_comment)
    private AutoLoadRecyclerView recyclerView;
    private CommentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    @Override
    public void loadFinsh(Object obj) {

    }
    public void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CommentBean commentBean = (CommentBean) bundle.getSerializable("commentBean");
        System.out.println(commentBean.getList().size());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CommentListAdapter(commentBean.getList(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        MyAnimator.closeActivityAnim(this);
        super.onBackPressed();
    }
}
