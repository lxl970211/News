package com.zzptc.liuxiaolong.news.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
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
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zzptc.liuxiaolong.news.MyApplication;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.FileUtils;
import com.zzptc.liuxiaolong.news.model.NewsData;


import java.io.File;
import java.util.List;

/**
 * Created by lxl97 on 2016/7/22.
 */
public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {

    private int lastPosition = -1;

    List<NewsData> list;
    private Context context;
    private FileUtils fileUtils;
    private Handler handler;



    //点击监听接口
    public interface OnRecylerAdapterListener{
        void OnItemClickListener(View itemView, int position, NewsData newsData);
    }

    private OnRecylerAdapterListener onRecylerAdapterListener;

    public void setOnItemClickListener(OnRecylerAdapterListener listener){
        onRecylerAdapterListener = listener;
    }

    //构造方法
    public RecylerViewAdapter(List<NewsData> list, Context context){
        this.list = list;
        this.context = context;
        fileUtils = new FileUtils();
        handler = new Handler();

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final NewsData newsData = list.get(position);
            //新闻数据不为空
            if (newsData != null) {
                //新闻标题
                holder.tv_newsTitle.setText(newsData.getTitle());
                //新闻来源作者
                holder.tv_newsAuthor_name.setText(newsData.getAuthor_name());
                //加载图片如果缓存中有就从缓存中读取没有就从网络获取
                if (fileUtils.getPicFromCache(newsData.getThumbnail_pic_s()) != null) {
                    //本地缓存新闻图片路径
                    String filepath = FileUtils.IMAGECACHEPATH + File.separator + fileUtils.converUrlToFileName(newsData.getThumbnail_pic_s());
                    String imageurl = ImageDownloader.Scheme.FILE.wrap(filepath);
                    //加载本地新闻图片

                    ImageLoader.getInstance().displayImage(imageurl, holder.iv_newsPic, MyApplication.mOptions);
                } else {
                    //网络获取图片
                    ImageLoader.getInstance().displayImage(newsData.getThumbnail_pic_s(), holder.iv_newsPic, MyApplication.mOptions);
                }


                //动画
                setAnimation(holder.card, position);
            }


        //监听
            if (onRecylerAdapterListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRecylerAdapterListener.OnItemClickListener(holder.itemView, position, newsData);
                    }
                });


            }
    }


    @Override
    public int getItemCount() {
        return list.size();
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





}
