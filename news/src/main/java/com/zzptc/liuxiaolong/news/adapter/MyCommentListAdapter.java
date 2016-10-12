package com.zzptc.liuxiaolong.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.javabean.Comment;
import com.zzptc.liuxiaolong.news.model.NewsData;

import java.util.List;

/**
 * Created by lxl97 on 2016/10/12.
 */

public class MyCommentListAdapter extends RecyclerView.Adapter<MyCommentListAdapter.MyViewHolder>{

    private Context context;
    private List<Comment> list;

    public interface OnClickListener{
        void OnClickListener(NewsData newsData);

    }
    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener listener){
        this.onClickListener = listener;
    }
    public MyCommentListAdapter(List<Comment> list,Context context){
        this.list = list;
        this.context = context;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_mycomment, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comment comment = list.get(position);
        if (comment != null){
            holder.name.setText("我");
            holder.time.setText(comment.getCommentTime());
            holder.newsTitle.setText("原文："+comment.getTitle());
            holder.content.setText(comment.getContent());
            holder.lou.setText(comment.getLou()+"楼");
            holder.zan.setText("支持("+comment.getLike()+")");
            final NewsData newsData = new NewsData();
            newsData.setTitle(comment.getTitle());
            newsData.setUrl(comment.getNewsId());
            holder.newsTitle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (onClickListener != null){

                        onClickListener.OnClickListener(newsData);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView time;
        TextView lou;
        TextView content;
        TextView newsTitle;
        TextView zan;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_mycomment_name);
            time = (TextView) itemView.findViewById(R.id.tv_mycomment_time);
            lou = (TextView) itemView.findViewById(R.id.tv_mylou);
            content = (TextView) itemView.findViewById(R.id.tv_mycomment_content);
            newsTitle = (TextView) itemView.findViewById(R.id.tv_mynewsTitle);
            zan = (TextView) itemView.findViewById(R.id.tv_mybuttress);

        }
    }

}
