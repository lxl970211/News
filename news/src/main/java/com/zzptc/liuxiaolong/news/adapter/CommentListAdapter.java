package com.zzptc.liuxiaolong.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.javabean.Comment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by lxl97 on 2016/10/7.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder>{
    private Context context;
    private List<Comment> list;

    public CommentListAdapter(List<Comment> list, Context context){
        this.list = list;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comment comment = list.get(position);
        System.out.println(comment.getLou());
        if (comment != null){

            holder.name.setText(comment.getName());
            long time = MyUtils.nowTimeLong() - MyUtils.timeStringToLong(comment.getCommentTime());
            holder.commentTime.setText(MyUtils.getCommentDistanceCurrentTime(time)+"前");
            holder.content.setText(comment.getContent());
            holder.zan.setText("支持("+comment.getZan()+")");
            holder.contra.setText("反对("+comment.getContra()+")");
            holder.lou.setText(comment.getLou()+"楼");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView header;
        TextView name;
        TextView commentTime;
        TextView zan;
        TextView contra;
        TextView content;
        TextView lou;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_comment_name);
            commentTime = (TextView) itemView.findViewById(R.id.tv_comment_time);
            contra = (TextView) itemView.findViewById(R.id.tv_contra);
            content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            zan = (TextView) itemView.findViewById(R.id.tv_buttress);
            lou = (TextView) itemView.findViewById(R.id.tv_lou);
        }
    }
}
