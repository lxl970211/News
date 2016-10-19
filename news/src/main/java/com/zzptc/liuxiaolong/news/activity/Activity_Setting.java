package com.zzptc.liuxiaolong.news.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zzptc.liuxiaolong.news.R;
import com.zzptc.liuxiaolong.news.Utils.FileUtils;
import com.zzptc.liuxiaolong.news.Utils.ImageTools;
import com.zzptc.liuxiaolong.news.Utils.MyAsyncTask;
import com.zzptc.liuxiaolong.news.Utils.MyUtils;
import com.zzptc.liuxiaolong.news.Utils.UserInfoAuthentication;
import com.zzptc.liuxiaolong.news.animator.MyAnimator;
import com.zzptc.liuxiaolong.news.content.ResultCodes;
import com.zzptc.liuxiaolong.news.content.StaticProperty;
import com.zzptc.liuxiaolong.news.fragment.Fragment_CheckUpdate;
import com.zzptc.liuxiaolong.news.fragment.Fragment_modifyName_dialog;
import com.zzptc.liuxiaolong.news.view.BaseActivity;
import com.zzptc.liuxiaolong.news.view.OnRequestResultListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

@ContentView(R.layout.activity_setting)
public class Activity_Setting extends BaseActivity implements MyAsyncTask.OnGetUserInfoListener, OnRequestResultListener{
    @ViewInject(R.id.setting_toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.clear_cache)
    private LinearLayout clear_cache;
    @ViewInject(R.id.tv_aboutapp)
    private TextView tv_aboutapp;
    @ViewInject(R.id.clear_cache_size)
    private TextView clearsize;
    @ViewInject(R.id.tv_feedback)
    private TextView feedback;
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName;
    @ViewInject(R.id.tv_myemail)
    private TextView myemail;



    @ViewInject(R.id.iv_userhead)
    private CircleImageView iv_userhead;

    @ViewInject(R.id.btn_signUp)
    private Button btn_signup;

    private Handler handler;
    private MyAsyncTask myAsyncTask;

