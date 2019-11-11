package cn.reebtech.repoms.util.net.http;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.net.Contacts;
import cn.reebtech.repoms.util.net.cookie.CookieJarImpl;
import cn.reebtech.repoms.util.net.cookie.store.PersistentCookieStore;
import cn.reebtech.repoms.util.net.fastjsonconverter.FastJsonConvertFactory;
import cn.reebtech.repoms.util.net.ApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by 解晓辉  on 2017/5/24 20:49 *
 * QQ  ：811733738
 * 作用:   提供 Retrofit 网络连接 ApiService
 *
 */

public class RetrofitClient {


    private static RetrofitClient instance;

    private OkHttpClient mOkHttpClient;


    private Context mContext;

    private Retrofit mRetrofit;

    private ApiService mApiService;

    public RetrofitClient(Context context) {
        this.mContext = context;
    }

    public static RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    private OkHttpClient provideOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new HttpParamInterceptor(mContext))
                    .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                    .build();
        }


        return mOkHttpClient;
    }

    private Retrofit provideRetrofit() {
        String baseUrl = DBUtils.getSrvBaseUrl();
        if(baseUrl == null || baseUrl.equals("")){
            baseUrl = Contacts.BASE_URL;
        }
        else{
            baseUrl = "http://" + baseUrl + "/";
        }
        Log.i("RequestURL","Request URL" + baseUrl);
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConvertFactory.create())
                    .client(provideOkHttpClient()).build();
        }
        return mRetrofit;
    }

    public ApiService provideApiService() {
        if (mApiService == null) {
            mApiService = provideRetrofit().create(ApiService.class);
        }
        return mApiService;
    }
}
