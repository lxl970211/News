package com.zzptc.liuxiaolong.news.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzptc.liuxiaolong.news.MyApplication;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.model.Search_Result;

import java.util.List;

/**
 * Created by lxl97 on 2016/8/18.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.MyViewHolder>{
    private int lastPosition = -1;

    private Context context;
    private List<Search_Result.S_NewsData> list;

    public interface OnClickerListener{
        void OnclickerListener(View v, int position, String NewsUrl);
    }

    private OnClickerListener onclickerlistener;

    public void setOnClickerListener(OnClickerListener listener){
        onclickerlistener = listener;
    }

    public SearchRecyclerViewAdapter(Context context, List<Search_Result.S_NewsData> list){
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Search_Result.S_NewsData newsData = list.get(position);
        if (newsData != null){

            holder.tv_newsTitle.setText(newsData.getTitle());
            holder.tv_newsAuthor_name.setText(newsData.getCategory());
            ImageLoader.getInstance().displayImage(newsData.getPic(), holder.iv_newsPic, MyApplication.mOptions);

        }
        setAnimation(holder.card, position);
        if (onclickerlistener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickerlistener.OnclickerListener(holder.itemView, position, newsData.getUrl());

                }
            });


        }

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_newsTitle;
        TextView tv_newsAuthor_name;
        ImageView iv_newsPic;
        CardView card;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_newsTitle= (TextView) itemView.findViewById(R.id.tv_newsTitle);
            tv_newsAuthor_name = (TextView) itemView.findViewById(R.id.tv_newsAuthor_name);
            iv_newsPic = (ImageView) itemView.findViewById(R.id.iv_newsPic);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }



    /**
     * 动画
     * @param viewToAnimate
     * @param position
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.card.clearAnimation();

    }
}