    private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
    private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
    private String imgPath  = "";
    private String imgName = "";
    private HttpUtils httpUtils;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();


    }

    public void init(){
        httpUtils = new HttpUtils(10000);
        if (UserInfoAuthentication.tokenExists(this)){
            btn_signup.setVisibility(View.VISIBLE);
            myemail.setText(UserInfoAuthentication.getTokeninfo(this, "email"));
            tv_userName.setText(UserInfoAuthentication.getTokeninfo(this, "name"));
        }else {
            btn_signup.setVisibility(View.GONE);
        }
        handler = new Handler();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



    /**
     * 点击监听
     * @param v
     */
    @Event({R.id.tv_aboutapp,  R.id.clear_cache,R.id.tv_feedback, R.id.iv_userhead, R.id.tv_userName, R.id.btn_signUp, R.id.tv_update})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.tv_aboutapp:
                Intent intent1 = new Intent(this, MyActivity.class);
                intent1.setAction("aboutApp");
                startActivity(intent1);
                MyAnimator.openActivityAnim(this);
                break;
            case R.id.tv_feedback:
                Intent intent2 = new Intent(this, MyActivity.class);
                intent2.setAction("feedback");
                startActivity(intent2);
                MyAnimator.openActivityAnim(this);
                break;


            case R.id.clear_cache:
                handler.post(clearCache);
                break;

            case R.id.iv_userhead:
                if (UserInfoAuthentication.tokenExists(this)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setItems(new String[]{"从相册选取", "拍照"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0:

                                            doPickPhotoFromGallery();
                                            break;
                                        case 1:

                                            doTakePhoto();
                                            break;

                                    }
                                }
                            }).create();
                    builder.show();

                }else{
                   MyUtils.login(this);
                }

                break;

            case R.id.tv_userName:
                if (!UserInfoAuthentication.tokenExists(this)){
                    //跳转到登录页面
                    MyUtils.login(this);
                }else{
                    //修改用户名
                    Fragment_modifyName_dialog fragment_modifyName_dialog = new Fragment_modifyName_dialog();
                    fragment_modifyName_dialog.show(getSupportFragmentManager(), null);
                    fragment_modifyName_dialog.setOnRequestResultListener(this);

                }

                break;

            case R.id.btn_signUp:
                SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                tv_userName.setText("点击登录");
                myemail.setText("");
                setResult(2);
                btn_signup.setVisibility(View.GONE);
                break;

            case R.id.tv_update:
                Fragment_CheckUpdate checkUpdate = new Fragment_CheckUpdate();
                checkUpdate.show(getSupportFragmentManager(), null);
                break;
        }

    }



    Runnable clearCache = new Runnable() {
        @Override
        public void run() {
            if (FileUtils.deleteCache()){
                clearsize.setText(FileUtils.getCacheSize());
                Toast.makeText(x.app(), "缓存有助于更好的节约数据流量\n如果可以请保留缓存", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(x.app(), "清理出错", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        clearsize.setText(FileUtils.getCacheSize());
    }

    @Override
    public void onBackPressed() {
        finish();
        MyAnimator.closeActivityAnim(this);
    }

    @Override
    public void OnGetUserInfoListener(String name, String email) {
        tv_userName.setText(name);
        myemail.setText(UserInfoAuthentication.getTokeninfo(this, "email"));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCodes.LOGIN_AUCCESS && resultCode == ResultCodes.LOGIN_AUCCESS) {
            myAsyncTask = new MyAsyncTask(this);
            myAsyncTask.getinfo();
            myAsyncTask.setOnGetUserInfoListener(this);
            btn_signup.setVisibility(View.VISIBLE);
        }

        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case PHOTO_WITH_CAMERA: //拍照获取图片
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) { //是否有SD卡

                        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/MyNews/" + "image.jpg");

                        imgName = createPhotoFileName();
                        //写一个方法将此文件保存到本应用下面啦
                        savePicture(imgName, bitmap);

                        if (bitmap != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);

                            iv_userhead.setImageBitmap(smallBitmap);
                            upload();
                        }
                        Toast.makeText(this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
                    }
                    break;

                case PHOTO_WITH_DATA: //从图库中选择图片
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();

                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                        imgName = createPhotoFileName();
                        //写一个方法将此文件保存到本应用下面啦
                        savePicture(imgName, photo);

                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / 5, photo.getHeight() / 5);
                            new FileUtils().saveBitmapToFile(smallBitmap);
                            iv_userhead.setImageBitmap(smallBitmap);
                            upload();
                        }
                        Toast.makeText(this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    }

    @Override
    public void OnGetRequestResultStatusListener(int status) {
        switch (status){
            case 1:
                tv_userName.setText(UserInfoAuthentication.getTokeninfo(this, "name"));
                break;
        }
    }



    /** 缩放Bitmap图片 **/
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**从相册获取图片**/
    private void doPickPhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");  // 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action
        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/MyNews/","image.jpg"));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍后这个图片都会被替照换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_WITH_DATA); //取得相片后返回到本画面
    }

    /**拍照获取相片**/
    private void doTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机

        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/MyNews/","image.jpg"));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍后这个图片都会被替照换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        //直接使用，没有缩小
        startActivityForResult(intent, PHOTO_WITH_CAMERA);  //用户点击了从相机获取
    }




    /**创建图片不同的文件名**/
    private String createPhotoFileName() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());  //系统当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        fileName = dateFormat.format(date) + ".jpg";
        return fileName;
    }

    /**获取文件路径**/
    public String uri2filePath(Uri uri)
    {
        String[] projection = {MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path =  cursor.getString(column_index);
        return path;
    }

    /**保存图片到本应用下**/
    private void savePicture(String fileName,Bitmap bitmap) {

        FileOutputStream fos =null;
        try {//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
            fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != fos) {
                    fos.close();
                    fos = null;
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile = new File(Environment.getExternalStorageDirectory()+"/MyNews/",
            "image.jpg");


    public void  upload(){
        RequestParams params=new RequestParams();
        params.setHeader("token", UserInfoAuthentication.getTokeninfo(this, "token"));
        params.addBodyParameter(tempFile.getPath().replace("/", ""), tempFile);

        httpUtils.send(HttpMethod.POST,StaticProperty.UPLOAD_SERVLET, params,new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException e, String msg) {
                Toast.makeText(Activity_Setting.this, "上传失败，检查一下服务器地址是否正确", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", e.getExceptionCode() + "====="
                        + msg);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(Activity_Setting.this, "上传成功，马上去服务器看看吧！", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "====upload_error====="
                        + responseInfo.result);
            }
        });


    }
}
