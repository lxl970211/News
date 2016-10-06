package com.zzptc.liuxiaolong.news.content;

/**
 * Created by lxl97 on 2016/7/23.
 */
public class StaticProperty {


    public static final String APPKEYS = "36e3354e519719700563b16e67cf04cd";
    public static final String JISUAPIKEYS = "284afd74001bd24d";

    //删除时间过长的新闻缓存
    public static final String DELETE_TIMEOUT_CACHE= "DELETE_TIMEOUT_CACHE";
    public static final String CLEAR_CACHE = "CLEAR_CACHE";
    //保存新闻到SDCard
    public static final String SAVENEWS_TOSDCARD_CACHE = "SAVENEWS";
    //收藏新闻
    public static final String COLLECT_NEWS = "COLLECT_NEWS";

    public static final int RETURN_NEWSLIST = 2;
    //密码正则表达式
    public static final String PASSWORD_REGULAT_EXPRESSIONS = "^[a-zA-Z]\\w{5,17}$";
    //邮箱正则表达式
    public static final String EMAIL_REGULAT_EXPRESSIONS = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    //服务器URL
    public static final String SERVERURL = "http://192.168.99.232:8080/News/servlet/";
    //新闻SDKURL
    public static final String NEWS_SDK_URL = "http://v.juhe.cn/toutiao/index";
    //注册servlet
    public static final String REGISTER_SERVLET = SERVERURL+"RegisterServlet";
    //登录servlet
    public static final String LOGIN_SERVLET = SERVERURL+"LoginServlet";

}
