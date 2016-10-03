package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.fragment.Fragment_MyCollectNews;
import com.zzptc.liuxiaolong.news.fragment.Fragment_MyComment;
import com.zzptc.liuxiaolong.news.view.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_my)
public class MyActivity extends BaseActivity {
    @ViewInject(R.id.my_Toolbar)
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();

    }
    public void init(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyAnimator.closeActivityAnim(MyActivity.this);
            }
        });


        Intent intent = getIntent();
        if (intent.getAction().equals("my_comment")){
            toolbar.setTitle("我的评论");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_my, new Fragment_MyComment()).commit();
        }else if (intent.getAction().equals("my_collect")){
            toolbar.setTitle("我的收藏");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_my, new Fragment_MyCollectNews()).commit();
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        MyAnimator.closeActivityAnim(this);
    }
}
