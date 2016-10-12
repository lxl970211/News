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


import java.util.List;

/**
 * Created by lxl97 on 2016/10/7.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder>{
    private Context context;
    private List<Comment> list;

    private int type =0;
    public interface OnClickListener{
        void OnClickListener(View v, int lou, int position);

    }

    private OnClickListener onClickListener;
    public void setOnClickListener(OnClickListener listener){
        onClickListener = listener;
    }

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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Comment comment = list.get(position);
        if (comment != null){
            holder.name.setText(comment.getName());
            long time = MyUtils.nowTimeLong() - MyUtils.timeStringToLong(comment.getCommentTime());
            holder.commentTime.setText(MyUtils.getCommentDistanceCurrentTime(time)+"前");
            holder.content.setText(comment.getContent());


            if (type == 0) {
                holder.like.setText("支持(" + list.get(position).getLike() + ")");
            }else if (type == 1){
                holder.like.setText("取消("+ list.get(position).getLike()+")");
            }
            holder.lou.setText(comment.getLou()+"楼");
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null){
                        onClickListener.OnClickListener(holder.like, comment.getLou(), position);
                    }
                }
            });

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
        TextView like;
        TextView content;
        TextView lou;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_comment_name);
            commentTime = (TextView) itemView.findViewById(R.id.tv_comment_time);
            content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            like = (TextView) itemView.findViewById(R.id.tv_buttress);
            lou = (TextView) itemView.findViewById(R.id.tv_lou);
        }
    }


    public void updateItemData(int position){
        int count = list.get(position).getLike();
        list.get(position).setLike((count +1));
        type = 1;
        notifyItemChanged(position);

    }

    public void cancelZan(int position){
        int count = list.get(position).getLike();
        list.get(position).setLike((count - 1));

        type = 0;
        notifyItemChanged(position);
    }

}
