package cn.reebtech.repoms.model;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.bean.LabelBendingBean;
import cn.reebtech.repoms.bean.LoginBean;
import cn.reebtech.repoms.contact.LabelBendingContact;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.AddAssetPresenter;
import cn.reebtech.repoms.presenter.LabelBendingPresenter;
import cn.reebtech.repoms.util.DBUtils;
import cn.reebtech.repoms.util.DataSyncUtils;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;
import cn.reebtech.repoms.util.net.ApiService;
import cn.reebtech.repoms.util.net.http.RetrofitClient;
import cn.reebtech.repoms.util.net.result.LoginResult;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LabelBendingModel extends BaseDBModel<AssetClsct, String, IConDbListener<Integer, AssetBean, BaseResultBean>>
        implements LabelBendingContact.LabelBendingMdl  {

    private ApiService apiService;
    private String token;
    private DataSyncUtils dataSyncUtils;

    public LabelBendingModel(LabelBendingContact.LabelBendingUI view){
        apiService = RetrofitClient.getInstance((Activity)view).provideApiService();
        dataSyncUtils = new DataSyncUtils();

    }

/*
    public void loginServer(final int type, final IConDbListener callback) {
        LoginBean loginBean = DBUtils.getServerLogin();
        if(loginBean.getUsrname() == null || loginBean.getUsrname().equals("") || loginBean.getPasswd() == null || loginBean.getPasswd().equals("")) {
            callback.onFailure(LabelBendingPresenter.TYPE_LOGIN_SERVER, new BaseResultBean(103, "服务器配置信息无效，请重新配置"));
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
                            callback.onSuccess(LabelBendingPresenter.TYPE_LOGIN_SERVER, type, new BaseResultBean(0, ""));
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
*/

    @Override
    public void loadBGS(IConDbListener callback){
        WareHouseDao wareHouseDao = getWareHouseDao();
        try{
            List<WareHouse> records = wareHouseDao.queryBuilder().list();
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (WareHouse record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(LabelBendingPresenter.TYPE_INIT_DATA_BGS, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(LabelBendingPresenter.TYPE_INIT_DATA_BGS, new BaseResultBean(LabelBendingPresenter.ERROR_CODE_LOAD_BGS_FAILED, e.getMessage()));
        }

    }

    @Override
    public void loadAssetClsFst(IConDbListener callback,String gbs_key) {
        AssetClsctDao table = getAssetClsDao();
        try{
            List<AssetClsct> records = table.queryBuilder()
                    .where(AssetClsctDao.Properties.Parent.eq("0"),AssetClsctDao.Properties.Location.eq(gbs_key))
                    .orderAsc(AssetClsctDao.Properties.Name)
                    .list();
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (AssetClsct record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(LabelBendingPresenter.TYPE_INIT_DATA_CLSFST, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(LabelBendingPresenter.TYPE_INIT_DATA_CLSFST, new BaseResultBean(LabelBendingPresenter.ERROR_CODE_LOAD_CLSFST_FAILED, e.getMessage()));
        }
    }

    @Override
    public void loadAssetClsScd(String parent, IConDbListener callback) {
        AssetClsctDao table = getAssetClsDao();
        try{
            List<AssetClsct> records = table.queryBuilder()
                    .where(AssetClsctDao.Properties.Parent.eq(parent))
                    .orderAsc(AssetClsctDao.Properties.Name)
                    .list();
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (AssetClsct record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(AddAssetPresenter.TYPE_INIT_DATA_CLSSCD, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(AddAssetPresenter.TYPE_INIT_DATA_CLSSCD, new BaseResultBean(AddAssetPresenter.ERROR_CODE_LOAD_CLSSCD_FAILED, e.getMessage()));
        }
    }

    @Override
    public void loadSugLocation(String clssscd, IConDbListener callback) {
        String result = "";
        AssetClsctDao table = getAssetClsDao();
        AssetClsct record = table.queryBuilder().where(AssetClsctDao.Properties.Id.eq(clssscd)).unique();
        if(record != null && !record.getLocation().equals("")){
            result = getLocation(record.getLocation());
        }
        callback.onSuccess(AddAssetPresenter.TYPE_LOAD_SUG_LOCATION, result, new BaseResultBean(0, ""));
    }

    @Override
    public void saveOrder(LabelBendingBean data, IConDbListener callback) {

    }

    @Override
    public void loadWareHouse(IConDbListener callback) {

    }

    @Override
    public void loadFromUsers(IConDbListener callback) {

    }

    @Override
    public void loadMgrs(IConDbListener callback) {

    }

    @Override
    public void loadAsset(String rfid, IConDbListener callback) {

    }


    private AssetClsctDao getAssetClsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }

    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }


    private String getLocation(String id){
        StringBuilder result = new StringBuilder();
        if(id != null && !id.equals("")){
            WareHouseDao table = getWareHouseDao();
            WareHouse record = table.queryBuilder().where(WareHouseDao.Properties.Id.eq(id)).unique();
            if(record != null){
                result.insert(0, record.getName() + (result.length() == 0 ? "" : "/"));
                if(!record.getParent().equals("0")){
                    result.insert(0, getLocation(record.getParent()) + (result.length() == 0 ? "" : "/"));
                }
            }
        }
        return result.toString();
    }






}
