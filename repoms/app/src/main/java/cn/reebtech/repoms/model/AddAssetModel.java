package cn.reebtech.repoms.model;

import android.util.Log;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.reebtech.repoms.bean.AssetBean;
import cn.reebtech.repoms.bean.BaseResultBean;
import cn.reebtech.repoms.contact.AddAssetContact;
import cn.reebtech.repoms.model.entity.AssetClsct;
import cn.reebtech.repoms.model.entity.Assets;
import cn.reebtech.repoms.model.entity.WareHouse;
import cn.reebtech.repoms.model.greendao.AssetClsctDao;
import cn.reebtech.repoms.model.greendao.AssetsDao;
import cn.reebtech.repoms.model.greendao.Order_In_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_Invt_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_Out_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_Req_DetailDao;
import cn.reebtech.repoms.model.greendao.Order_Ret_DetailDao;
import cn.reebtech.repoms.model.greendao.WareHouseDao;
import cn.reebtech.repoms.presenter.AddAssetPresenter;
import cn.reebtech.repoms.util.GreenDaoManager;
import cn.reebtech.repoms.util.IConDbListener;

public class AddAssetModel extends BaseDBModel<AssetClsct, String, IConDbListener<Integer, AssetBean, BaseResultBean>> implements AddAssetContact.AddAssetMdl {
    @Override
    public void loadAssetClsFst(String warehouse, IConDbListener callback) {
        AssetClsctDao table = getAssetClsDao();
        try{
            List<AssetClsct> records = table.queryBuilder()
                    .where(AssetClsctDao.Properties.Parent.eq("0"), AssetClsctDao.Properties.Location.eq(warehouse))
                    .orderAsc(AssetClsctDao.Properties.Name)
                    .list();
//                    .where(AssetClsctDao.Properties.Parent.eq("0"))

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (AssetClsct record: records) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("id", record.getId());
                item.put("name", record.getName());
                data.add(item);
            }
            callback.onSuccess(AddAssetPresenter.TYPE_INIT_DATA_CLSFST, data, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(AddAssetPresenter.TYPE_INIT_DATA_CLSFST, new BaseResultBean(AddAssetPresenter.ERROR_CODE_LOAD_CLSFST_FAILED, e.getMessage()));
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
    public void loadBindedRFIDs(IConDbListener callback) {
        AssetsDao table = getAssetDao();
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        try{
            List<Assets> records = table.queryBuilder().where(AssetsDao.Properties.Rfid.notEq(""), AssetsDao.Properties.Rfid.notEq("null"), AssetsDao.Properties.Rfid.isNotNull()).list();
            for(Assets record : records){
                Map<String, String> item = new HashMap<String, String>();
                if(record.getRfid() != null){
                    item.put("rfid", record.getRfid());
                    Log.i("asset rfid list", record.getRfid());
                    result.add(item);
                }
            }
            Log.i("loadBindedRfids", "count:" + result.size());
            callback.onSuccess(AddAssetPresenter.TYPE_LOAD_BINDED_RFIDS, result, new BaseResultBean(0, ""));
        }
        catch(Exception e){
            callback.onFailure(AddAssetPresenter.TYPE_LOAD_BINDED_RFIDS, new BaseResultBean(AddAssetPresenter.ERROR_CODE_LOAD_BINDED_RFIDS_FAILED, e.getMessage()));
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
    public void checkRfidValid(String rfid, IConDbListener callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rfid", rfid);
        //入库单
        Order_In_DetailDao tb_order_in = getOrderInDetailDao();
        Order_Out_DetailDao tb_order_out = getOrderOutDetailDao();
        Order_Req_DetailDao tb_order_req = getOrderReqDetailDao();
        Order_Ret_DetailDao tb_order_ret = getOrderRetDetailDao();
        Order_Invt_DetailDao tb_order_invt = getOrderInvtDetailDao();
        if(tb_order_in.queryBuilder().where(Order_In_DetailDao.Properties.Uploaded.eq(false), Order_In_DetailDao.Properties.Rfid.eq(rfid)).list().size() > 0){
            result.put("valid", false);
            result.put("canbind", false);
            callback.onSuccess(AddAssetPresenter.TYPE_CHECK_RFID_VALID, result, new BaseResultBean(0, ""));
            return;
        }
        else if(tb_order_out.queryBuilder().where(Order_Out_DetailDao.Properties.Uploaded.eq(false), Order_Out_DetailDao.Properties.Asset.eq(rfid)).list().size() > 0){
            result.put("valid", false);
            result.put("canbind", false);
            callback.onSuccess(AddAssetPresenter.TYPE_CHECK_RFID_VALID, result, new BaseResultBean(0, ""));
            return;
        }
        else if(tb_order_req.queryBuilder().where(Order_Req_DetailDao.Properties.Uploaded.eq(false), Order_Req_DetailDao.Properties.Asset.eq(rfid)).list().size() > 0){
            result.put("valid", false);
            result.put("canbind", false);
            callback.onSuccess(AddAssetPresenter.TYPE_CHECK_RFID_VALID, result, new BaseResultBean(0, ""));
            return;
        }
        else if(tb_order_ret.queryBuilder().where(Order_Ret_DetailDao.Properties.Uploaded.eq(false), Order_Ret_DetailDao.Properties.Asset.eq(rfid)).list().size() > 0){
            result.put("valid", false);
            result.put("canbind", false);
            callback.onSuccess(AddAssetPresenter.TYPE_CHECK_RFID_VALID, result, new BaseResultBean(0, ""));
            return;
        }
        else if(tb_order_invt.queryBuilder().where(Order_Invt_DetailDao.Properties.Uploaded.eq(false), Order_Invt_DetailDao.Properties.Asset.eq(rfid)).list().size() > 0){
            result.put("valid", false);
            result.put("canbind", false);
            callback.onSuccess(AddAssetPresenter.TYPE_CHECK_RFID_VALID, result, new BaseResultBean(0, ""));
            return;
        }
        result.put("valid", true);
        AssetsDao tb_asset = getAssetDao();
        if(tb_asset.queryBuilder().where(AssetsDao.Properties.Rfid.eq(rfid)).list().size() > 0){
            result.put("canbind", false);

        }
        else{
            result.put("canbind", true);
        }
        callback.onSuccess(AddAssetPresenter.TYPE_CHECK_RFID_VALID, result, new BaseResultBean(0, ""));
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
    private AssetClsctDao getAssetClsDao(){
        return GreenDaoManager.getInstance().getSession().getAssetClsctDao();
    }
    private AssetsDao getAssetDao(){
        return GreenDaoManager.getInstance().getSession().getAssetsDao();
    }
    private WareHouseDao getWareHouseDao(){
        return GreenDaoManager.getInstance().getSession().getWareHouseDao();
    }
    private Order_In_DetailDao getOrderInDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_In_DetailDao();
    }
    private Order_Out_DetailDao getOrderOutDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Out_DetailDao();
    }
    private Order_Req_DetailDao getOrderReqDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Req_DetailDao();
    }
    private Order_Ret_DetailDao getOrderRetDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Ret_DetailDao();
    }
    private Order_Invt_DetailDao getOrderInvtDetailDao(){
        return GreenDaoManager.getInstance().getSession().getOrder_Invt_DetailDao();
    }
}
