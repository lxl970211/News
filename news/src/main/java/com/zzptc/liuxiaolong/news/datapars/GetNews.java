package com.zzptc.liuxiaolong.news.datapars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import com.google.gson.Gson;
import com.zzptc.liuxiaolong.news.MainActivity;
import com.zzptc.liuxiaolong.news.Utils.FileUtils;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.model.NewsData;
import com.zzptc.liuxiaolong.news.model.Newsbean;
import com.zzptc.liuxiaolong.news.model.SearchNewsBean;
import com.zzptc.liuxiaolong.news.model.Search_Result;
import com.zzptc.liuxiaolong.news.service.MyService;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * Created by lxl97 on 2016/7/26.
 */
public class GetNews {




    //长时间缓存
    private static final int CACHE_LONG_TIME = 3600000;


    public static final String SDCardPath = Environment.getExternalStorageDirectory() + File.separator;
    //文件路径
    public static final String folderName = "MyNews";
    public static final String cacheFolderName = "cache";

    private Context context;
    public GetNews(Context context){
        this.context = context;

    }

    //获取新闻监听接口
    public interface OnGetNewsListener{

        void OnStartGetNewsListener();
        void OnProcessGetNewsListener(int process, int total);
        void OnCompleteGetNewsListener(List<NewsData> list);
    }

    private OnGetNewsListener onGetNewsListener;

    public void setOnGetNewsListener(OnGetNewsListener listener){
        onGetNewsListener = listener;

    }
    public void getNews(String newsType){
        new GetNewsData().execute(newsType);
    }
    /**
     * 异步获取新闻
     */
    class GetNewsData extends AsyncTask<String, Integer, List<NewsData>>{
        @Override
        protected void onPreExecute() {
            if (onGetNewsListener != null){
                onGetNewsListener.OnStartGetNewsListener();
            }

        }

        @Override
        protected List<NewsData> doInBackground(String... params) {

            //缓存路径/sdcard/MyNews/cache/fileName
            File file = new File(SDCardPath + folderName + File.separator + cacheFolderName, FileUtils.fileName(params[0]));
            //判断文件路径是否存在
            FileUtils.createCacheFolder(FileUtils.IMAGECACHEPATH);
            //如果路径存在并且缓存时间小于一小时则读取缓存文件
            if (file.length()>0 && FileUtils.getalreadyCacheTime(file)) {
                //从缓存中获取新闻
                return getNewsDataFromCache(params[0]);
            } else {
                String json = null;
                try {
                    URL url = new URL(newsUrl(params[0]));
                    json  = getJson(url);

                    //开启服务
                    Intent intent = new Intent(context, MyService.class);
                    //设置动作为保存新闻到sd卡缓存
                    intent.setAction(StaticProperty.SAVENEWS_TOSDCARD_CACHE);
                    //新闻数据
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", FileUtils.fileName(params[0]));
                    bundle.putString("newsJson", getJson(url));
                    intent.putExtras(bundle);
                    //启动服务
                    context.startService(intent);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                //从网络获取新闻

                return parsJsonToData(json);
            }


        }
        //完成
        @Override
        protected void onPostExecute(List<NewsData> list) {
            super.onPostExecute(list);

            if (onGetNewsListener != null){
                onGetNewsListener.OnCompleteGetNewsListener(list);
            }

        }

    }


    /**
     * 下载图片
     * @param strurl
     * @return
     */
    public static Bitmap downloadImageByUrl(String strurl){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap bitmap = null;

        try {
            URL url = new URL(strurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(6 * 1000);
            conn.connect();
            if (conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                int a = 0;
                byte[] bytes = new byte[1024];
                while ((a = is.read(bytes, 0, bytes.length)) > 0){
                    bos.write(bytes, 0, a);

                }
                byte[] bytes1 = bos.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);

                //保存图片到缓存目录/MyNews/cache/image
                new FileUtils().saveBitmapToFile(strurl, bitmap);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    /**
     *从缓存中得到新闻数据
     * @param newsType
     * @return
     */
    public List<NewsData> getNewsDataFromCache(String newsType){
        //如果文件路径不存在则创建
        FileUtils.createCacheFolder(FileUtils.TXTCACHEPATH);

        File file = new File(FileUtils.TXTCACHEPATH+File.separator+FileUtils.fileName(newsType));
        List<NewsData> list = new ArrayList<>();

            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String line = null;
                Gson gson = new Gson();
                //读取到文件的所有数据
                while ((line = br.readLine()) != null) {
                    list = parsJsonToData(line);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        return list;
    }


    /**
     * 得到新闻json数据
     * @param url
     * @return
     */
    public String getJson(URL url){
        String jsonData = null;
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //请求类型
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            //设置连接超时时间
            conn.setConnectTimeout(6 * 1000);
            conn.connect();

            if (conn.getResponseCode() == 200 ){
                byte[] bytes = new byte[1024];
                InputStream is = conn.getInputStream();
                int a = 0;

                while ((a = is.read(bytes, 0, bytes.length)) >0){
                    bos.write(bytes, 0, a);
                }
                byte[] bytes1 = bos.toByteArray();
                //新闻json数据
                jsonData = new String(bytes1);


            }else {
                System.out.println("连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonData;

    }


    /**
     * 解析新闻json数据转为list
     * @param json
     * @return
     */
    public List<NewsData> parsJsonToData(String json){
        Gson gson = new Gson();
        //得到json数据
        Newsbean newsbean = gson.fromJson(json, Newsbean.class);
        List<NewsData> list = new ArrayList<>();
        if (newsbean != null){
            //得到新闻列表数据集合
            list = newsbean.getResult().getData();
        }


        return list;
    }


    public static String newsUrl(String newsType){

        return "http://v.juhe.cn/toutiao/index?type=" + newsType + "&key=" + StaticProperty.APPKEYS;
    }

    public void searchNews(String type){
        new SearchNews().execute(type);
    }
    class SearchNews extends AsyncTask<String, Void, List<NewsData>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (onGetNewsListener != null) {
                onGetNewsListener.OnStartGetNewsListener();
            }
        }

        @Override
        protected List<NewsData> doInBackground(final String... params) {
            GetNews getNews = new GetNews(context);
            URL url = null;

            List<NewsData> list = new ArrayList<>();
            for (int i = 0; i< MainActivity.pinyintitles.length; i++) {
                try {
                    url = new URL(newsUrl(MainActivity.pinyintitles[i]));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                String json = getNews.getJson(url);
                List<NewsData> list2 = getNews.parsJsonToData(json);
                for (NewsData newsData : list2){
                    if (newsData.getTitle().contains(params[0])){
                        list.add(newsData);
                    }
                }
            }
            return list;
        }


        @Override
        protected void onPostExecute(List<NewsData> newsDatas) {
            super.onPostExecute(newsDatas);
            if (onGetNewsListener != null){
                onGetNewsListener.OnCompleteGetNewsListener(newsDatas);
            }
        }
    }


}
