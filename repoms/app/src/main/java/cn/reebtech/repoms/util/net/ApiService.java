package cn.reebtech.repoms.util.net;


import java.util.List;

import cn.reebtech.repoms.util.net.result.LoginResult;
import cn.reebtech.repoms.util.net.result.SyncResult;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by on 2017/6/26.
 * 作用：
 */

public interface ApiService {


    @POST(Contacts.LOGIN_URL)
    Observable<LoginResult> login(@Query("username") String name, @Query("password") String password);

    @GET(Contacts.USER_URL)
    Observable<String> featchUsers(@Header("accessToken") String token);

    @GET(Contacts.DEPT_URL)
    Observable<String> featchDepts(@Header("accessToken") String token);

    @GET(Contacts.ASSEET_URL)
    Observable<String> featchAssets(@Header("accessToken") String token);

    @GET(Contacts.WAREHOUSE_URL)
    Observable<String> featchWareHouses(@Header("accessToken") String token);

    @GET(Contacts.ACLST_URL)
    Observable<String> featchAClst(@Header("accessToken") String token);

    @POST(Contacts.ORDER_IN_URL)
    Observable<SyncResult> pushOrderIn(@Header("accessToken") String token, @Body RequestBody data);

    @POST(Contacts.ORDER_OUT_URL)
    Observable<SyncResult> pushOrderOut(@Header("accessToken") String token, @Body RequestBody  data);

    @POST(Contacts.ORDER_INVT_URL)
    Observable<SyncResult> pushOrderInvt(@Header("accessToken") String token, @Body RequestBody  data);


    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @GET
    @Streaming
    Flowable<Response<ResponseBody>> download(@Url String url);

    @GET
    @Streaming
    Flowable<ResponseBody> download2(@Url String url);
}
