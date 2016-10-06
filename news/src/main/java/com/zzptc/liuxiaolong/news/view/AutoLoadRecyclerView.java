package com.zzptc.liuxiaolong.news.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by lxl97 on 2016/8/9.
 */
public class AutoLoadRecyclerView extends RecyclerView implements LoadFinshCallBack {


    private onLoadMoreListener loadMoreListener;    //加载更多回调
    private boolean isLoadingMore;                  //是否加载更多


    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new AutoLoadScrollListener(null, true, true));
    }

    @Override
    public void loadFinsh(Object obj) {
        isLoadingMore = false;
    }


    public void setOnPauseListenerParams(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {

        addOnScrollListener(new AutoLoadScrollListener(imageLoader, pauseOnScroll, pauseOnFling));

    }

    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }



    public interface onLoadMoreListener {
        void loadMore();
    }

    /**
     * 滑动自动加载
     */
    private class AutoLoadScrollListener extends OnScrollListener {

        private ImageLoader imageLoader;
        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
            this.imageLoader = imageLoader;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = AutoLoadRecyclerView.this.getAdapter().getItemCount();
                //有回调接口，且不是加载状态，且计算后剩下2个item，且处于向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount -
                        3 && dy > 0) {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }
        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；由于用户的操作，屏幕产生惯性滑动时为2
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //根据屏幕状态进行处理
            if (imageLoader != null) {
                switch (newState) {
                    case 0:
                        imageLoader.resume();
                        break;

                    case 1:
                        if (pauseOnScroll) {
                            imageLoader.pause();
                        } else {
                            imageLoader.resume();
                        }
                        break;

                    case 2:
                        if (pauseOnFling) {
                            imageLoader.pause();
                        } else {
                            imageLoader.resume();
                        }
                        break;
                }
            }
        }
    }
}

