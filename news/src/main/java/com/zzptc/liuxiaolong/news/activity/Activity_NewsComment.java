package com.zzptc.liuxiaolong.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.PushData;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.adapter.CommentListAdapter;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.fragment.FragmentDialog_WriteComment;
import com.zzptc.liuxiaolong.news.javabean.Comment;
import com.zzptc.liuxiaolong.news.javabean.CommentBean;
import com.zzptc.liuxiaolong.news.view.AutoLoadRecyclerView;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collections;
import java.util.Comparator;

@ContentView(R.layout.activity__news_comment)
public class Activity_NewsComment extends AppCompatActivity implements LoadFinshCallBack, OnRequestResultListener, PushData.OnPushInfoListener, CommentListAdapter.OnClickListener{
    @ViewInject(R.id.recy_comment)
    private AutoLoadRecyclerView recyclerView;
    private CommentListAdapter adapter;
    @ViewInject(R.id.tv_noComment)
    private TextView tv_noComment;
    @ViewInject(R.id.tv_writeComment)
    private TextView tv_writeComment;
    @ViewInject(R.id.btn_new_comment)
    private Button new_comment;
    @ViewInject(R.id.btn_earliest)
    private Button btn_earliest;

    private String url;
    private String title;
    private PushData pushData;
    private CommentBean commentBean;
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
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            pushData = new PushData(this);
            pushData.getCommentList(url);
            pushData.setOnPushInfoListener(this);




    }

    @Event(value = {R.id.tv_writeComment, R.id.btn_earliest, R.id.btn_new_comment})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.tv_writeComment:
                FragmentDialog_WriteComment fragmentDialog_writeComment = FragmentDialog_WriteComment.newInstance(url, title);
                fragmentDialog_writeComment.show(getSupportFragmentManager(), null);
                fragmentDialog_writeComment.setOnRequestResultListener(this);
                break;

            case R.id.btn_earliest:
                if (commentBean.getList().size() > 0) {
                    //升序
                    Collections.sort(commentBean.getList(), new Comparator<Comment>() {
                        @Override
                        public int compare(Comment lhs, Comment rhs) {

                            return lhs.getLou().compareTo(rhs.getLou());
                        }
                    });
                    adapter.notifyDataSetChanged();
                    select(1);

                }
                break;
            case R.id.btn_new_comment:
                if (commentBean.getList().size() > 0) {
                    //降序
                    Collections.sort(commentBean.getList(), new Comparator<Comment>() {
                        @Override
                        public int compare(Comment lhs, Comment rhs) {
                            return rhs.getLou().compareTo(lhs.getLou());
                        }
                    });
                    adapter.notifyDataSetChanged();
                    select(0);
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        finish();
        MyAnimator.closeActivityAnim(this);
        super.onBackPressed();
    }

    @Override
    public void OnGetRequestResultStatusListener(int status) {
        switch (status){
            case 3:

                pushData.getCommentList(url);
                break;
        }

    }


    @Override
    public void OnGetRequestDataListener(String json) {
        final Gson gson = new Gson();
        commentBean = gson.fromJson(json, CommentBean.class);
        //评论列表为空则不显示评论列表
        if (commentBean.getCommentCount() == 0){
            tv_noComment.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            tv_noComment.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new CommentListAdapter(commentBean.getList(), this);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.setOnClickListener(this);

        }
    }

    public void select(int i){
        switch (i){
            case 0:
                new_comment.setBackgroundResource(R.drawable.commentbtn_selector_shape);
                new_comment.setTextColor(getResources().getColor(android.R.color.white));
                btn_earliest.setBackgroundResource(R.drawable.commentbtn_unselector_shape);
                btn_earliest.setTextColor(getResources().getColor(R.color.blue));
                break;

            case 1:
                new_comment.setBackgroundResource(R.drawable.commentbtn_unselector_shape);
                new_comment.setTextColor(getResources().getColor(R.color.blue));
                btn_earliest.setBackgroundResource(R.drawable.commentbtn_selector_shape);
                btn_earliest.setTextColor(getResources().getColor(android.R.color.white));
                break;

        }

    }
    //支持点击事件
    @Override
    public void OnClickListener(View v, final int lou, final int position) {
        final TextView like = (TextView) v.findViewById(R.id.tv_buttress);
        //登录后才可点赞
        if (UserInfoAuthentication.tokenExists(this)) {

            pushData.like(url, lou);
            pushData.setOnRequestResultListener(new OnRequestResultListener() {
                @Override
                public void OnGetRequestResultStatusListener(int status) {
                    switch (status) {
                        case 1:
                            adapter.updateItemData(position);
                            break;
                        case 2:
                            adapter.cancelZan(position);
                            break;

                    }
                }
            });
        }else{
                startActivity(new Intent(this, Activity_Login.class));
                MyAnimator.openActivityAnim(this);
        }

    }


}
