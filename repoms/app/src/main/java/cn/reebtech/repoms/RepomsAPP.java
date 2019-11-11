package cn.reebtech.repoms;

import android.app.Application;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.reebtech.repoms.util.GreenDaoManager;

public class RepomsAPP extends Application{

    private static Context mContext;
    private static String token;
    private static String user;
    private static String srvBaseUrl = "";
    private static String department = "";
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //greenDao全局配置,只希望有一个数据库操作对象
        GreenDaoManager.getInstance();
    }
    public static Context getContext() {
        return mContext;
    }
    public static String getToken(){
        return token;
    }
    public static void setToken(String value){
        token = value;
    }
    public static String getUser(){
        return user;
    }
    public static void setUser(String value){
        user = value;
    }
    public static String getSrvBaseUrl() {
        return srvBaseUrl;
    }
    public static void setSrvBaseUrl(String srvBaseUrl) {
        RepomsAPP.srvBaseUrl = srvBaseUrl;
    }
    public static void setDepartment(String department){
        RepomsAPP.department = department;
    }
    public static String getDepartment(){
        return department;
    }

}
