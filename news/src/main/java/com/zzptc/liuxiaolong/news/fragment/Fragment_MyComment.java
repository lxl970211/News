package com.zzptc.liuxiaolong.news.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.NetWorkStatus;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.activity.Activity_Login;
import com.zzptc.liuxiaolong.news.activity.Activity_NewsDetail;
import com.zzptc.liuxiaolong.news.adapter.MyCommentListAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.javabean.CommentBean;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment__my_comment)
public class Fragment_MyComment extends Fragment implements PushData.OnPushInfoListener, MyCommentListAdapter.OnClickListener{


    @ViewInject(R.id.recy_mycomment)
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private MyCommentListAdapter adapter;
    private PushData pushData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);
        init();
        return v;
    }

    public void init(){
        pushData = new PushData(getContext());

        if (NetWorkStatus.getNetWorkType(getContext()) != 0){
            if (UserInfoAuthentication.tokenExists(getContext())){
                pushData.getMyCommentList();
                pushData.setOnPushInfoListener(this);


            }else{
                startActivity(new Intent(getContext(), Activity_Login.class));
                MyAnimator.openActivityAnim(getActivity());
            }
        }else{
            Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void OnGetRequestDataListener(String json) {
        CommentBean commentbean = new Gson().fromJson(json, CommentBean.class);
        adapter = new MyCommentListAdapter(commentbean.getList(), getContext());
        adapter.setOnClickListener(this);
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        autoLoadRecyclerView.setAdapter(adapter);

    }

    @Override
    public void OnClickListener(NewsData newsData) {
        Intent intent = new Intent(getContext(), Activity_NewsDetail.class);
        intent.putExtra("newsData", newsData);
        startActivity(intent);
        MyAnimator.openActivityAnim(getActivity());
    }
}
