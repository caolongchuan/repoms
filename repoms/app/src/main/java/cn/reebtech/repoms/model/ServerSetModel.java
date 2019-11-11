package cn.reebtech.repoms.model;

import android.app.Activity;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.ServerBean;
import cn.reebtech.repoms.contact.ServerSetContact;
import cn.reebtech.repoms.model.entity.Server;
import cn.reebtech.repoms.model.greendao.ServerDao;
import cn.reebtech.repoms.presenter.ServerSetPresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.util.net.ApiService;
import cn.reebtech.repoms.util.net.cookie.CookieJarImpl;
import cn.reebtech.repoms.util.net.cookie.store.PersistentCookieStore;
import cn.reebtech.repoms.util.net.fastjsonconverter.FastJsonConvertFactory;
import cn.reebtech.repoms.util.net.http.HttpParamInterceptor;
import cn.reebtech.repoms.util.net.http.RetrofitClient;
import cn.reebtech.repoms.util.net.result.LoginResult;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ServerSetModel implements ServerSetContact.ServerSetMdl {
    private ApiService apiService;
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    public ServerSetModel(ServerSetContact.ServerSetUI view){
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpParamInterceptor((Activity)view))
                .cookieJar(new CookieJarImpl(new PersistentCookieStore((Activity)view)))
                .build();

    }
    @Override
    public void testServerSet(ServerBean server, final IConDbListener callback) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://" + server.getHost())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConvertFactory.create())
                .client(mOkHttpClient).build();
        apiService = mRetrofit.create(ApiService.class);
        apiService.login(server.getUser(), server.getPasswd())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(LoginResult respResult) {
                        if(respResult.getCode() == 200){
                            callback.onSuccess(ServerSetPresenter.TYPE_OP_TEST_SRVSET, "", new BaseResultBean(0, ""));
                        }
                        else{
                            callback.onFailure(ServerSetPresenter.TYPE_OP_TEST_SRVSET, new BaseResultBean(101, "用户名或密码无效"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(ServerSetPresenter.TYPE_OP_TEST_SRVSET, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }

    @Override
    public void saveServerSet(ServerBean server, IConDbListener callback) {
        ServerDao table = getServerDao();
        List<Server> records = table.queryBuilder().list();
        Server record;
        if(records.size() > 0){
            record = records.get(0);
        }
        else{
            record = new Server();
        }
        record.setUrl(server.getHost());
        record.setUser(server.getUser());
        record.setPassword(server.getPasswd());
        try{
            table.save(record);
            callback.onSuccess(ServerSetPresenter.TYPE_OP_SAVE_SRVSET, "", new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(ServerSetPresenter.TYPE_OP_SAVE_SRVSET, new BaseResultBean(101, e.getMessage()));
        }
    }

    @Override
    public void loadCurrentConfig(IConDbListener callback) {
        ServerDao table = getServerDao();
        try{
            Server record = table.queryBuilder().unique();
            ServerBean result = new ServerBean();
            if(record != null){
                result.setHost(record.getUrl());
                result.setUser(record.getUser());
                result.setPasswd(record.getPassword());
            }
            callback.onSuccess(ServerSetPresenter.TYPE_LOAD_CONFIG, result, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(ServerSetPresenter.TYPE_LOAD_CONFIG, new BaseResultBean(101, e.getMessage()));
        }
    }

    private ServerDao getServerDao(){
        return GreenDaoManager.getInstance().getSession().getServerDao();
    }
}
