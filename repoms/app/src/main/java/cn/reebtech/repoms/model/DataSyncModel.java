package cn.reebtech.repoms.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.R;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.DataSyncContact;
import cn.reebtech.repoms.model.entity.User;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.LastLoginDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.DataSyncPresenter;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.DataSyncUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.util.net.ApiService;
import cn.reebtech.repoms.util.net.http.RetrofitClient;
import cn.reebtech.repoms.util.net.result.LoginResult;
import cn.reebtech.repoms.util.net.result.SyncResult;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DataSyncModel extends BaseDBModel<User, String, IConDbListener<Integer, LoginBean, BaseResultBean>> implements DataSyncContact.DataSyncMdl {
    private ApiService apiService;
    private List<String> uplItems;
    private List<String> dwlItems;
    private String token;
    private DataSyncUtils dataSyncUtils;
    public DataSyncModel(DataSyncContact.DataSyncUI view){
        uplItems = new ArrayList<String>();
        dwlItems = new ArrayList<String>();
        apiService = RetrofitClient.getInstance((Activity)view).provideApiService();
        dataSyncUtils = new DataSyncUtils();
    }
    /*
     * 获取上次登录信息
     * */
    private LastLoginDao getLastLoginDao(){
        return GreenDaoManager.getInstance().getSession().getLastLoginDao();
    }

    private AssetClsctDao getAssetClsctDao(){
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }
    private WareHouseDao getWarehouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }

    @Override
    public void loadDownloadItems(Context context, IConDbListener callback) {
        String[] downlItems = context.getResources().getStringArray(R.array.downlitems);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < downlItems.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", downlItems[i]);
            item.put("id", i+1);
            data.add(item);
        }
        callback.onSuccess(DataSyncPresenter.OP_LOAD_DOWNLOAD_ITEMS, data, new BaseResultBean(0, ""));
    }

    @Override
    public void loadUploadItems(Context context, IConDbListener callback) {
        String[] uplItems = context.getResources().getStringArray(R.array.uplitems);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < uplItems.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("name", uplItems[i]);
            item.put("id", i+1);
            data.add(item);
        }
        callback.onSuccess(DataSyncPresenter.OP_LOAD_UPLOAD_ITEMS, data, new BaseResultBean(0, ""));
    }

    @Override
    public void downloadItems(List<Integer> items, IConDbListener callback) {
        //生成下载队列
        for (Integer item: items) {
            switch(item.intValue()){
                case 0:
                    dwlItems.add("user");
                    dwlItems.add("dept");
                    break;
                case 1:
                    dwlItems.add("asset");
                    break;
                case 2:
                    dwlItems.add("aclst");
                    break;
                case 3:
                    dwlItems.add("warehouse");
                    break;
            }
        }
        startDataSync(DataSyncPresenter.TYPE_DOWNLOAD, callback);
    }

    @Override
    public void uploadItems(List<Integer> items, IConDbListener callback) {
        //生成上传队列
        for (Integer item: items) {
            switch(item.intValue()){
                case 0:
                    uplItems.add("order_in");
                    break;
                case 1:
                    uplItems.add("order_out");
                    break;
                case 2:
                    uplItems.add("order_invt");
                    break;
            }
        }
        startDataSync(DataSyncPresenter.TYPE_UPLOAD, callback);
    }

    @Override
    public void startDataSync(int type, IConDbListener callback){
        //先登陆，获取Token
        if(token == null || token.equals("")){
            loginServer(type, callback);
            return;
        }
        //根据Type选择启动下载还是上传
        if(type == DataSyncPresenter.TYPE_DOWNLOAD){
            startDownload(type, callback);
        }
        else{
            startUpload(type, callback);
        }
    }
    @Override
    public boolean checkDlValid(){
        return dataSyncUtils.chkDlvalid();
    }

    @Override
    public void syncNext(int type) {
        switch(type){
            case DataSyncPresenter.TYPE_DOWNLOAD:
                if(dwlItems.size() > 0){
                    dwlItems.remove(0);
                }
                break;
            case DataSyncPresenter.TYPE_UPLOAD:
                if(uplItems.size() > 0){
                    uplItems.remove(0);
                }
                break;
        }
    }

    public void loginServer(final int type, final IConDbListener callback) {
        LoginBean loginBean = DBUtils.getServerLogin();
        if(loginBean.getUsrname() == null || loginBean.getUsrname().equals("") || loginBean.getPasswd() == null || loginBean.getPasswd().equals("")) {
            callback.onFailure(DataSyncPresenter.TYPE_LOGIN_SERVER, new BaseResultBean(103, "服务器配置信息无效，请重新配置"));
            return;
        }
        apiService.login(loginBean.getUsrname(), loginBean.getPasswd())
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
                            token = respResult.getResult();
                            Log.i("Token", token);
                            callback.onSuccess(DataSyncPresenter.TYPE_LOGIN_SERVER, type, new BaseResultBean(0, ""));
                        }
                        else{
                            callback.onFailure(type, new BaseResultBean(101, "用户名或密码无效"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void startDownload(int type, IConDbListener callback){
        String downItem = "";
        if(dwlItems.size() > 0){
            downItem = dwlItems.get(0);
        }
        switch(downItem){
            case "": //下载完成
                callback.onSuccess(DataSyncPresenter.TYPE_DOWN_COMPLETE, null , new BaseResultBean(0, ""));
                break;
            case "user":
                downUsers(type, callback);
                break;
            case "dept":
                downDepts(type, callback);
                break;
            case "asset":
                downAssets(type, callback);
                break;
            case "aclst":
                downAClst(type, callback);
                break;
            case "warehouse":
                downWareHouses(type, callback);
                break;
        }
    }
    private void startUpload(final int type, final IConDbListener callback){
        String uplItem = "";
        if(uplItems.size() > 0){
            uplItem = uplItems.get(0);
        }
        switch(uplItem){
            case "": //上传完成
                callback.onSuccess(DataSyncPresenter.TYPE_UP_COMPLETE, null , new BaseResultBean(0, ""));
                break;
            case "order_in":
                uplOrderIn(type, callback);
                break;
            case "order_out":
                uplOrderOut(type, callback);
                break;
            case "order_invt":
                uplOrderInvt(type, callback);
                break;
        }
    }
    private void downUsers(final int type, final IConDbListener callback){
        Log.i("sync", "down_user");
        apiService.featchUsers(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String respResult) {
                        try{
                            JSONObject data = new JSONObject(respResult);
                            if(data.getInt("code") == 200){
                                data.put("type", "user");
                                dataSyncUtils.saveDownloadData(data);
                                callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                            }
                            else{
                                callback.onFailure(type, new BaseResultBean(101, "下载用户信息失败"));
                            }
                        }
                        catch(Exception e){

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void downDepts(final int type, final IConDbListener callback){
        Log.i("sync", "down_dept");
        apiService.featchDepts(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String respResult) {
                        try{
                            JSONObject data = new JSONObject(respResult);
                            if(data.getInt("code") == 200){
                                data.put("type", "dept");
                                dataSyncUtils.saveDownloadData(data);
                                callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                            }
                            else{
                                callback.onFailure(type, new BaseResultBean(101, "下载部门信息失败"));
                            }
                        }
                        catch(Exception e){

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void downAssets(final int type, final IConDbListener callback){
        Log.i("sync", "down_asset");
        apiService.featchAssets(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String respResult) {
                        Log.i("clc", "原始数据:"+respResult);
                        try{
                            JSONObject data = new JSONObject(respResult);
                            if(data.getInt("code") == 200){
                                data.put("type", "asset");
                                dataSyncUtils.saveDownloadData(data);
                                callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                            }
                            else{
                                callback.onFailure(type, new BaseResultBean(101, "下载物资信息失败"));
                            }
                        }
                        catch(Exception e){

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void downAClst(final int type, final IConDbListener callback){
        Log.i("sync", "down_aclst");
        apiService.featchAClst(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String respResult) {
                        try{
                            JSONObject data = new JSONObject(respResult);
                            if(data.getInt("code") == 200){
                                data.put("type", "aclst");
                                dataSyncUtils.saveDownloadData(data);
                                callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                            }
                            else{
                                callback.onFailure(type, new BaseResultBean(101, "下载物资分类信息失败"));
                            }
                        }
                        catch(Exception e){

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void downWareHouses(final int type, final IConDbListener callback){
        Log.i("sync", "down_whouse");
        apiService.featchWareHouses(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(String respResult) {
                        try{
                            JSONObject data = new JSONObject(respResult);
                            if(data.getInt("code") == 200){
                                data.put("type", "whouse");
                                dataSyncUtils.saveDownloadData(data);
                                callback.onSuccess(DataSyncPresenter.TYPE_DOWN_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                            }
                            else{
                                callback.onFailure(type, new BaseResultBean(101, "下载仓库信息失败"));
                            }
                        }
                        catch(Exception e){

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void uplOrderIn(final int type, final IConDbListener callback){
        Log.i("sync", "up_order_in");
        JSONArray data = dataSyncUtils.readUploadData("order_in");
        //发送
        String jsonString = data.toString();
        if(jsonString.equals("[]")){
            callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
            return;
        }
        RequestBody postData = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        apiService.pushOrderIn(token, postData)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SyncResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(SyncResult respResult) {
                        if(respResult.getCode() == 200){
                            dataSyncUtils.updateOrderList("order_in");
                            callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                        }
                        else{
                            callback.onFailure(type, new BaseResultBean(101, "上传入库单失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void uplOrderOut(final int type, final IConDbListener callback){
        Log.i("sync", "up_order_out");
        JSONArray data = dataSyncUtils.readUploadData("order_out");
        //发送
        String jsonString = data.toString();
        if(jsonString.equals("[]")){
            callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
            return;
        }
        RequestBody postData = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        apiService.pushOrderOut(token, postData)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SyncResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(SyncResult respResult) {
                        if(respResult.getCode() == 200){
                            dataSyncUtils.updateOrderList("order_out");
                            callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                        }
                        else{
                            callback.onFailure(type, new BaseResultBean(101, "上传出库单失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
    private void uplOrderInvt(final int type, final IConDbListener callback){
        Log.i("sync", "up_order_invt");
        JSONArray data = dataSyncUtils.readUploadData("order_invt");
        //发送
        String jsonString = data.toString();
        if(jsonString.equals("[]")){
            callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
            return;
        }
        RequestBody postData = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        apiService.pushOrderInvt(token, postData)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SyncResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(SyncResult respResult) {
                        if(respResult.getCode() == 200){
                            dataSyncUtils.updateOrderList("order_invt");
                            callback.onSuccess(DataSyncPresenter.TYPE_UP_ITEM_COMPLETE, type, new BaseResultBean(0, ""));
                        }
                        else{
                            callback.onFailure(type, new BaseResultBean(101, "上传盘点单失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(type, new BaseResultBean(102, "连接服务器失败：" + e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.i("complete", "complete");
                    }
                });
    }
}
