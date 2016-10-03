package com.zzptc.liuxiaolong.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.javabean.CollectNewsBean;
import com.zzptc.liuxiaolong.news.javabean.CollectNewsData;
import com.zzptc.liuxiaolong.news.view.LoadFinshCallBack;
import com.zzptc.liuxiaolong.news.view.OnLongClickListener;

import java.util.List;

/**
 * Created by lxl97 on 2016/10/3.
 */

public class CollectNewsAdapter  extends RecyclerView.Adapter<CollectNewsAdapter.MyViewHolder>{
    private Context context;
    private List<CollectNewsData> list;

    public CollectNewsAdapter(List<CollectNewsData> list, Context context){
        this.context = context;
        this.list = list;
    }


    public interface OnClickerListener{
        void OnItemClickListener(View view, String url);
    }
    private OnClickerListener onClickerListener;
    private com.zzptc.liuxiaolong.news.view.OnLongClickListener onLongClickListener;
    public void setOnClickListener(OnClickerListener listener){
        onClickerListener = listener;
    }
    public void setOnLongClickListener(com.zzptc.liuxiaolong.news.view.OnLongClickListener listener){
        onLongClickListener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_collectnews, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final CollectNewsData collectNewsData = list.get(position);
        if (collectNewsData != null) {
            holder.newsTitle.setText(collectNewsData.getTitle());
            holder.newsTime.setText(collectNewsData.getCollectTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickerListener != null) {
                        onClickerListener.OnItemClickListener(holder.itemView, collectNewsData.getUrl());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null){
                        onLongClickListener.OnLongClickListener(holder.itemView, position, collectNewsData.getUrl());
                    }
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView newsTitle;
        private TextView newsTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.tv_collectNews_title);
            newsTime = (TextView) itemView.findViewById(R.id.tv_collectNews_time);
        }
    }

    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
}
