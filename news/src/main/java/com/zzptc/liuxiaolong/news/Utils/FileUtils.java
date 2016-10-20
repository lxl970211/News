package com.zzptc.liuxiaolong.news.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.zzptc.liuxiaolong.news.MainActivity;
import com.zzptc.liuxiaolong.news.datapars.GetNews;
import com.zzptc.liuxiaolong.news.model.NewsData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lxl97 on 2016/7/30.
 */
public class FileUtils {


    public static final String IMAGECACHEPATH = Environment.getExternalStorageDirectory() + "/MyNews/cache/image";
    public static final String TXTCACHEPATH = Environment.getExternalStorageDirectory() + "/MyNews/cache";
    //文件后缀名
    private static final String CACHETAIL = ".cach";


    //文件路径
    public static final String folderName = "MyNews";

    public static final String cacheFolderName = "cache";

    public static final String SDCardPath = Environment.getExternalStorageDirectory() + File.separator;


    //长时间缓存1小时
    private static final long CACHE_LONG_TIME = 3600000;


    //判断文件路径是否存在，不存在则创建
    public static boolean isFileExists() {
        File file = new File(IMAGECACHEPATH);

        return file.exists() ? true : file.mkdir();

    }


    /**
     * 将url转为文件名
     *
     * @param url
     * @return
     */
    public String converUrlToFileName(String url) {

        return url.hashCode() + CACHETAIL;
    }


    /**
     * 将图片保存
     *
     * @param url
     * @param bm
     */
    public void saveBitmapToFile(String url, Bitmap bm) {

        if (bm == null) {
            return;
        }
        String filename = converUrlToFileName(url);

        //判断文件路径是否为空，为空则创建
        isFileExists();

        File file = new File(IMAGECACHEPATH + File.separator + filename);
        try {
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void saveBitmapToFile(Bitmap bm, String imgName) {

        if (bm == null) {
            return;
        }


        File file = new File(Environment.getExternalStorageDirectory()+"/MyNews/"+imgName);
        try {
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 从缓存中读取图片
     *
     * @param url
     * @return
     */
    public Bitmap getPicFromCache(String url) {

        String filepath = IMAGECACHEPATH + File.separator + converUrlToFileName(url);

        File file = new File(filepath);

        if (file != null && file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filepath);

            return bitmap;
        }
        return null;
    }

    //删除缓存图片
    public static void deleteCacheImg() {

        String filepath = IMAGECACHEPATH + File.separator;
        File f = new File(filepath);
        //得到当前路径下所有文件
        File[] files = f.listFiles();
        if (f.exists()) {
            for (File file : files) {
                //缓存时间大于一小时
                if (!getalreadyCacheTime(file)) {
                    //删除文件
                    file.delete();

                }

            }
        }


    }
    public static boolean deleteCache(){
        boolean flags = false;
        File file = new File(TXTCACHEPATH);
        File[] files = file.listFiles();

        if (file.exists()){
            for (File file1 : files){

                if (file1.isDirectory()){
                    File[] children = file1.listFiles();
                    for (File f1 : children){
                        f1.delete();
                    }

                }else{
                    file1.delete();
                }


            }


        }
        flags = true;
        return flags;

    }
    /**
     * 创建文件缓存目录
     *
     * @param folderName
     */
    public static void createCacheFolder(String folderName) {

        File file = new File(folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

    }


    /**
     * 缓存是否大于一小时
     *
     * @param file
     * @return
     */
    public static boolean getalreadyCacheTime(File file) {
        //当前时间-文件最后修改时间

        return (new Date().getTime() - file.lastModified()) < CACHE_LONG_TIME ? true : false;
    }


    public static void saveNewsToSDCardCache(final String fileName, final String NewsJson, final Context context) {
        //启用子线程将新闻数据缓存到本地
        new Thread(new Runnable() {
            @Override
            public void run() {

                //将获取到的json数据保存到sdcard中
                File file = new File(FileUtils.TXTCACHEPATH, fileName);

                FileOutputStream fos = null;
                try {

                    fos = new FileOutputStream(file);
                    //写入json数据
                    fos.write(NewsJson.getBytes());
                    //关闭写入流
                    fos.close();
                    GetNews getNews = new GetNews(context);
                    List<NewsData> list = getNews.parsJsonToData(NewsJson);
                    //下载图片
                    for (int i = 0; i < list.size(); i++) {
                        NewsData newsData = list.get(i);
                        GetNews.downloadImageByUrl(newsData.getThumbnail_pic_s());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }


    public static String formatLongToTimeStr(Long l) {
        String str = "";
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour + "小时" + minute + "分钟" + second + "秒";
        return strtime;
    }

    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + "日" + hours + "小时" + minutes + "分"
                + seconds + "秒";
    }

    public static final String fileName(String fileName) {

        return fileName + ".txt";
    }


    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }


    }

    /**
     * 得到缓存大小
     * @return
     */
    public static String getCacheSize(){
        File file = new File(FileUtils.TXTCACHEPATH);
        return String.format("%.2f", getDirSize(file))+"MB";
    }



}
